package com.ufpi.backend.model.enums;

public enum TipoPessoa {
  MOTORISTA("Motorista"),
  PASSAGEIRO("Passageiro");

  private String descricao;

  private TipoPessoa(String descricao) {
    this.descricao = descricao;
  }

  public String getDescricao() {
    return descricao;
  }
}
