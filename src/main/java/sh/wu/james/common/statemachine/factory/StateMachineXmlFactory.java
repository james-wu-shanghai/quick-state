package sh.wu.james.common.statemachine.factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.time.StopWatch;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import sh.wu.james.common.exception.NotSupportOperationException;
import sh.wu.james.common.exception.StateMachineInitException;
import sh.wu.james.common.statemachine.GenericStateMachine;
import sh.wu.james.common.statemachine.fork.Judger;
import sh.wu.james.common.statemachine.listener.StateListener;
import sh.wu.james.common.statemachine.preprocessor.PreProcessor;
import sh.wu.james.common.statemachine.xml.Fork;
import sh.wu.james.common.statemachine.xml.ListenerDefine;
import sh.wu.james.common.statemachine.xml.Path;
import sh.wu.james.common.statemachine.xml.PreProcessorDefine;
import sh.wu.james.common.statemachine.xml.State;
import sh.wu.james.common.statemachine.xml.StateMachineDefine;
import sh.wu.james.common.statemachine.xml.StatusStateMapping;
import sh.wu.james.common.statemachine.xml.StatusStateMappings;
import sh.wu.james.common.statemachine.xml.SupportMethod;
import sh.wu.james.common.statemachine.xml.XmlFactory;
import sh.wu.james.common.utils.Logger;
import sh.wu.james.common.utils.ReflectionUtil;

@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
@Component
public class StateMachineXmlFactory<T, P, S> extends AbstactStateFactory<T, P, S> {
	private static String STATE_PAYLOAD_FIELD_NAME = "payload";
	@Value("${state.machine.define.config.path}")
	private String xmlConfigFilePath;

	private Class bizIfaceClazz = null;
	private Class payloadClazz = null;
	private Class statusEntityClazz = null;


	private List<StateListener> stateListeners = new ArrayList<StateListener>();

	private Map<Object, String> stateStatusMap = new HashMap<Object, String>();

	private List<PreProcessor> emtpyStatusPreprocessor = new ArrayList<PreProcessor>();
	private Map<String, PreProcessor> preProcessorMap = new HashMap<String, PreProcessor>();

	private DynamicSupportMethodHandlerBuilder emptyStateHandlerBuilder;
	private Map<String, DynamicSupportMethodHandlerBuilder> invocationHandlerBuilderRegistry = new HashMap<String, DynamicSupportMethodHandlerBuilder>();

	@PostConstruct
	private void initFromXml() {
		Logger.info(this, "Start init state machine.");
		StopWatch sw = new StopWatch();
		sw.start();
		StateMachineDefine stateDefine = XmlFactory.unmarshall(getClass().getResourceAsStream(xmlConfigFilePath),
				StateMachineDefine.class);
		bizIfaceClazz = FactoryUtils.findClassWithException(stateDefine.getBizInterface().getClassName());
		payloadClazz = FactoryUtils.findClassWithException(stateDefine.getPayload().getClassName());
		statusEntityClazz = FactoryUtils.findClassWithException(stateDefine.getPayload().getStatusEntity()
				.getClassName());
		stateFieldName = stateDefine.getPayload().getStateEntity().getFieldName();
		statePayloadFieldName = stateDefine.getPayload().getStateEntity().getPayloadFieldName();
		statusFieldName = stateDefine.getPayload().getStatusEntity().getFieldName();

		// TODO: state machine check

		convertListener(stateDefine.getListenersDefine());

		for (State state : stateDefine.getStates()) {
			invocationHandlerBuilderRegistry.put(state.getName(), new DynamicSupportMethodHandlerBuilder(state));
		}
		convertStateStatusMap(stateDefine.getMapping());
		sw.stop();
		Logger.info(this, String.format("State machine init finished, total %s ms been taken.", sw.getTime()));
	}

