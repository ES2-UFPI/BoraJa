package com.ufpi.backend.model.dto.passageiro;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import com.ufpi.backend.model.entity.Passageiro;

import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassageiroUpdateDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  private String nome;

  private String email;

  private String foto;

  @Past(message = "Data de nascimento deve estar no passado!")
  private LocalDate dataNascimento;

  public static PassageiroUpdateDTO fromEntity(Passageiro passageiro) {
    if (Objects.isNull(passageiro)) {
      return null;
    }

    return PassageiroUpdateDTO.builder()
        .nome(passageiro.getNome())
        .email(passageiro.getEmail())
        .foto(passageiro.getFoto())
        .dataNascimento(passageiro.getDataNascimento())
        .build();
  }

  public Passageiro toEntity() {
    return Passageiro.builder()
        .email(email)
        .nome(nome)
        .foto(foto)
        .dataNascimento(dataNascimento)
        .build();
  }

  public static Passageiro mapPassageiroUpdate(PassageiroDTO passageiroExistente,
      PassageiroUpdateDTO passageiroUpdate) {
    return Passageiro.builder()
        .username(passageiroExistente.getUsername())
        .foto(passageiroUpdate.getFoto())
        .nome(passageiroUpdate.getNome() == null ? passageiroExistente.getNome() : passageiroUpdate.getNome())
        .cpf(passageiroExistente.getCpf())
        .email(passageiroUpdate.getEmail() == null ? passageiroExistente.getEmail() : passageiroUpdate.getEmail())
        .dataNascimento(passageiroUpdate.getDataNascimento())
        .dataCadastro(passageiroExistente.getDataCadastro())
        .build();
  }
}