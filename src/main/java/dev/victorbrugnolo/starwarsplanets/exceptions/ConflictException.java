package dev.victorbrugnolo.starwarsplanets.exceptions;

import dev.victorbrugnolo.starwarsplanets.dtos.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ConflictException extends APIException {

  private static final long serialVersionUID = 1L;

  public ConflictException(final Throwable cause) {
    super(cause);
  }

  public ConflictException(final ErrorMessage error) {
    super(error);
  }

  public ConflictException(final String error) {
    super(error);
  }

}
