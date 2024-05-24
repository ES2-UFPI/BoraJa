package com.ufpi.backend.model.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "viagem")
public class Viagem implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(unique = true, nullable = false)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "motorista_id", nullable = false)
  private Motorista motorista;

  @ManyToOne
  @JoinColumn(name = "veiculo_placa", nullable = false)
  private Veiculo veiculo;

  @OneToOne
  @JoinColumn(name = "origem_id", nullable = false)
  private Localidade origem;

  @OneToOne
  @JoinColumn(name = "destino_id", nullable = false)
  private Localidade destino;

  @Column(nullable = false)
  private Integer quantidadeVagas;

  @Column(nullable = false)
  private LocalDateTime previsaoSaida;

  @Column(nullable = false)
  private LocalDateTime previsaoChegada;

  @Column(nullable = true)
  private LocalDateTime horaSaida;

  @Column(nullable = true)
  private LocalDateTime horaChegada;

  @Column(nullable = false)
  private Boolean finalizada;

  @CreationTimestamp
  @Column(nullable = false)
  private LocalDateTime dataCadastro;

  @UpdateTimestamp
  @Column(nullable = true)
  private LocalDateTime dataAtualizacao;

  public void finalizarViagem() {
    this.horaChegada = LocalDateTime.now();
    this.finalizada = true;
    this.dataAtualizacao = LocalDateTime.now();
  }

  public void cancelarViagem() {
    this.finalizada = true;
    this.dataAtualizacao = LocalDateTime.now();
  }

  public void iniciarViagem() {
    this.horaSaida = LocalDateTime.now();
    this.finalizada = false;
    this.dataAtualizacao = LocalDateTime.now();
  }
}
