package com.ufpi.backend.model.dto.veiculo;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.ufpi.backend.model.entity.Motorista;
import com.ufpi.backend.model.entity.Veiculo;
import com.ufpi.backend.model.enums.TipoVeiculo;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VeiculoDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private String placa;

  @NotBlank(message = "propriedade foto não pode ser nula ou string vazia.")
  private String foto;

  private String marca;

  private String modelo;

  private String cor;

  private Integer ano;

  private TipoVeiculo tipo;

  private Motorista proprietario;

  private Boolean ativo;

  @CreationTimestamp
  private LocalDateTime dataCadastro;

  @UpdateTimestamp
  private LocalDateTime dataAtualizacao;

  public static Veiculo toEntity(VeiculoDTO veiculoDTO) {
    return Veiculo.builder()
        .placa(veiculoDTO.getPlaca())
        .foto(veiculoDTO.getFoto())
        .marca(veiculoDTO.getMarca())
        .modelo(veiculoDTO.getModelo())
        .cor(veiculoDTO.getCor())
        .ano(veiculoDTO.getAno())
        .tipo(veiculoDTO.getTipo())
        .proprietario(veiculoDTO.getProprietario())
        .ativo(veiculoDTO.getAtivo())
        .dataCadastro(veiculoDTO.getDataCadastro())
        .dataAtualizacao(veiculoDTO.getDataAtualizacao())
        .build();
  }

  public static VeiculoDTO fromEntity(Veiculo veiculo) {
    return VeiculoDTO.builder()
        .placa(veiculo.getPlaca())
        .foto(veiculo.getFoto())
        .marca(veiculo.getMarca())
        .modelo(veiculo.getModelo())
        .cor(veiculo.getCor())
        .ano(veiculo.getAno())
        .tipo(veiculo.getTipo())
        .proprietario(veiculo.getProprietario())
        .ativo(veiculo.getAtivo())
        .dataCadastro(veiculo.getDataCadastro())
        .dataAtualizacao(veiculo.getDataAtualizacao())
        .build();
  }

}