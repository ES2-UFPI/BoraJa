package com.ufpi.backend.model.dto.viagem;

import java.io.Serializable;
import java.util.UUID;

import com.ufpi.backend.model.entity.Localidade;
import com.ufpi.backend.model.entity.Motorista;
import com.ufpi.backend.model.entity.Passageiro;
import com.ufpi.backend.model.entity.Veiculo;
import com.ufpi.backend.model.entity.Viagem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViagemCreateDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @NotNull(message = "propriedade motorista não pode ser nula.")
  private UUID motoristaId;

  @NotNull(message = "propriedade passageiro não pode ser nula.")
  private UUID passageiroId;

  @NotBlank(message = "propriedade veiculo não pode ser nula.")
  private String veiculoPlaca;

  @NotNull(message = "propriedade origem não pode ser nula.")
  private Localidade origem;

  @NotNull(message = "propriedade destino não pode ser nula.")
  private Localidade destino;

  public static Viagem toEntity(ViagemCreateDTO viagemDTO) {
    return Viagem.builder()
        .motorista(Motorista.builder().id(viagemDTO.getMotoristaId()).build())
        .passageiro(Passageiro.builder().id(viagemDTO.getPassageiroId()).build())
        .veiculo(Veiculo.builder().placa(viagemDTO.getVeiculoPlaca()).build())
        .destino(viagemDTO.getDestino())
        .origem(viagemDTO.getOrigem())
        .build();
  }
}
