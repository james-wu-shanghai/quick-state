package sh.wu.james.common.statemachine.factory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public abstract class AbstactStateFactory<T, P, S> implements ApplicationContextAware, StateFactory<T, P, S> {
	protected ApplicationContext ctx;

	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		this.ctx = ctx;
	}

}