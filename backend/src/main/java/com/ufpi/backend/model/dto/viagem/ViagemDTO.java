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

  @NotNull(message = "propriedade veiculo não pode ser nula.")
  private Veiculo veiculo;

  @NotNull(message = "propriedade origem não pode ser nula.")
  private Localidade origem;

  @NotNull(message = "propriedade destino não pode ser nula.")
  private Localidade destino;

  @NotNull(message = "propriedade previsão de saída não pode ser nula.")
  private LocalDateTime previsaoSaida;

  @NotNull(message = "propriedade previsão de chegada não pode ser nula.")
  private LocalDateTime previsaoChegada;

  private LocalDateTime horaSaida;

  private LocalDateTime horaChegada;

  private Boolean finalizada;

  private LocalDateTime dataCadastro;

  private LocalDateTime dataAtualizacao;

  public static Viagem toEntity(ViagemDTO viagemDTO) {
    return Viagem.builder()
        .id(viagemDTO.getId())
        .motorista(viagemDTO.getMotorista())
        .origem(viagemDTO.getOrigem())
        .destino(viagemDTO.getDestino())
        .previsaoSaida(viagemDTO.getPrevisaoSaida())
        .previsaoChegada(viagemDTO.getPrevisaoChegada())
        .horaSaida(viagemDTO.getHoraSaida())
        .horaChegada(viagemDTO.getHoraChegada())
        .finalizada(viagemDTO.getFinalizada() == null ? false : viagemDTO.getFinalizada())
        .dataCadastro(viagemDTO.getDataCadastro() == null ? LocalDateTime.now() : viagemDTO.getDataCadastro())
        .dataAtualizacao(viagemDTO.getDataAtualizacao())
        .build();
  }

  public static ViagemDTO fromEntity(Viagem viagem) {
    return ViagemDTO.builder()
        .id(viagem.getId())
        .motorista(viagem.getMotorista())
        .veiculo(viagem.getVeiculo())
        .origem(viagem.getOrigem())
        .destino(viagem.getDestino())
        .previsaoSaida(viagem.getPrevisaoSaida())
        .previsaoChegada(viagem.getPrevisaoChegada())
        .horaSaida(viagem.getHoraSaida())
        .horaChegada(viagem.getHoraChegada())
        .finalizada(viagem.getFinalizada())
        .dataCadastro(viagem.getDataCadastro())
        .dataAtualizacao(viagem.getDataAtualizacao())
        .build();
  }

}