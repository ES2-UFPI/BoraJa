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

  @OneToOne
  @JoinColumn(name = "motorista_id", nullable = false)
  private Motorista motorista;

  @OneToOne
  @JoinColumn(name = "passageiro_id", nullable = false)
  private Passageiro passageiro;

  @OneToOne
  @JoinColumn(name = "veiculo_placa", nullable = false)
  private Veiculo veiculo;

  @Column(nullable = false)
  private Localidade origem;

  @Column(nullable = false)
  private Localidade destino;

  @Column(nullable = true)
  private LocalDateTime horaEmbarque;

  @Column(nullable = true)
  private LocalDateTime horaDesembarque;

  @Column(nullable = false)
  private Boolean finalizada;

  @CreationTimestamp
  @Column(nullable = false)
  private LocalDateTime dataCadastro;

  @UpdateTimestamp
  @Column(nullable = true)
  private LocalDateTime dataAtualizacao;

  public void finalizarViagem() {
    this.horaDesembarque = LocalDateTime.now();
    this.finalizada = true;
    this.dataAtualizacao = LocalDateTime.now();
  }

  public void cancelarViagem() {
    this.finalizada = true;
    this.dataAtualizacao = LocalDateTime.now();
  }

  public void iniciarViagem() {
    this.horaEmbarque = LocalDateTime.now();
    this.finalizada = false;
    this.dataAtualizacao = LocalDateTime.now();
  }
}
