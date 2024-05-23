package com.ufpi.backend.model.dto.viagem;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import com.ufpi.backend.model.entity.Localidade;
import com.ufpi.backend.model.entity.Motorista;
import com.ufpi.backend.model.entity.Passageiro;
import com.ufpi.backend.model.entity.Veiculo;
import com.ufpi.backend.model.entity.Viagem;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViagemDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private UUID id;

  @NotNull(message = "propriedade motorista não pode ser nula.")
  private Motorista motorista;

  @NotNull(message = "propriedade passageiro não pode ser nula.")
  private Passageiro passageiro;

  @NotNull(message = "propriedade veiculo não pode ser nula.")
  private Veiculo veiculo;

  @NotNull(message = "propriedade origem não pode ser nula.")
  private Localidade origem;

  @NotNull(message = "propriedade destino não pode ser nula.")
  private Localidade destino;

  private LocalDateTime horaEmbarque;

  private LocalDateTime horaDesembarque;

  private Boolean finalizada;

  private LocalDateTime dataCadastro;

  private LocalDateTime dataAtualizacao;

  public static Viagem toEntity(ViagemDTO viagemDTO) {
    return Viagem.builder()
        .id(viagemDTO.getId())
        .motorista(viagemDTO.getMotorista())
        .passageiro(viagemDTO.getPassageiro())
        .origem(viagemDTO.getOrigem())
        .destino(viagemDTO.getDestino())
        .horaEmbarque(viagemDTO.getHoraEmbarque())
        .horaDesembarque(viagemDTO.getHoraDesembarque())
        .finalizada(viagemDTO.getFinalizada() == null ? false : viagemDTO.getFinalizada())
        .dataCadastro(viagemDTO.getDataCadastro() == null ? LocalDateTime.now() : viagemDTO.getDataCadastro())
        .dataAtualizacao(viagemDTO.getDataAtualizacao())
        .build();
  }

  public static ViagemDTO fromEntity(Viagem viagem) {
    return ViagemDTO.builder()
        .id(viagem.getId())
        .motorista(viagem.getMotorista())
        .passageiro(viagem.getPassageiro())
        .veiculo(viagem.getVeiculo())
        .origem(viagem.getOrigem())
        .destino(viagem.getDestino())
        .horaEmbarque(viagem.getHoraEmbarque())
        .horaDesembarque(viagem.getHoraDesembarque())
        .finalizada(viagem.getFinalizada())
        .dataCadastro(viagem.getDataCadastro())
        .dataAtualizacao(viagem.getDataAtualizacao())
        .build();
  }

}