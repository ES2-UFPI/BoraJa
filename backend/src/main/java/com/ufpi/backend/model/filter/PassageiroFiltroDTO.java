package com.ufpi.backend.model.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassageiroFiltroDTO {
  String cpf;
  String nome;
  private Integer page;
  private Integer size;
}
