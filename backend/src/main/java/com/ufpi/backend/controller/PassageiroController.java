package com.ufpi.backend.controller;

import java.util.regex.Pattern;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufpi.backend.config.response.ResponseModel;
import com.ufpi.backend.exceptions.InvalidDataError;
import com.ufpi.backend.model.dto.passageiro.PassageiroCreateDTO;
import com.ufpi.backend.model.dto.passageiro.PassageiroDTO;
import com.ufpi.backend.model.dto.passageiro.PassageiroUpdateDTO;
import com.ufpi.backend.model.entity.Passageiro;
import com.ufpi.backend.service.PassageiroService;
import com.ufpi.backend.utils.StringUtils;
import com.ufpi.backend.validator.CpfValidator;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/passageiro")
@Tag(name = "Passageiro")
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class PassageiroController {

  private final PassageiroService passageiroService;
  private final String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

  @PostMapping
  public ResponseEntity<ResponseModel<PassageiroDTO>> cadastrar(
      @Valid @RequestBody PassageiroCreateDTO passageiroCreateDTO) {
    ResponseModel<PassageiroDTO> resposta = new ResponseModel<>();
    if (!CpfValidator.isCpf(passageiroCreateDTO.getCpf())) {
      throw new InvalidDataError("cpf", "CPF inválido!");
    }
    if (!Pattern.compile(emailRegex).matcher(passageiroCreateDTO.getEmail()).find()) {
      throw new InvalidDataError("email", "Email inválido!");
    }
    if (!StringUtils.isUsernameValido(passageiroCreateDTO.getUsername())) {
      resposta.setMessage("Username inválido! O username não pode conter espaços em branco.");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
    }
    if (passageiroService.consultarPassageiroPeloCPF(passageiroCreateDTO.getCpf()) != null) {
      resposta.setMessage("Já existe um passageiro cadastrado com o cpf informado!");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
    }

    Passageiro passageiro = passageiroService.insert(passageiroCreateDTO);
    resposta.setData(PassageiroDTO.fromEntity(passageiro));
    return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
  }

  @PutMapping("/{username}")
  public ResponseEntity<ResponseModel<Passageiro>> atualizar(@PathVariable String username,
      @Valid @RequestBody PassageiroUpdateDTO passageiroUpdateDTO) {
    ResponseModel<Passageiro> resposta = new ResponseModel<>();
    if (passageiroUpdateDTO.getEmail() != null || !passageiroUpdateDTO.getEmail().isEmpty()) {
      if (!Pattern.compile(emailRegex).matcher(passageiroUpdateDTO.getEmail()).find()) {
        throw new InvalidDataError("email", "Email inválido!");
      }
    }
    Passageiro passageiroUpdate = PassageiroUpdateDTO.mapPassageiroUpdate(passageiroService.consultarPorId(username),
        passageiroUpdateDTO);
    if (passageiroUpdate == null) {
      resposta.setMessage("O passageiro com ID informado não existe!");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
    }

    resposta.setData(passageiroService.atualizar(username, passageiroUpdate));
    resposta.setMessage("Operação realizada com sucesso!");
    return ResponseEntity.status(HttpStatus.OK).body(resposta);
  }

  @GetMapping
  public ResponseEntity<ResponseModel<PassageiroDTO>> consultaPaginada(
      @PageableDefault(size = 10, sort = { "dataCadastro" }, direction = Sort.Direction.DESC) Pageable paginacao) {
    var resultado = passageiroService.consultar(paginacao);
    ResponseModel<PassageiroDTO> resposta = new ResponseModel<>();
    resposta.setList(resultado.getContent().stream().map(PassageiroDTO::fromEntity).toList());
    resposta.setTotalElements(resultado.getTotalElements());
    resposta.setTotalPages(resultado.getTotalPages());
    return ResponseEntity.status(HttpStatus.OK).body(resposta);
  }

  @GetMapping("/{username}")
  public ResponseEntity<ResponseModel<PassageiroDTO>> consultaPorId(@PathVariable String username) {
    var resultado = passageiroService.consultarPorId(username);
    ResponseModel<PassageiroDTO> resposta = new ResponseModel<>();
    resposta.setData(resultado);
    return ResponseEntity.status(HttpStatus.OK).body(resposta);
  }

  @DeleteMapping("/{username}")
  public ResponseEntity<Void> delete(@PathVariable String username) {
    var resultado = passageiroService.consultarPorId(username);
    passageiroService.excluir(username);
    ResponseModel<PassageiroDTO> resposta = new ResponseModel<>();
    resposta.setData(resultado);
    resposta.setMessage("Operação realizada com sucesso!");
    return ResponseEntity.status(HttpStatus.OK).build();
  }

}
