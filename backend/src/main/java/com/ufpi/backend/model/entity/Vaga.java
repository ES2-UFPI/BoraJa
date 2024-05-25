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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "vaga")
public class Vaga implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(unique = true, nullable = false)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "viagem_id", nullable = false)
  private Viagem viagem;

  @ManyToOne
  @JoinColumn(name = "passageiro_id", nullable = false)
  private Passageiro passageiro;

  @CreationTimestamp
  @Column(nullable = false)
  private LocalDateTime dataCadastro;

  @UpdateTimestamp
  @Column(nullable = true)
  private LocalDateTime dataAtualizacao;

}