	private void convertStateStatusMap(StatusStateMappings mapping) {
		if (mapping.getEmpty() != null) {
			String stateName = mapping.getEmpty().getState();
			emptyStateHandlerBuilder = invocationHandlerBuilderRegistry.get(stateName);
		}
		for (StatusStateMapping map : mapping.getMap()) {
			Object status = FactoryUtils.buildStatus(statusEntityClazz, map.getStatusMark());
			stateStatusMap.put(status, map.getStateName());
		}
	}

	private void convertListener(List<ListenerDefine> listenersDefine) {
		for (ListenerDefine lsnDef : listenersDefine) {
			try {
				StateListener listener = (StateListener) FactoryUtils.getBeanByType(ctx,
						Class.forName(lsnDef.getClassName()));
				stateListeners.add(listener);
			} catch (Exception e) {
				Logger.error(this, "Failed to init state listener", e);
				throw new StateMachineInitException(e);
			}
		}
	}

	@Override
	public T initState(P req) {
		unbindStateAndPayload(req);
		DynamicSupportMethodHandler handler = null;
		Object status = ReflectionUtil.getValue(req, statusFieldName);
		if (status == null) {
			handler = emptyStateHandlerBuilder.build();
		} else {
			String stateName = stateStatusMap.get(status);
			handler = invocationHandlerBuilderRegistry.get(stateName).build();
		}
		T proxyState = (T) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { bizIfaceClazz,
				GenericStateMachine.class }, handler);

