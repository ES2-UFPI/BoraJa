package com.ufpi.backend.model.dto.veiculo;

import java.io.Serializable;
import java.util.Objects;
import com.ufpi.backend.model.entity.Veiculo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VeiculoUpdateDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  @NotNull
  private String placa;

  private String foto;

  public static VeiculoUpdateDTO fromEntity(VeiculoDTO veiculoDTO) {
    if (Objects.isNull(veiculoDTO)) {
      return null;
    }

    return VeiculoUpdateDTO.builder()
        .placa(veiculoDTO.getPlaca())
        .build();
  }

  public Veiculo toEntity() {
    return Veiculo.builder()
        .placa(placa)
        .foto(foto)
        .build();
  }

  public static Veiculo mapVeiculoUpdate(VeiculoDTO veiculoExistente,
      VeiculoUpdateDTO veiculoUpdate) {
    return Veiculo.builder()
        .placa(veiculoExistente.getPlaca())
        .foto(veiculoUpdate.getFoto())
        .build();
  }
}