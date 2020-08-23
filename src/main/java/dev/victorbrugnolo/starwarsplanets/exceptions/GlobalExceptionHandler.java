package dev.victorbrugnolo.starwarsplanets.exceptions;

import java.util.Locale;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  private static final String EXCEPTION_LOG_MSG = "e=%s,m=%s";
  private static final String BAD_REQUEST_MSG = "Invalid request";

  private final MessageSource messageSource;

  @ExceptionHandler(APIException.class)
  protected ResponseEntity<ErrorMessage> processAPIException(final APIException ex) {
    final ResponseStatus status = ex.getClass().getDeclaredAnnotation(ResponseStatus.class);
    logE(ex);
    return new ResponseEntity<>(ex.getError(),
        Objects.nonNull(status) ? status.code() : HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(
      final MethodArgumentNotValidException ex) {
    logE(ex);
    final ErrorMessage errorMessage = ErrorMessage.builder().message(BAD_REQUEST_MSG).build();
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      errorMessage.addError(error.getField() + ": " + getMessage(error));
    }
    return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
  }

  private static void logE(final Exception e) {
    final String message = String
        .format(EXCEPTION_LOG_MSG, e.getClass().getSimpleName(), e.getMessage());
    log.error(message, e);
  }

  private String getMessage(final ObjectError objectError) {
    final String code = String.valueOf(objectError.getDefaultMessage());
    final Object[] args = objectError.getArguments();
    return messageSource.getMessage(code, args, code, Locale.getDefault());
  }

}
