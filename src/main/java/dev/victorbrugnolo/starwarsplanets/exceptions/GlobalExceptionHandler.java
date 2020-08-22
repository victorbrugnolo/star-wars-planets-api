package dev.victorbrugnolo.starwarsplanets.exceptions;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  private static final String EXCEPTION_LOG_MSG = "e=%s,m=%s";

  @ExceptionHandler(APIException.class)
  protected ResponseEntity<ErrorMessage> processAPIException(final APIException ex) {
    final ResponseStatus status = ex.getClass().getDeclaredAnnotation(ResponseStatus.class);
    logE(ex);
    return new ResponseEntity<>(ex.getError(),
        Objects.nonNull(status) ? status.code() : HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private static void logE(final Exception e) {
    final String message = String
        .format(EXCEPTION_LOG_MSG, e.getClass().getSimpleName(), e.getMessage());
    log.error(message, e);
  }

}
