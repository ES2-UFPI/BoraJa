package com.ufpi.backend.model.dto;

import java.io.Serializable;
import java.time.LocalDate;
import com.ufpi.backend.annotation.ValidCPF;
import com.ufpi.backend.model.entity.Pessoa;
import com.ufpi.backend.model.enums.TipoPessoa;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PessoaCreateDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private String foto;

  @NotBlank(message = "propriedade cpf não pode ser nula ou string vazia.")
  @ValidCPF
  private String cpf;

  @NotBlank(message = "propriedade nome não pode ser nula ou string vazia.")
  private String nome;

  @NotBlank(message = "propriedade data de nascimento não pode ser nula ou string vazia.")
  private LocalDate dataNascimento;

  @NotBlank(message = "propriedade tipo do usuário não pode ser nula ou string vazia.")
  private TipoPessoa tipo;

  public static Pessoa toEntity(PessoaCreateDTO userDTO) {
    return Pessoa.builder()
        .foto(userDTO.getFoto() == null ? "" : userDTO.getFoto())
        .cpf(userDTO.getCpf())
        .nome(userDTO.getNome())
        .dataNascimento(userDTO.getDataNascimento())
        .tipo(userDTO.getTipo())
        .build();
  }

}
