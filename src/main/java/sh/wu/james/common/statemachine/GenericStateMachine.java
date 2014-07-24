package sh.wu.james.common.statemachine;

import java.util.List;
import java.util.Map;

import sh.wu.james.common.statemachine.factory.StateFactory;
import sh.wu.james.common.statemachine.listener.StateListener;
import sh.wu.james.common.statemachine.preprocessor.PreProcessor;

/**
 * The method define in interface is generic methods of state machine. Comparing to BizInterface, these methods take no
 * relationship to concrete business logic.
 * 
 * @author James Wu
 * 
 * @param <T>
 * @param <P>
 * @param <S>
 */
public interface GenericStateMachine<T, P, S> {

	public List<StateListener<T, P, S>> getListeners();

	public List<PreProcessor> getPreProcessors();

	public void setPreProcessors(List<PreProcessor> preProcessors);

	public void setExtraInfo(Map<String, Object> info);

	public Map<String, Object> getExtraInfo();

	public void setNextStatus(S next);

	public S getNextStatus();

	public P getPayload();

	public void setPayload(P object);

	public void setFactory(StateFactory<T, P, S> factory);

	public void addListener(StateListener<T, P, S>... listenerList);

	public void preProcess();

}