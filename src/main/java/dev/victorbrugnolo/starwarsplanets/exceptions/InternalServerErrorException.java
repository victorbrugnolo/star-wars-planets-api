
package dev.victorbrugnolo.starwarsplanets.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerErrorException extends APIException {

  private static final long serialVersionUID = 1L;

  public InternalServerErrorException(Throwable cause) {
    super(cause);
  }

  public InternalServerErrorException(ErrorMessage error) {
    super(error);
  }

  public InternalServerErrorException(String error) {
    super(error);
  }

}
