package next.operator.diagnostic.exception;

/** 薇兒拋錯 */
public class DiagnosticException extends RuntimeException {

  public DiagnosticException() {
  }

  public DiagnosticException(String message) {
    super(message);
  }

  public DiagnosticException(String message, Throwable cause) {
    super(message, cause);
  }

  public DiagnosticException(Throwable cause) {
    super(cause);
  }

  public DiagnosticException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