		GenericStateMachine generic = initGenericState(req, handler);
		bindStateAndPayload(req, proxyState, generic);
		return proxyState;
	}

	private void unbindStateAndPayload(Object payload) {
		Object state = ReflectionUtil.getValue(payload, stateFieldName);
		if (state != null) {
			ReflectionUtil.setValue(state, STATE_PAYLOAD_FIELD_NAME, null);
			ReflectionUtil.setValue(payload, stateFieldName, null);
		}
	}

	private GenericStateMachine initGenericState(P payload, DynamicSupportMethodHandler handler) {
		GenericStateMachine generic = (GenericStateMachine) FactoryUtils.getBeanByType(ctx, GenericStateMachine.class);
		generic.setFactory(this);
		generic.setPayload(payload);
		generic.addListener(stateListeners.toArray(new StateListener[stateListeners.size()]));
		generic.setPreProcessors(handler.preProcessor);
		generic.preProcess();
		return generic;
	}

	private void bindStateAndPayload(P payload, T state, GenericStateMachine generic) {
		Object invokHndlr = ReflectionUtil.getValue(state, "h");
		ReflectionUtil.setValue(invokHndlr, "assignee", generic);

		ReflectionUtil.setValue(state, STATE_PAYLOAD_FIELD_NAME, payload);
		ReflectionUtil.setValue(payload, stateFieldName, state);
	}

	private class DynamicSupportMethodHandler implements InvocationHandler {
		private String stateName;
		private List<PreProcessor> preProcessor;
		private GenericStateMachine assignee;
		private Map<String, String> methodNextStatusMap;
		private Map<String, List<String>> methodEventsMap;

		private Map<String, Map<String, String>> forkNextStatusMap = new HashMap<String, Map<String, String>>();
		private Map<String, Map<String, List<String>>> forkEventsMap = new HashMap<String, Map<String, List<String>>>();
		private Judger judger;

		public void setAssignee(GenericStateMachine assignee) {
			this.assignee = assignee;
		}

		public DynamicSupportMethodHandler(String stateName, Map<String, String> methodNextStatusMap,
				Map<String, List<String>> methodEventsMap, List<PreProcessor> preProcessor,
				Map<String, Map<String, String>> forkNextStatusMap,
				Map<String, Map<String, List<String>>> forkEventsMap, Judger judger) {
			this.stateName = stateName;
			this.preProcessor = preProcessor;
			this.methodEventsMap = methodEventsMap;
			this.methodNextStatusMap = methodNextStatusMap;

			this.judger = judger;
			this.forkNextStatusMap = forkNextStatusMap;
			this.forkEventsMap = forkEventsMap;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			if (method.getDeclaringClass() == Object.class) {
				return method.invoke(method.getDeclaringClass(), args);
			}
			if (method.getDeclaringClass() == GenericStateMachine.class) {
				return method.invoke(assignee, args);
			}
			String methodName = method.getName();
			if (methodNextStatusMap.get(methodName) != null) {
				String nextStatus = methodNextStatusMap.get(methodName);

				List<String> evts = methodEventsMap.get(methodName);
				// Find method GenericStateImpl.invokeOperation(S next, String... evts)
				Method proxyMethod = assignee.getClass().getDeclaredMethod("invokeNext",
						new Class[] { Object.class, List.class });

				// Reflection call method GenericStateImpl.invokeOperation(S next, String... evts)
				return proxyMethod.invoke(assignee,
						new Object[] { FactoryUtils.buildStatus(statusEntityClazz, nextStatus), evts });
			} else if (forkNextStatusMap.get(methodName) != null) {
				Map<String, String> forkStatusMap = forkNextStatusMap.get(methodName);
				Map<String, List<String>> forkEventMap = forkEventsMap.get(methodName);

				Method proxyMethod = assignee.getClass().getDeclaredMethod("invokeFork",
						new Class[] { Map.class, Map.class, Judger.class });
				return proxyMethod.invoke(assignee,
						new Object[] { FactoryUtils.buildStatusMap(statusEntityClazz, forkStatusMap), forkEventMap,
								judger });
			} else {
				throw new NotSupportOperationException(methodName + " is not supported in current state");
			}
		}
	}

	private class DynamicSupportMethodHandlerBuilder {
		private String stateName;
		private List<PreProcessor> preProcessor = new ArrayList<PreProcessor>();;

		private Map<String, String> methodNextStatusMap = new HashMap<String, String>();
		private Map<String, List<String>> methodEventsMap = new HashMap<String, List<String>>();

		private Judger judger = null;
		private Map<String, Map<String, String>> forkNextStatusMap = new HashMap<String, Map<String, String>>();
		private Map<String, Map<String, List<String>>> forkEventsMap = new HashMap<String, Map<String, List<String>>>();

		public DynamicSupportMethodHandlerBuilder(State state) {
			this.stateName = state.getName();
			if (state.getPreProcessor() != null) {
				for (PreProcessorDefine def : state.getPreProcessor()) {
					preProcessor.add((PreProcessor) FactoryUtils.getBeanByType(ctx,
							FactoryUtils.findClassWithException(def.getClassName())));
				}
			}
			List<SupportMethod> supportMethods = state.getSupportMethods();
			if (supportMethods != null) {
				for (SupportMethod method : supportMethods) {
					if (null != method.getFork()) {
						Fork fork = method.getFork();
						this.judger = FactoryUtils.getBeanByType(ctx,
								FactoryUtils.findClassWithException(fork.getJudgeClass()));
						List<Path> paths = fork.getPaths();
						Map<String, String> pathStatusMap = new HashMap<String, String>();
						Map<String, List<String>> pathEvtsMap = new HashMap<String, List<String>>();
						for (Path path : paths) {

							pathStatusMap.put(path.getName(), path.getNextStatus());
							pathEvtsMap.put(path.getName(), FactoryUtils.buildEvent(path.getEvents()));
						}
						forkNextStatusMap.put(method.getName(), pathStatusMap);
						forkEventsMap.put(method.getName(), pathEvtsMap);
					} else if (null != method.getNextStep()) {
						methodNextStatusMap.put(method.getName(), method.getNextStep().getNextStatus());
						methodEventsMap
								.put(method.getName(), FactoryUtils.buildEvent(method.getNextStep().getEvents()));
					} else {
						throw new StateMachineInitException(
								"Found support method with no step definition. Method name:" + method.getName());
					}
				}
			}
		}

		public DynamicSupportMethodHandler build() {
			return new DynamicSupportMethodHandler(stateName, methodNextStatusMap, methodEventsMap, preProcessor,
					forkNextStatusMap, forkEventsMap, judger);
		}
	}

}
