package com.ufpi.backend.model.dto.motorista;

import java.io.Serializable;
import java.time.LocalDate;
import com.ufpi.backend.annotation.ValidCPF;
import com.ufpi.backend.model.entity.Motorista;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MotoristaCreateDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @NotBlank(message = "propriedade foto não pode ser nula ou string vazia.")
  private String foto;

  @NotBlank(message = "propriedade cpf não pode ser nula ou string vazia.")
  @ValidCPF
  private String cpf;

  @NotBlank(message = "propriedade nome não pode ser nula ou string vazia.")
  private String nome;

  @NotBlank(message = "propriedade email não pode ser nula ou string vazia.")
  private String email;

  @Past(message = "Data de nascimento deve estar no passado!")
  @NotBlank(message = "propriedade data de nascimento não pode ser nula ou string vazia.")
  private LocalDate dataNascimento;

  public static Motorista toEntity(MotoristaCreateDTO userDTO) {
    return Motorista.builder()
        .foto(userDTO.getFoto())
        .cpf(userDTO.getCpf())
        .nome(userDTO.getNome())
        .email(userDTO.getEmail())
        .dataNascimento(userDTO.getDataNascimento())
        .build();
  }

}
