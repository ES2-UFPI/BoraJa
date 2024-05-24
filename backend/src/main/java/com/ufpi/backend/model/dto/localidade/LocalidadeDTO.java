package com.ufpi.backend.model.dto.localidade;

import java.io.Serializable;
import java.util.UUID;

import com.ufpi.backend.model.entity.Localidade;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocalidadeDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  private UUID id;

  @NotNull
  private Double latitude;

  @NotNull
  private Double longitude;

  private String nome;

  public static Localidade toEntity(LocalidadeDTO localidadeDTO) {
    return Localidade.builder()
        .latitude(localidadeDTO.getLatitude())
        .longitude(localidadeDTO.getLongitude())
        .nome(localidadeDTO.getNome())
        .build();
  }
}
