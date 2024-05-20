package com.ufpi.backend.model.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.ufpi.backend.annotation.ValidCPF;
import com.ufpi.backend.model.entity.User;
import com.ufpi.backend.model.enums.UserType;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {

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

  private UserType tipo;

  public static User toEntity(UserDTO userDTO) {
    return User.builder()
        .foto(userDTO.getFoto() == null ? "" : userDTO.getFoto())
        .cpf(userDTO.getCpf())
        .nome(userDTO.getNome())
        .dataNascimento(userDTO.getDataNascimento())
        .dataCadastro(userDTO.getDataCadastro() == null ? LocalDateTime.now() : userDTO.getDataCadastro())
        .tipo(userDTO.getTipo())
        .build();
  }

  /*  */
  public static UserDTO fromEntity(User user) {
    return UserDTO.builder()
        .id(user.getId())
        .foto(user.getFoto() == null ? "" : user.getFoto())
        .cpf(user.getCpf())
        .nome(user.getNome())
        .dataNascimento(user.getDataNascimento())
        .tipo(user.getTipo())
        .build();
  }

}