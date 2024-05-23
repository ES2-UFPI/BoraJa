package com.ufpi.backend.model.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.validator.constraints.br.CPF;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "motorista")
public class Motorista implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(unique = true)
  private UUID id;

  @Column(columnDefinition = "text", nullable = false)
  private String foto;

  @CPF
  @Column(nullable = false, unique = true, updatable = false)
  private String cpf;

  @Column(nullable = false)
  private String nome;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private LocalDate dataNascimento;

  @Column(nullable = false)
  private LocalDateTime dataCadastro;

  @Column(nullable = true)
  private LocalDateTime dataAtualizacao;

  @Min(0)
  @Max(5)
  @Column(nullable = true)
  private Float avaliacao;

  private Integer corridasTotais;

  private Integer corridasAvaliadas;

  public void incrementarCorridasAvaliadas() {
    corridasAvaliadas++;
  }

  public void incrementarCorridasTotais() {
    corridasTotais++;
  }

}