package com.ufpi.backend.model.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.ufpi.backend.model.enums.TipoVeiculo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "veiculo")
public class Veiculo {
  @Id
  private String placa;

  @Column(nullable = false)
  private String marca;

  @Column(nullable = false)
  private String modelo;

  @Column(nullable = false)
  private String cor;

  @Column(nullable = false)
  private Integer ano;

  @Column(columnDefinition = "text", nullable = false)
  private String foto;

  @Column(nullable = false)
  private TipoVeiculo tipo;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Motorista proprietario;

  @Column(nullable = false)
  private Boolean ativo;

  @CreationTimestamp
  @Column(nullable = false)
  private LocalDateTime dataCadastro;

  @UpdateTimestamp
  @Column(nullable = false)
  private LocalDateTime dataAtualizacao;
}
