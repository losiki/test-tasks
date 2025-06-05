package losiki.task001.service;

import org.springframework.http.HttpStatus;

public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = 5322098724502952380L;

	private HttpStatus status;

	public ServiceException(HttpStatus status, String message) {
		super(message);
		this.status = status;
	}

	public ServiceException(HttpStatus status) {
		this(status, null);
	}

	public HttpStatus getStatus() {
		return status;
	}
}
