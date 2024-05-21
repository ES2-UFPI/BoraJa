package com.ufpi.backend.model.dto.passageiro;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import com.ufpi.backend.annotation.ValidCPF;
import com.ufpi.backend.model.entity.Passageiro;

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
public class PassageiroUpdateDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  @NotNull
  private UUID id;

  private String nome;

  @ValidCPF
  private String cpf;

  private String foto;

  @Past(message = "Data de nascimento deve estar no passado!")
  private LocalDate dataNascimento;

  public static PassageiroUpdateDTO fromEntity(Passageiro passageiro) {
    if (Objects.isNull(passageiro)) {
      return null;
    }

    return PassageiroUpdateDTO.builder()
        .id(passageiro.getId())
        .nome(passageiro.getNome())
        .cpf(passageiro.getCpf())
        .foto(passageiro.getFoto())
        .dataNascimento(passageiro.getDataNascimento())
        .build();
  }

  public Passageiro toEntity() {
    return Passageiro.builder()
        .id(id)
        .nome(nome)
        .foto(foto)
        .cpf(cpf)
        .dataNascimento(dataNascimento)
        .build();
  }

  public static Passageiro mapPassageiroUpdate(PassageiroDTO passageiroExistente,
      PassageiroUpdateDTO passageiroUpdate) {
    return Passageiro.builder()
        .id(passageiroExistente.getId())
        .foto(passageiroUpdate.getFoto())
        .nome(passageiroUpdate.getNome() == null ? passageiroExistente.getNome() : passageiroUpdate.getNome())
        .cpf(passageiroExistente.getCpf())
        .dataNascimento(passageiroUpdate.getDataNascimento())
        .dataCadastro(passageiroExistente.getDataCadastro())
        .build();
  }
}