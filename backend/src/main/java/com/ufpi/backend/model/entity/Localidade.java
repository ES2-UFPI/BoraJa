package com.ufpi.backend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Localidade {
  private Double latitude;
  private Double longitude;
  private String nome;
}
