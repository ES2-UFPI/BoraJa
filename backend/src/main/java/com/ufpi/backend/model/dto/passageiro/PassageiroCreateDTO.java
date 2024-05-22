package com.ufpi.backend.model.dto.passageiro;

import java.io.Serializable;
import java.time.LocalDate;
import com.ufpi.backend.annotation.ValidCPF;
import com.ufpi.backend.model.entity.Passageiro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PassageiroCreateDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private String foto;

  @NotBlank(message = "propriedade em ail não pode ser nula ou string vazia.")
  private String email;

  @ValidCPF
  @NotBlank(message = "propriedade cpf não pode ser nula ou string vazia.")
  private String cpf;

  @NotBlank(message = "propriedade nome não pode ser nula ou string vazia.")
  private String nome;

  @Past(message = "Data de nascimento deve estar no passado!")
  @NotNull(message = "propriedade data de nascimento não pode ser nula ou string vazia.")
  private LocalDate dataNascimento;

  public static Passageiro toEntity(PassageiroCreateDTO userDTO) {
    return Passageiro.builder()
        .foto(userDTO.getFoto() == null ? "" : userDTO.getFoto())
        .cpf(userDTO.getCpf())
        .nome(userDTO.getNome())
        .email(userDTO.getEmail())
        .dataNascimento(userDTO.getDataNascimento())
        .build();
  }
}
