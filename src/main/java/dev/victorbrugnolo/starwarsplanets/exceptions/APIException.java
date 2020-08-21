package dev.victorbrugnolo.starwarsplanets.exceptions;

import lombok.Getter;

@Getter
public abstract class APIException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  private final ErrorMessage error;

  public APIException(final Throwable cause) {
    super(cause);
    this.error = ErrorMessage.builder().message(cause.getMessage()).build();
  }

  public APIException(final ErrorMessage error) {
    super(error.getMessage());
    this.error = error;
  }

  public APIException(final String error) {
    super(error);
    this.error = ErrorMessage.builder().message(error).build();
  }

}
