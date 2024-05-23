package com.ufpi.backend.model.dto.viagem;

import java.io.Serializable;
import java.util.Objects;

import com.ufpi.backend.model.entity.Localidade;
import com.ufpi.backend.model.entity.Passageiro;
import com.ufpi.backend.model.entity.Viagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViagemUpdateDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  private Passageiro passageiro;

  private Localidade origem;

  private Localidade destino;

  public static ViagemUpdateDTO fromEntity(Viagem viagem) {
    if (Objects.isNull(viagem)) {
      return null;
    }

    return ViagemUpdateDTO.builder()
        .passageiro(viagem.getPassageiro())
        .origem(viagem.getOrigem())
        .destino(viagem.getDestino())
        .build();
  }

  public Viagem toEntity() {
    return Viagem.builder()
        .origem(origem)
        .destino(destino)
        .passageiro(passageiro)
        .build();
  }

  public static Viagem mapViagemUpdate(ViagemDTO viagemExistente,
      ViagemUpdateDTO viagemUpdateDTO) {
    return Viagem.builder()
        .passageiro(viagemUpdateDTO.getPassageiro() == null ? viagemExistente.getPassageiro()
            : viagemUpdateDTO.getPassageiro())
        .origem(viagemUpdateDTO.getOrigem() == null ? viagemExistente.getOrigem()
            : viagemUpdateDTO.getOrigem())
        .destino(viagemUpdateDTO.getDestino() == null ? viagemExistente.getDestino()
            : viagemUpdateDTO.getDestino())
        .build();
  }
}