package sh.wu.james.common.statemachine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sh.wu.james.common.statemachine.factory.StateFactory;
import sh.wu.james.common.statemachine.listener.StateListener;
import sh.wu.james.common.statemachine.preprocessor.PreProcessor;

@Component
@Scope("prototype")
public class GenericStateImpl<T, P, S> implements GenericState<T, P, S> {
	private StateFactory<T, P, S> factory;

	public void setFactory(StateFactory<T, P, S> factory) {
		this.factory = factory;
	}

	// 处理状态变化的侦听器，具體业务操作主要在这里实现
	private List<StateListener<T, P, S>> listeners = new ArrayList<StateListener<T, P, S>>();

	// 被状态机反映的本体对象
	private P payload;

	// 下一个状态标志，通常是 payload 的一个变量
	private S next;

	// 这个容器存储用来在预处理State对象的方法类，通常不需要使用
	private List<PreProcessor> preProcessors = new ArrayList<PreProcessor>();

	// 这个Map可以用来放一些预处理需要的信息，这些信息无法存放在Payload中。通常不许要使用
	private Map<String, Object> extraInfo = new HashMap<String, Object>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lufax.common.statemachine.GenericState#getListeners()
	 */
	@Override
	public List<StateListener<T, P, S>> getListeners() {
		return listeners;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lufax.common.statemachine.GenericState#getPreProcessors()
	 */
	@Override
	public List<PreProcessor> getPreProcessors() {
		return preProcessors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lufax.common.statemachine.GenericState#setPreProcessors(java.util.List)
	 */
	@Override
	public void setPreProcessors(List<PreProcessor> preProcessors) {
		this.preProcessors = preProcessors;
	}

	public void preProcess() {
		for (PreProcessor processor : preProcessors) {
			processor.process(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lufax.common.statemachine.GenericState#setExtraInfo(java.util.Map)
	 */
	@Override
	public void setExtraInfo(Map<String, Object> info) {
		this.extraInfo = info;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lufax.common.statemachine.GenericState#getExtraInfo()
	 */
	@Override
	public Map<String, Object> getExtraInfo() {
		return extraInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lufax.common.statemachine.GenericState#setNextStatus(S)
	 */
	@Override
	public void setNextStatus(S next) {
		this.next = next;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lufax.common.statemachine.GenericState#getNextStatus()
	 */
	@Override
	public S getNextStatus() {
		return next;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lufax.common.statemachine.GenericState#getPayload()
	 */
	@Override
	public P getPayload() {
		return payload;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lufax.common.statemachine.GenericState#setPayload(P)
	 */
	@Override
	public void setPayload(P object) {
		this.payload = object;
	}

	public void addListener(StateListener<T, P, S>... listenerList) {
		for (StateListener<T, P, S> lstnr : listenerList) {
			listeners.add(lstnr);
		}
	}

	public void handleEventByListeners(List<String> events) {
		for (String event : events) {
			for (StateListener<T, P, S> listener : listeners) {
				listener.onEvent(this, event);
			}
		}
	}

	public T invokeOperation(S next, String... evts) {
		setNextStatus(next);
		handleEventByListeners(Arrays.asList(evts));
		return factory.initState(payload);
	}

}
