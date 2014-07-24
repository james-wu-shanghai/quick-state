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
import sh.wu.james.common.statemachine.GenericState;
import sh.wu.james.common.statemachine.listener.StateListener;
import sh.wu.james.common.statemachine.preprocessor.PreProcessor;
import sh.wu.james.common.statemachine.xml.EventDefinition;
import sh.wu.james.common.statemachine.xml.ListenerDefine;
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
    private String statusFieldName = null;
    private String stateFieldName = null;
    private String statePayloadFieldName = null;

    private List<StateListener> stateListeners = new ArrayList<StateListener>();

    private Map<Object, String> stateStatusMap = new HashMap<Object, String>();

    private List<PreProcessor> emtpyStatusPreprocessor = new ArrayList<PreProcessor>();
    private Map<String, PreProcessor> preProcessorMap = new HashMap<String, PreProcessor>();

    private DynamicSupportMethodHandlerBuilder emptyStateHandlerBuilder;
    private Map<String, DynamicSupportMethodHandlerBuilder> invocationHandlerBuilderRegistry = new HashMap<String, DynamicSupportMethodHandlerBuilder>();

    @PostConstruct
    private void initFromXml() {
        Logger.info(this,"Start init state machine.");
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
        Logger.info(this,String.format("State machine init finished, total %s ms been taken.", sw.getTime()));
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
                Logger.error(this,"Failed to init state listener", e);
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
                GenericState.class }, handler);

        GenericState generic = initGenericState(req, handler);
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

    private GenericState initGenericState(P payload, DynamicSupportMethodHandler handler) {
        GenericState generic = (GenericState) FactoryUtils.getBeanByType(ctx, GenericState.class);
        generic.setFactory(this);
        generic.setPayload(payload);
        generic.addListener(stateListeners.toArray(new StateListener[stateListeners.size()]));
        generic.setPreProcessors(handler.preProcessor);
        generic.preProcess();
        return generic;
    }

    private void bindStateAndPayload(P payload, T state, GenericState generic) {
        Object invokHndlr = ReflectionUtil.getValue(state, "h");
        ReflectionUtil.setValue(invokHndlr, "assignee", generic);

        ReflectionUtil.setValue(state, STATE_PAYLOAD_FIELD_NAME, payload);
        ReflectionUtil.setValue(payload, stateFieldName, state);
    }

    private class DynamicSupportMethodHandler implements InvocationHandler {
        private String stateName;
        private GenericState assignee;
        private Map<String, String> methodNextStatusMap;
        private Map<String, List<EventDefinition>> methodEventsMap;
        private List<PreProcessor> preProcessor;

        public void setAssignee(GenericState assignee) {
            this.assignee = assignee;
        }

        public DynamicSupportMethodHandler(String stateName, Map<String, String> methodNextStatusMap,
                Map<String, List<EventDefinition>> methodEventsMap, List<PreProcessor> preProcessor) {
            this.stateName = stateName;
            this.methodEventsMap = methodEventsMap;
            this.methodNextStatusMap = methodNextStatusMap;
            this.preProcessor = preProcessor;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getDeclaringClass() == Object.class) {
                return method.invoke(method.getDeclaringClass(), args);
            }
            if (method.getDeclaringClass() == GenericState.class) {
                return method.invoke(assignee, args);
            }
            String methodName = method.getName();
            String nextStatus = methodNextStatusMap.get(methodName);
            List<EventDefinition> evts = methodEventsMap.get(methodName);
            if (nextStatus != null) {
                // Find method GenericStateImpl.invokeOperation(S next, String... evts)
                Method proxyMethod = assignee.getClass().getDeclaredMethod("invokeOperation",
                        new Class[] { Object.class, String[].class });

                // Reflection call method GenericStateImpl.invokeOperation(S next, String... evts)
                return proxyMethod.invoke(
                        assignee,
                        new Object[] { FactoryUtils.buildStatus(statusEntityClazz, nextStatus),
                                FactoryUtils.buildEvent(evts) });
            } else {
                throw new NotSupportOperationException(methodName + " is not supported in current state");
            }
        }
    }

    private class DynamicSupportMethodHandlerBuilder {
        private String stateName;
        private Map<String, String> methodNextStatusMap = new HashMap<String, String>();
        private Map<String, List<EventDefinition>> methodEventsMap = new HashMap<String, List<EventDefinition>>();
        private List<PreProcessor> preProcessor = new ArrayList<PreProcessor>();;

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
                    methodNextStatusMap.put(method.getName(), method.getNextStatus());
                    methodEventsMap.put(method.getName(), method.getEvents());
                }
            }
        }

        public DynamicSupportMethodHandler build() {
            return new DynamicSupportMethodHandler(stateName, methodNextStatusMap, methodEventsMap, preProcessor);
        }
    }

}
