package com.ufpi.backend.model.dto.passageiro;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassageiroInsertDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private UUID id;

}
