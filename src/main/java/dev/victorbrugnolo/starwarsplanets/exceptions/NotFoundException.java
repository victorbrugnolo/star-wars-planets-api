package dev.victorbrugnolo.starwarsplanets.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundException extends APIException {

  private static final long serialVersionUID = 1L;

  public NotFoundException(final Throwable cause) {
    super(cause);
  }

  public NotFoundException(final ErrorMessage error) {
    super(error);
  }

  public NotFoundException(final String error) {
    super(error);
  }

}

