package com.ufpi.backend.utils;

public class StringUtils {

  public Boolean isUsernameValido(String username) {
    return username.matches("^[^\\s].*[^\\s]$");
  }

}
