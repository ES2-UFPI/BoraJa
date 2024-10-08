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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "passageiro")
public class Passageiro implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(unique = true)
  private String username;

  @Column(columnDefinition = "text")
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

}