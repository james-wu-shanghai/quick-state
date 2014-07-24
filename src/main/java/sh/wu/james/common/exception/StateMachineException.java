package sh.wu.james.common.exception;

public class StateMachineException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2016441557559646044L;

	public StateMachineException(String msg) {
		super(msg);
	}

	public StateMachineException(String msg, Throwable t) {
		super(msg, t);
	}

	public StateMachineException(Throwable t) {
		super(t);
	}

}
