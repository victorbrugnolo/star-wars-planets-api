package dev.victorbrugnolo.starwarsplanets.exceptions;

import dev.victorbrugnolo.starwarsplanets.dtos.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BadRequestException extends APIException {

  private static final long serialVersionUID = 1L;

  public BadRequestException(final Throwable cause) {
    super(cause);
  }

  public BadRequestException(final ErrorMessage error) {
    super(error);
  }

  public BadRequestException(final String error) {
    super(error);
  }

}
