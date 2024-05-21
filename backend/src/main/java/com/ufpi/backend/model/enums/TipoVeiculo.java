package com.ufpi.backend.model.enums;

public enum TipoVeiculo {
  CARRO("Carro"),
  MOTO("Moto");

  private String descricao;

  private TipoVeiculo(String descricao) {
    this.descricao = descricao;
  }

  public String getDescricao() {
    return descricao;
  }
}
