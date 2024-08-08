package com.ufpi.backend.model.dto.viagem;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import com.ufpi.backend.model.dto.localidade.LocalidadeCreateDTO;
import com.ufpi.backend.model.entity.Motorista;
import com.ufpi.backend.model.entity.Veiculo;
import com.ufpi.backend.model.entity.Viagem;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

  @NotNull(message = "propriedade username não pode ser nula.")
  private String motoristaUsername;

  @Min(value = 1, message = "propriedade quantidade de vagas deve ser maior que 0.")
  @Max(value = 5, message = "propriedade quantidade de vagas deve ser menor que 5.")
  @NotNull(message = "propriedade quantidade de vagas não pode ser nula.")
  private Integer quantidadeVagas;

  @NotBlank(message = "propriedade veiculo não pode ser nula.")
  private String veiculoPlaca;

  @Future(message = "propriedade previsão de saída deve ser uma data futura.")
  @NotNull(message = "propriedade previsão de saída não pode ser nula.")
  private LocalDateTime previsaoSaida;

  @Future(message = "propriedade previsão de saída deve ser uma data futura.")
  @NotNull(message = "propriedade previsão de chegada não pode ser nula.")
  private LocalDateTime previsaoChegada;

  @NotNull(message = "propriedade origem não pode ser nula.")
  private LocalidadeCreateDTO origem;

  @NotNull(message = "propriedade destino não pode ser nula.")
  private LocalidadeCreateDTO destino;

  public static Viagem toEntity(ViagemCreateDTO viagemDTO) {
    return Viagem.builder()
        .motorista(Motorista.builder().username(viagemDTO.getMotoristaUsername()).build())
        .veiculo(Veiculo.builder().placa(viagemDTO.getVeiculoPlaca()).build())
        .destino(LocalidadeCreateDTO.toEntity(viagemDTO.getDestino()))
        .origem(LocalidadeCreateDTO.toEntity(viagemDTO.getOrigem()))
        .previsaoChegada(viagemDTO.getPrevisaoChegada())
        .previsaoSaida(viagemDTO.getPrevisaoSaida())
        .quantidadeVagas(viagemDTO.getQuantidadeVagas())
        .build();
  }

}
