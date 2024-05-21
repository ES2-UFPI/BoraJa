package com.ufpi.backend.validator;

import com.ufpi.backend.annotation.ValidCPF;

import jakarta.validation.ConstraintValidatorContext;

public class CpfValidator implements jakarta.validation.ConstraintValidator<ValidCPF, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return value == null || value.isEmpty() || isCpf(value);
  }

  /**
   * Realiza a validação do CPF.
   * 
   * @param cpf número de CPF a ser validado pode ser passado no formado
   *            999.999.999-99 ou 99999999999
   * @return true se o CPF é válido e false se não é válido
   */
  public static boolean isCpf(String cpf) {
    cpf = cpf.replace(".", "");
    cpf = cpf.replace("-", "");

    try {
      Long.parseLong(cpf);
    } catch (NumberFormatException e) {
      return false;
    }

    int d1;
    int d2;
    int digito1;
    int resto;
    int digito2;
    int digitoCPF;
    String nDigResult;

    d1 = d2 = 0;
    digito1 = digito2 = resto = 0;

    for (int nCount = 1; nCount < cpf.length() - 1; nCount++) {
      digitoCPF = Integer.parseInt(cpf.substring(nCount - 1, nCount));

      // multiplique a ultima casa por 2 a seguinte por 3 a seguinte por 4
      // e assim por diante.
      d1 = d1 + (11 - nCount) * digitoCPF;

      // para o segundo digito repita o procedimento incluindo o primeiro
      // digito calculado no passo anterior.
      d2 = d2 + (12 - nCount) * digitoCPF;
    }

    // Primeiro resto da divisão por 11.
    resto = (d1 % 11);

    // Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é 11
    // menos o resultado anterior.
    if (resto < 2)
      digito1 = 0;
    else
      digito1 = 11 - resto;

    d2 += 2 * digito1;

    // Segundo resto da divisão por 11.
    resto = (d2 % 11);

    // Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é 11
    // menos o resultado anterior.
    if (resto < 2)
      digito2 = 0;
    else
      digito2 = 11 - resto;

    // Digito verificador do CPF que está sendo validado.
    String nDigVerific = cpf.substring(cpf.length() - 2, cpf.length());

    // Concatenando o primeiro resto com o segundo.
    nDigResult = String.valueOf(digito1) + String.valueOf(digito2);

    // comparar o digito verificador do cpf com o primeiro resto + o segundo
    // resto.
    return nDigVerific.equals(nDigResult);
  }
}
