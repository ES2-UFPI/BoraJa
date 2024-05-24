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
public class LocalidadeCreateDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  @NotNull
  private Double latitude;

  @NotNull
  private Double longitude;

  private String nome;

  public static Localidade toEntity(LocalidadeCreateDTO LOcalidadeCreateDTO) {
    return Localidade.builder()
        .latitude(LOcalidadeCreateDTO.getLatitude())
        .longitude(LOcalidadeCreateDTO.getLongitude())
        .nome(LOcalidadeCreateDTO.getNome())
        .build();
  }
}
