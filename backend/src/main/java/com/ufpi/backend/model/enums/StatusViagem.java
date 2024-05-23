package com.ufpi.backend.model.enums;

public enum StatusViagem {
  INICIADA("Iniciada"),
  CANCELADA("Cancelada"),
  FINALIZADA("Finalizada");

  private String descricao;

  private StatusViagem(String descricao) {
    this.descricao = descricao;
  }

  public String getDescricao() {
    return descricao;
  }
}
