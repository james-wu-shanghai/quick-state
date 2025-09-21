package sh.wu.james.common.statemachine.preprocessor;

import sh.wu.james.common.statemachine.GenericStateMachine;

/**
 * Class implements the interface can process the GenericState before methods calling in state.
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
public interface PreProcessor {
	<T, P, S> void process(GenericStateMachine<T, P, S> s);
}
