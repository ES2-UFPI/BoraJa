package com.ufpi.backend.model.dto.passageiro;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.ufpi.backend.annotation.ValidCPF;
import com.ufpi.backend.model.entity.Passageiro;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassageiroDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private String username;

  private String foto;

  @NotBlank(message = "propriedade cpf não pode ser nula ou string vazia.")
  @ValidCPF
  private String cpf;

  @NotBlank(message = "propriedade nome não pode ser nula ou string vazia.")
  private String nome;

  private String email;

  @NotBlank(message = "propriedade data de nascimento não pode ser nula ou string vazia.")
  private LocalDate dataNascimento;

  private LocalDateTime dataCadastro;

  private LocalDateTime dataAtualizacao;

  public static Passageiro toEntity(PassageiroDTO passageiroDTO) {
    return Passageiro.builder()
        .foto(passageiroDTO.getFoto() == null ? "" : passageiroDTO.getFoto())
        .cpf(passageiroDTO.getCpf())
        .nome(passageiroDTO.getNome())
        .email(passageiroDTO.getEmail())
        .dataNascimento(passageiroDTO.getDataNascimento())
        .dataCadastro(passageiroDTO.getDataCadastro() == null ? LocalDateTime.now() : passageiroDTO.getDataCadastro())
        .dataAtualizacao(passageiroDTO.getDataAtualizacao())
        .build();
  }

  /*  */
  public static PassageiroDTO fromEntity(Passageiro passageiro) {
    return PassageiroDTO.builder()
        .username(passageiro.getUsername())
        .foto(passageiro.getFoto() == null ? "" : passageiro.getFoto())
        .cpf(passageiro.getCpf())
        .nome(passageiro.getNome())
        .email(passageiro.getEmail())
        .dataNascimento(passageiro.getDataNascimento())
        .dataCadastro(passageiro.getDataCadastro())
        .dataAtualizacao(passageiro.getDataAtualizacao())
        .build();
  }

}