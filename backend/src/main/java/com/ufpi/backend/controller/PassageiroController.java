package com.ufpi.backend.controller;

import java.util.UUID;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufpi.backend.config.response.ResponseModel;
import com.ufpi.backend.model.dto.passageiro.PassageiroCreateDTO;
import com.ufpi.backend.model.dto.passageiro.PassageiroDTO;
import com.ufpi.backend.service.PassageiroService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/passageiro")
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class PassageiroController {

  private final PassageiroService passageiroService;

  @PostMapping
  public ResponseEntity<ResponseModel<PassageiroDTO>> cadastrar(PassageiroCreateDTO passageiroCreateDTO) {
    var passageiro = passageiroService.insert(passageiroCreateDTO);
    ResponseModel<PassageiroDTO> resposta = new ResponseModel<>();
    resposta.setData(PassageiroDTO.fromEntity(passageiro));
    return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
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

  @GetMapping("/{id}")
  public ResponseEntity<ResponseModel<PassageiroDTO>> consultaPorId(@PathVariable UUID id) {
    var resultado = passageiroService.consultarPorId(id);
    ResponseModel<PassageiroDTO> resposta = new ResponseModel<>();
    resposta.setData(resultado);
    return ResponseEntity.status(HttpStatus.OK).body(resposta);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    var resultado = passageiroService.consultarPorId(id);
    passageiroService.excluir(id);
    ResponseModel<PassageiroDTO> resposta = new ResponseModel<>();
    resposta.setData(resultado);
    resposta.setMessage("Operação realizada com sucesso!");
    return ResponseEntity.status(HttpStatus.OK).build();
  }

}
