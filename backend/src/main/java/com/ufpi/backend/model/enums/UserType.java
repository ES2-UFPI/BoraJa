package com.ufpi.backend.model.enums;

public enum UserType {
  MOTORISTA("Motorista"),
  PASSAGEIRO("Passageiro");

  private String descricao;

  private UserType(String descricao) {
    this.descricao = descricao;
  }

  public String getDescricao() {
    return descricao;
  }
}
