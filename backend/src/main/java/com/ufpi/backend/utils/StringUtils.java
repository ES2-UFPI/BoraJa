package com.ufpi.backend.utils;

public class StringUtils {

  public static Boolean isUsernameValido(String username) {
    return username.matches("^[^\\s].*[^\\s]$");
  }

}
