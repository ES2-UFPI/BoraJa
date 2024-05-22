package com.ufpi.backend.model.dto.veiculo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import com.ufpi.backend.model.entity.Veiculo;
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

  private String foto;

  private String cor;

  private Boolean ativo;

  public static VeiculoUpdateDTO fromEntity(VeiculoDTO veiculoDTO) {
    if (Objects.isNull(veiculoDTO)) {
      return null;
    }

    return VeiculoUpdateDTO.builder()
        .foto(veiculoDTO.getFoto())
        .cor(veiculoDTO.getCor())
        .ativo(veiculoDTO.getAtivo())
        .build();
  }

  public static Veiculo mapVeiculoUpdate(VeiculoDTO veiculoExistente,
      VeiculoUpdateDTO veiculoUpdate) {
    return Veiculo.builder()
        .placa(veiculoExistente.getPlaca())
        .foto(veiculoUpdate.getFoto())
        .marca(veiculoExistente.getMarca())
        .modelo(veiculoExistente.getModelo())
        .cor(veiculoUpdate.getCor())
        .ano(veiculoExistente.getAno())
        .tipo(veiculoExistente.getTipo())
        .proprietario(veiculoExistente.getProprietario())
        .ativo(veiculoUpdate.getAtivo())
        .dataCadastro(veiculoExistente.getDataCadastro())
        .dataAtualizacao(LocalDateTime.now())
        .build();
  }
}