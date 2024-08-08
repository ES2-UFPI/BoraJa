package com.ufpi.backend.model.filter;

import java.time.LocalDateTime;
import java.util.UUID;

import com.ufpi.backend.model.dto.localidade.LocalidadeDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FiltroViagemDTO {
  private UUID id;
  private String motoristaUsername;
  private String veiculoPlaca;
  private LocalidadeDTO origem;
  private LocalidadeDTO destino;
  private LocalDateTime previsaoSaida;
  private LocalDateTime previsaoChegada;
  private Integer page;
  private Integer size;
}
