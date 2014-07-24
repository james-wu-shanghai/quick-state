package sh.wu.james.common.statemachine.preprocessor;

import sh.wu.james.common.statemachine.GenericState;

/**
 * Class implements the interface can process the GenericState before methods calling in state.
 * 
 * @author wujian
 * 
 * @param <T>
 *            is a java interface contains all business operations, the API also be defined in XML
 * @param <P>
 *            is Payload class that state machine holds
 * @param <S>
 *            is Status mark which will be used to decide the state
 */
public interface PreProcessor {
	<T, P, S> void process(GenericState<T, P, S> s);
}
