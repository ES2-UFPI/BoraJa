package com.ufpi.backend.model.dto.motorista;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import com.ufpi.backend.annotation.ValidCPF;
import com.ufpi.backend.model.entity.Motorista;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MotoristaUpdateDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  @NotNull
  private UUID id;

  private String nome;

  @ValidCPF
  private String cpf;

  private String foto;

  @Past(message = "Data de nascimento deve estar no passado!")
  private LocalDate dataNascimento;

  public static MotoristaUpdateDTO fromEntity(MotoristaDTO motorista) {
    if (Objects.isNull(motorista)) {
      return null;
    }

    return MotoristaUpdateDTO.builder()
        .id(motorista.getId())
        .nome(motorista.getNome())
        .cpf(motorista.getCpf())
        .foto(motorista.getFoto())
        .dataNascimento(motorista.getDataNascimento())
        .build();
  }

  public Motorista toEntity() {
    return Motorista.builder()
        .id(id)
        .nome(nome)
        .foto(foto)
        .cpf(cpf)
        .dataNascimento(dataNascimento)
        .build();
  }

  public static Motorista mapMotoristaUpdate(MotoristaDTO motoristaExistente,
      MotoristaUpdateDTO motoristaUpdate) {
    return Motorista.builder()
        .id(motoristaExistente.getId())
        .foto(motoristaUpdate.getFoto())
        .nome(motoristaUpdate.getNome() == null ? motoristaExistente.getNome() : motoristaUpdate.getNome())
        .cpf(motoristaExistente.getCpf())
        .dataNascimento(motoristaUpdate.getDataNascimento())
        .dataCadastro(motoristaExistente.getDataCadastro())
        .build();
  }
}