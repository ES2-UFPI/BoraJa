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
import com.ufpi.backend.exceptions.NotFoundError;
import com.ufpi.backend.model.dto.motorista.MotoristaCreateDTO;
import com.ufpi.backend.model.dto.motorista.MotoristaDTO;
import com.ufpi.backend.model.dto.motorista.MotoristaUpdateDTO;
import com.ufpi.backend.model.entity.Motorista;
import com.ufpi.backend.service.MotoristaService;
import com.ufpi.backend.utils.StringUtils;
import com.ufpi.backend.validator.CpfValidator;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/motorista")
@Tag(name = "Motorista")
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class MotoristaController {

  private final MotoristaService motoristaService;
  private final String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

  @PostMapping
  public ResponseEntity<ResponseModel<MotoristaDTO>> cadastrar(
      @Valid @RequestBody MotoristaCreateDTO motoristaCreateDTO) {
    ResponseModel<MotoristaDTO> resposta = new ResponseModel<>();
    if (!CpfValidator.isCpf(motoristaCreateDTO.getCpf())) {
      resposta.setMessage("CPF inválido!");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
    }
    if (!Pattern.compile(emailRegex).matcher(motoristaCreateDTO.getEmail()).find()) {
      throw new InvalidDataError("email", "Email inválido!");
    }

    if (motoristaService.consultarMotoristaPeloCPF(motoristaCreateDTO.getCpf()) != null) {
      resposta.setMessage("Já existe um motorista cadastrado com o cpf informado!");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
    }

    if (StringUtils.isUsernameValido(motoristaCreateDTO.getUsername())) {
      resposta.setMessage("Username inválido! O username não pode conter espaços em branco.");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
    }

    Motorista motorista = motoristaService.insert(motoristaCreateDTO);
    resposta.setData(MotoristaDTO.fromEntity(motorista));
    return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
  }

  @PutMapping("/{username}")
  public ResponseEntity<ResponseModel<Motorista>> atualizar(@PathVariable String username,
      @Valid @RequestBody MotoristaUpdateDTO motoristaUpdateDTO) {
    ResponseModel<Motorista> resposta = new ResponseModel<>();
    MotoristaDTO motoristaExistente = motoristaService.consultarPorId(username);
    if (motoristaExistente == null) {
      throw new NotFoundError("Motorista não encontrado!");
    }
    if (!motoristaUpdateDTO.getEmail().isBlank()) {
      if (!Pattern.compile(emailRegex).matcher(motoristaUpdateDTO.getEmail()).find()) {
        throw new InvalidDataError("email", "Email inválido!");
      }
    }
    Motorista motoristaUpdate = MotoristaUpdateDTO.mapMotoristaUpdate(motoristaExistente,
        motoristaUpdateDTO);

    if (motoristaUpdate == null) {
      resposta.setMessage("O motorista com ID informado não existe!");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
    }
    resposta.setData(motoristaService.atualizar(username, motoristaUpdate));
    resposta.setMessage("Operação realizada com sucesso!");
    return ResponseEntity.status(HttpStatus.OK).body(resposta);
  }

  @PutMapping("/avaliar/{username}")
  public ResponseEntity<ResponseModel<Motorista>> avaliar(@PathVariable String username, @RequestBody Float nota) {
    ResponseModel<Motorista> resposta = new ResponseModel<>();
    MotoristaDTO motoristaExistente = motoristaService.consultarPorId(username);
    if (motoristaExistente == null) {
      throw new NotFoundError("Motorista não encontrado!");
    }
    if (nota < 0 || nota > 5) {
      throw new InvalidDataError("nota", "Nota inválida!");
    }
    resposta.setData(motoristaService.avaliar(username, nota));
    resposta.setMessage("Operação realizada com sucesso!");
    return ResponseEntity.status(HttpStatus.OK).body(resposta);
  }

  @GetMapping
  public ResponseEntity<ResponseModel<MotoristaDTO>> consultaPaginada(
      @PageableDefault(size = 10, sort = { "dataCadastro" }, direction = Sort.Direction.DESC) Pageable paginacao) {
    var resultado = motoristaService.consultar(paginacao);
    ResponseModel<MotoristaDTO> resposta = new ResponseModel<>();
    resposta.setList(resultado.getContent().stream().map(MotoristaDTO::fromEntity).toList());
    resposta.setTotalElements(resultado.getTotalElements());
    resposta.setTotalPages(resultado.getTotalPages());
    return ResponseEntity.status(HttpStatus.OK).body(resposta);
  }

  @GetMapping("/{username}")
  public ResponseEntity<ResponseModel<MotoristaDTO>> consultaPorId(@PathVariable String username) {
    var resultado = motoristaService.consultarPorId(username);
    ResponseModel<MotoristaDTO> resposta = new ResponseModel<>();
    resposta.setData(resultado);
    return ResponseEntity.status(HttpStatus.OK).body(resposta);
  }

  @DeleteMapping("/{username}")
  public ResponseEntity<Void> delete(@PathVariable String username) {
    var resultado = motoristaService.consultarPorId(username);
    motoristaService.excluir(username);
    ResponseModel<MotoristaDTO> resposta = new ResponseModel<>();
    resposta.setData(resultado);
    resposta.setMessage("Operação realizada com sucesso!");
    return ResponseEntity.status(HttpStatus.OK).build();
  }

}
