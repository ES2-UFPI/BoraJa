package com.ufpi.backend.controller;

import java.util.Optional;

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
import com.ufpi.backend.model.dto.veiculo.VeiculoCreateDTO;
import com.ufpi.backend.model.dto.veiculo.VeiculoDTO;
import com.ufpi.backend.model.entity.Motorista;
import com.ufpi.backend.service.MotoristaService;
import com.ufpi.backend.service.PassageiroService;
import com.ufpi.backend.service.VeiculoService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/veiculo")
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class VeiculoController {

  private final VeiculoService veiculoService;
  private final MotoristaService motoristaService;

  @PostMapping
  public ResponseEntity<ResponseModel<VeiculoDTO>> cadastrar(VeiculoCreateDTO veiculoCreateDTO) {
    Motorista motorista = motoristaService.findByCpf(veiculoCreateDTO.getCpfProprietario());
    if (motorista == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    var veiculo = veiculoService.insert(veiculoCreateDTO, motorista);
    ResponseModel<VeiculoDTO> resposta = new ResponseModel<>();
    resposta.setData(VeiculoDTO.fromEntity(veiculo));
    return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
  }

  @GetMapping("/{placa}")
  public ResponseEntity<ResponseModel<VeiculoDTO>> consultaPorPlaca(@PathVariable String placa) {
    var resultado = veiculoService.consultarPorPlaca(placa);
    ResponseModel<VeiculoDTO> resposta = new ResponseModel<>();
    resposta.setData(resultado);
    return ResponseEntity.status(HttpStatus.OK).body(resposta);
  }

  @DeleteMapping("/{placa}")
  public ResponseEntity<Void> delete(@PathVariable String placa) {
    var resultado = veiculoService.consultarPorPlaca(placa);
    veiculoService.excluir(placa);
    ResponseModel<VeiculoDTO> resposta = new ResponseModel<>();
    resposta.setData(resultado);
    resposta.setMessage("Operação realizada com sucesso!");
    return ResponseEntity.status(HttpStatus.OK).build();
  }

}
