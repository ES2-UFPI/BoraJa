package com.ufpi.backend.model.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.ufpi.backend.annotation.ValidCPF;
import com.ufpi.backend.model.entity.Pessoa;
import com.ufpi.backend.model.enums.TipoPessoa;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PessoaDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private UUID id;

  private String foto;

  @NotBlank(message = "propriedade cpf não pode ser nula ou string vazia.")
  @ValidCPF
  private String cpf;

  @NotBlank(message = "propriedade nome não pode ser nula ou string vazia.")
  private String nome;

  @NotBlank(message = "propriedade data de nascimento não pode ser nula ou string vazia.")
  private LocalDate dataNascimento;

  private LocalDateTime dataCadastro;

  private TipoPessoa tipo;

  public static Pessoa toEntity(PessoaDTO pessoaDTO) {
    return Pessoa.builder()
        .foto(pessoaDTO.getFoto() == null ? "" : pessoaDTO.getFoto())
        .cpf(pessoaDTO.getCpf())
        .nome(pessoaDTO.getNome())
        .dataNascimento(pessoaDTO.getDataNascimento())
        .dataCadastro(pessoaDTO.getDataCadastro() == null ? LocalDateTime.now() : pessoaDTO.getDataCadastro())
        .tipo(pessoaDTO.getTipo())
        .build();
  }

  /*  */
  public static PessoaDTO fromEntity(Pessoa pessoa) {
    return PessoaDTO.builder()
        .id(pessoa.getId())
        .foto(pessoa.getFoto() == null ? "" : pessoa.getFoto())
        .cpf(pessoa.getCpf())
        .nome(pessoa.getNome())
        .dataNascimento(pessoa.getDataNascimento())
        .tipo(pessoa.getTipo())
        .build();
  }

}