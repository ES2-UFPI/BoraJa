package com.ufpi.backend.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufpi.backend.config.response.ResponseModel;
import com.ufpi.backend.model.dto.PessoaCreateDTO;
import com.ufpi.backend.model.dto.PessoaDTO;
import com.ufpi.backend.service.PessoaService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class PessoaController {

  private final PessoaService pessoaService;

  @PostMapping
  public ResponseEntity<ResponseModel<PessoaDTO>> cadastrar(PessoaCreateDTO pessoaDTO) {
    var user = pessoaService.insert(pessoaDTO);
    ResponseModel<PessoaDTO> resposta = new ResponseModel<>();
    resposta.setData(PessoaDTO.fromEntity(user));
    return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
  }

  @GetMapping
  public ResponseEntity<ResponseModel<PessoaDTO>> consultaPaginada(
      @PageableDefault(size = 10, sort = { "dataCadastro" }, direction = Sort.Direction.DESC) Pageable paginacao) {
    var resultado = pessoaService.consultar(paginacao);
    ResponseModel<PessoaDTO> resposta = new ResponseModel<>();
    resposta.setList(resultado.getContent().stream().map(PessoaDTO::fromEntity).toList());
    resposta.setTotalElements(resultado.getTotalElements());
    resposta.setTotalPages(resultado.getTotalPages());
    return ResponseEntity.status(HttpStatus.OK).body(resposta);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ResponseModel<PessoaDTO>> consultaPorId(@PathVariable UUID id) {
    var resultado = pessoaService.consultarPorId(id);
    ResponseModel<PessoaDTO> resposta = new ResponseModel<>();
    resposta.setData(resultado);
    return ResponseEntity.status(HttpStatus.OK).body(resposta);
  }
}
