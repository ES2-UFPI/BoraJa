package com.ufpi.backend.model.dto.veiculo;

import java.io.Serializable;

import com.ufpi.backend.model.entity.Veiculo;
import com.ufpi.backend.model.enums.TipoVeiculo;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VeiculoCreateDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @NotBlank(message = "propriedade placa não pode ser nula ou string vazia.")
  private String placa;

  @NotBlank(message = "propriedade foto não pode ser nula ou string vazia.")
  private String foto;

  @NotBlank(message = "propriedade marca não pode ser nula ou string vazia.")
  private String marca;

  @NotBlank(message = "propriedade modelo não pode ser nula ou string vazia.")
  private String modelo;

  @NotBlank(message = "propriedade cor não pode ser nula ou string vazia.")
  private String cor;

  @NotBlank(message = "propriedade ano não pode ser nula ou string vazia.")
  private Integer ano;

  @NotBlank(message = "propriedade tipo não pode ser nula ou string vazia.")
  private TipoVeiculo tipo;

  @NotBlank(message = "propriedade cpf do proprietário não pode ser nula ou string vazia.")
  private String cpfProprietario;

  public static Veiculo toEntity(VeiculoCreateDTO veiculoCreateDTO) {
    return Veiculo.builder()
        .foto(veiculoCreateDTO.getFoto())
        .placa(veiculoCreateDTO.getPlaca())
        .marca(veiculoCreateDTO.getMarca())
        .modelo(veiculoCreateDTO.getModelo())
        .cor(veiculoCreateDTO.getCor())
        .ano(veiculoCreateDTO.getAno())
        .tipo(veiculoCreateDTO.getTipo())
        .build();
  }

}
