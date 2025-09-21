package sh.wu.james.common.exception;

public class NotSupportOperationException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4550332054725512762L;

	public NotSupportOperationException(String msg) {
		super(msg);
	}

	public NotSupportOperationException(String msg, Throwable t) {
		super(msg, t);
	}

	public NotSupportOperationException(Throwable t) {
		super(t);
	}
}
