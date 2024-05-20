package com.ufpi.backend.config.response;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

// import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class ResponseModel<T> {
  private String message;

  private T data = null;

  private Long totalElements;
  private Integer totalPages;
  @Singular("item")
  private List<T> list;

  @Singular("error")
  private Map<Object, Object> errors;
}
