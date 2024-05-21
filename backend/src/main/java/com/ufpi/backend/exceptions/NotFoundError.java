package com.ufpi.backend.exceptions;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class NotFoundError extends RuntimeException {

  private static final long serialVersionUID = 1L;

  @NonNull
  private final String message;

}
