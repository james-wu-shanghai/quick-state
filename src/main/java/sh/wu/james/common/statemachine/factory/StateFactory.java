package sh.wu.james.common.statemachine.factory;

/**
 * @author wujian
 * 
 * @param <T>
 *            is a java interface contains all business operations, the API also be defined in XML
 * @param <P>
 *            is Payload class that state machine holds
 * @param <S>
 *            is Status mark which will be used to decide the state
 */
public interface StateFactory<T, P, S> {
	public T initState(P req);

}