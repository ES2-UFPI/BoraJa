package com.ufpi.backend.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ufpi.backend.config.response.ResponseModel;
import com.ufpi.backend.exceptions.InvalidDataError;
import com.ufpi.backend.exceptions.NotFoundError;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ExceptionDefaultHandler {
  private static final String ERRO_VALIDACAO_MENSAGEM = "Os campos correspondentes não atendem as regras de validação";

  @ExceptionHandler(Exception.class)
  @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseModel<Object> handleGenericException(Exception e) {
    ResponseModel<Object> response = new ResponseModel<>();
    response.setMessage(e.getMessage());

    log.error(e.getMessage());

    return response;
  }

  @ExceptionHandler(NotFoundError.class)
  @ResponseStatus(code = HttpStatus.NOT_FOUND)
  public ResponseModel<Object> handleRecursoNaoEncontradoException(NotFoundError e) {
    ResponseModel<Object> response = new ResponseModel<>();
    response.setMessage(e.getMessage());

    log.error(e.getMessage());

    return response;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  public ResponseModel<Object> handleValidationExceptions(MethodArgumentNotValidException e) {
    Map<Object, Object> errors = new HashMap<>();
    e.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    ResponseModel<Object> response = new ResponseModel<>();
    response.setMessage(ERRO_VALIDACAO_MENSAGEM);
    response.setErrors(errors);

    log.error(e.getMessage());

    return response;
  }

  @ExceptionHandler(InvalidDataError.class)
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  public ResponseModel<Object> handleValidationExceptions(InvalidDataError e) {
    Map<Object, Object> errors = new HashMap<>();
    {
      String fieldName = e.getField();
      String errorMessage = e.getMessage();
      errors.put(fieldName, errorMessage);
    }
    ;

    ResponseModel<Object> response = new ResponseModel<>();
    response.setMessage(ERRO_VALIDACAO_MENSAGEM);
    response.setErrors(errors);

    log.error(e.getMessage());

    return response;
  }
}
