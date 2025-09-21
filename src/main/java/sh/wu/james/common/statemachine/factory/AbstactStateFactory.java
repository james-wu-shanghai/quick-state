package sh.wu.james.common.statemachine.factory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public abstract class AbstactStateFactory<T, P, S> implements ApplicationContextAware, StateFactory<T, P, S> {
    protected ApplicationContext ctx;

    protected String statusFieldName = null;
    protected String stateFieldName = null;
    protected String statePayloadFieldName = null;

    public String getStatusFieldName() {
        return statusFieldName;
    }

    public String getStateFieldName() {
        return stateFieldName;
    }

    public String getStatePayloadFieldName() {
        return statePayloadFieldName;
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        this.ctx = ctx;
    }

}