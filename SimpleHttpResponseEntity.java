package it.httpmanager.example;


public class SimpleHttpResponseEntity {

	private final int httpStatus;
	private final String bodyText;

	public SimpleHttpResponseEntity(int httpStatus, String bodyText) {
		this.httpStatus = httpStatus;
		this.bodyText = bodyText;
	}

	public int getHttpStatus() {
		return httpStatus;
	}

	public String getBodyText() {
		return bodyText;
	}

	public boolean isResponseOK() {
		return (this.httpStatus >= 200 && this.httpStatus < 300);
	}

	public boolean isResponseRedirect() {
		return (this.httpStatus >= 300 && this.httpStatus < 400);
	}

	@Override
	public String toString() {
		return "SimpleHttpResponseEntity [httpStatus=" + httpStatus + ", bodyText=" + bodyText + "]";
	}

}
