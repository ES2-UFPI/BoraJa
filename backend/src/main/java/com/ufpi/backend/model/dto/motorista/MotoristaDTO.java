package com.ufpi.backend.model.dto.motorista;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.ufpi.backend.annotation.ValidCPF;
import com.ufpi.backend.model.entity.Motorista;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MotoristaDTO implements Serializable {

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

  private String email;

  private LocalDateTime dataCadastro;

  private LocalDateTime dataAtualizacao;

  private Float avaliacao;

  private Integer corridasAvaliadas;

  private Integer corridasTotais;

  public static Motorista toEntity(MotoristaDTO motoristaDTO) {
    return Motorista.builder()
        .foto(motoristaDTO.getFoto() == null ? "" : motoristaDTO.getFoto())
        .cpf(motoristaDTO.getCpf())
        .nome(motoristaDTO.getNome())
        .dataNascimento(motoristaDTO.getDataNascimento())
        .dataCadastro(motoristaDTO.getDataCadastro() == null ? LocalDateTime.now() : motoristaDTO.getDataCadastro())
        .avaliacao(motoristaDTO.getAvaliacao() == null ? 0 : motoristaDTO.getAvaliacao())
        .corridasAvaliadas(motoristaDTO.getCorridasAvaliadas() == null ? 0 : motoristaDTO.getCorridasAvaliadas())
        .corridasTotais(motoristaDTO.getCorridasTotais() == null ? 0 : motoristaDTO.getCorridasTotais())
        .build();
  }

  /*  */
  public static MotoristaDTO fromEntity(Motorista motorista) {
    return MotoristaDTO.builder()
        .id(motorista.getId())
        .foto(motorista.getFoto() == null ? "" : motorista.getFoto())
        .cpf(motorista.getCpf())
        .nome(motorista.getNome())
        .dataNascimento(motorista.getDataNascimento())
        .email(motorista.getEmail())
        .dataCadastro(motorista.getDataCadastro())
        .dataAtualizacao(motorista.getDataAtualizacao())
        .avaliacao(motorista.getAvaliacao())
        .build();
  }

}