package com.ufpi.backend.model.dto.motorista;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import com.ufpi.backend.model.entity.Motorista;
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

  private String nome;

  private String email;

  private String foto;

  @Past(message = "Data de nascimento deve estar no passado!")
  private LocalDate dataNascimento;

  public static MotoristaUpdateDTO fromEntity(MotoristaDTO motorista) {
    if (Objects.isNull(motorista)) {
      return null;
    }

    return MotoristaUpdateDTO.builder()
        .nome(motorista.getNome())
        .foto(motorista.getFoto())
        .dataNascimento(motorista.getDataNascimento())
        .email(motorista.getEmail())
        .build();
  }

  public Motorista toEntity() {
    return Motorista.builder()
        .nome(nome)
        .foto(foto)
        .dataNascimento(dataNascimento)
        .email(email)
        .build();
  }

  public static Motorista mapMotoristaUpdate(MotoristaDTO motoristaExistente,
      MotoristaUpdateDTO motoristaUpdate) {
    return Motorista.builder()
        .username(motoristaExistente.getUsername())
        .foto(motoristaUpdate.getFoto())
        .nome(motoristaUpdate.getNome() == null ? motoristaExistente.getNome() : motoristaUpdate.getNome())
        .cpf(motoristaExistente.getCpf())
        .email(motoristaUpdate.getEmail() == null ? motoristaExistente.getEmail() : motoristaUpdate.getEmail())
        .dataNascimento(motoristaUpdate.getDataNascimento())
        .dataCadastro(motoristaExistente.getDataCadastro())
        .build();
  }
}