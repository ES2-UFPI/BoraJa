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
import com.ufpi.backend.model.dto.motorista.MotoristaCreateDTO;
import com.ufpi.backend.model.dto.motorista.MotoristaDTO;
import com.ufpi.backend.service.MotoristaService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/motorista")
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class MotoristaController {

  private final MotoristaService motoristaService;

  @PostMapping
  public ResponseEntity<ResponseModel<MotoristaDTO>> cadastrar(MotoristaCreateDTO motoristaCreateDTO) {
    var motorista = motoristaService.insert(motoristaCreateDTO);
    ResponseModel<MotoristaDTO> resposta = new ResponseModel<>();
    resposta.setData(MotoristaDTO.fromEntity(motorista));
    return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
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

  @GetMapping("/{id}")
  public ResponseEntity<ResponseModel<MotoristaDTO>> consultaPorId(@PathVariable UUID id) {
    var resultado = motoristaService.consultarPorId(id);
    ResponseModel<MotoristaDTO> resposta = new ResponseModel<>();
    resposta.setData(resultado);
    return ResponseEntity.status(HttpStatus.OK).body(resposta);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    var resultado = motoristaService.consultarPorId(id);
    motoristaService.excluir(id);
    ResponseModel<MotoristaDTO> resposta = new ResponseModel<>();
    resposta.setData(resultado);
    resposta.setMessage("Operação realizada com sucesso!");
    return ResponseEntity.status(HttpStatus.OK).build();
  }

}
