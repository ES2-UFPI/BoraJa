package com.ufpi.backend.model.dto.veiculo;

import java.io.Serializable;
import com.ufpi.backend.model.entity.Veiculo;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VeiculoDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private String placa;

  @NotBlank(message = "propriedade foto não pode ser nula ou string vazia.")
  private String foto;

  public static Veiculo toEntity(VeiculoDTO veiculoDTO) {
    return Veiculo.builder()

        .build();
  }

  public static VeiculoDTO fromEntity(Veiculo veiculo) {
    return VeiculoDTO.builder()

        .build();
  }

}