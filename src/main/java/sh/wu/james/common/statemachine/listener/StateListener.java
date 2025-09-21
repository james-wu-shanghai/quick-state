package sh.wu.james.common.statemachine.listener;

import sh.wu.james.common.statemachine.GenericStateMachine;

/**
 * Class implements the interface is for handling the event during business methods called 
 * 
 * @author James Wu
 *
 * @param <T>
 *            is a java interface contains all business operations, the API also be defined in XML
 * @param <P>
 *            is Payload class that state machine holds
 * @param <S>
 *            is Status mark which will be used to decide the state
 */
public interface StateListener<T, P, S> {
	void onEvent(GenericStateMachine<T, P, S> state, String eventType);
}
