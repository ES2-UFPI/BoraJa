package com.ufpi.backend.exceptions;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class InvalidDataError extends RuntimeException {

  private static final long serialVersionUID = 1L;

  @NonNull
  private final String field;

  @NonNull
  private final String message;

}
