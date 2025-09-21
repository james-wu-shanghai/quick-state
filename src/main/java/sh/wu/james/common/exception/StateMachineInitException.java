package sh.wu.james.common.exception;

public class StateMachineInitException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2016441557559646044L;

	public StateMachineInitException(String msg) {
		super(msg);
	}

	public StateMachineInitException(String msg, Throwable t) {
		super(msg, t);
	}

	public StateMachineInitException(Throwable t) {
		super(t);
	}

}
