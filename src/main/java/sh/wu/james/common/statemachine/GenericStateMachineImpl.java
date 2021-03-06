package sh.wu.james.common.statemachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sh.wu.james.common.statemachine.factory.AbstactStateFactory;
import sh.wu.james.common.statemachine.fork.Judger;
import sh.wu.james.common.statemachine.listener.StateListener;
import sh.wu.james.common.statemachine.preprocessor.PreProcessor;
import sh.wu.james.common.utils.ReflectionUtil;

@Component
@Scope("prototype")
public class GenericStateMachineImpl<T, P, S> implements GenericStateMachine<T, P, S> {
    private AbstactStateFactory<T, P, S> factory;

    public void setFactory(AbstactStateFactory<T, P, S> factory) {
        this.factory = factory;
    }

    // 处理状态变化的侦听器，具體业务操作主要在这里实现
    private List<StateListener<T, P, S>> listeners = new ArrayList<StateListener<T, P, S>>();

    // 被状态机反映的本体对象
    private P payload;

    //上一个状态标志
    private S previous;

    // 当前的状态标志，通常是 payload 的一个变量
    private S current;

    // 这个容器存储用来在预处理State对象的方法类，通常不需要使用
    private List<PreProcessor> preProcessors = new ArrayList<PreProcessor>();

    // 这个Map可以用来放一些预处理需要的信息，这些信息无法存放在Payload中。通常不许要使用
    private Map<String, Object> extraInfo = new HashMap<String, Object>();

    @Override
    public List<StateListener<T, P, S>> getListeners() {
        return listeners;
    }

    @Override
    public List<PreProcessor> getPreProcessors() {
        return preProcessors;
    }

    @Override
    public void setPreProcessors(List<PreProcessor> preProcessors) {
        this.preProcessors = preProcessors;
    }

    public void preProcess() {
        for (PreProcessor processor : preProcessors) {
            processor.process(this);
        }
    }

    @Override
    public void setExtraInfo(Map<String, Object> info) {
        this.extraInfo = info;
    }

    @Override
    public Map<String, Object> getExtraInfo() {
        return extraInfo;
    }

    public S getPrevious() {
        return previous;
    }

    public void setPrevious(S previous) {
        this.previous = previous;
    }

    public S getCurrent() {
        return current;
    }

    public void setCurrent(S current) {
        this.current = current;
    }

    @Override
    public P getPayload() {
        return payload;
    }

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

    public T invokeNext(S current, List<String> evts) {
        setPrevious((S) ReflectionUtil.getValue(payload, factory.getStatusFieldName()));
        setCurrent(current);
        ReflectionUtil.setValue(payload, factory.getStatusFieldName(), current);
        handleEventByListeners(evts);
        return factory.initState(payload);
    }

    public T invokeFork(Map<String, S> forkStatusMap, Map<String, List<String>> forkEventMap, Judger judger) {
        setPrevious((S) ReflectionUtil.getValue(payload, factory.getStatusFieldName()));

        String path = judger.judge(this);
        S current = forkStatusMap.get(path);
        List<String> evts = forkEventMap.get(path);
        
        setCurrent(current);
        ReflectionUtil.setValue(payload, factory.getStatusFieldName(), current);
        handleEventByListeners(evts);
        return factory.initState(payload);
    }

}
