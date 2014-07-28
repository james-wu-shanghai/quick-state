package sh.wu.james.common.statemachine.fork;

import sh.wu.james.common.statemachine.GenericStateMachine;

public interface Judger<T, P, S> {
	/**
	 * It's interface of a Judger which used in 'Fork' step to deciede next status of the machine
	 * 
	 * @param state
	 * @return
	 */
	public String judge(GenericStateMachine<T, P, S> state);
}
