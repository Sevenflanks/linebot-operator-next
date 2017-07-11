package next.operator.will.exception;

/** 薇兒拋錯 */
public class WillException extends RuntimeException {

  public WillException() {
  }

  public WillException(String message) {
    super(message);
  }

  public WillException(String message, Throwable cause) {
    super(message, cause);
  }

  public WillException(Throwable cause) {
    super(cause);
  }

  public WillException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
