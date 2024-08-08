package com.ufpi.backend.controller;

import java.util.regex.Pattern;

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
import com.ufpi.backend.exceptions.AppError;
import com.ufpi.backend.exceptions.InvalidDataError;
import com.ufpi.backend.model.dto.veiculo.VeiculoCreateDTO;
import com.ufpi.backend.model.dto.veiculo.VeiculoDTO;
import com.ufpi.backend.model.dto.veiculo.VeiculoUpdateDTO;
import com.ufpi.backend.model.entity.Motorista;
import com.ufpi.backend.model.entity.Veiculo;
import com.ufpi.backend.service.MotoristaService;
import com.ufpi.backend.service.VeiculoService;
import com.ufpi.backend.validator.CpfValidator;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Tag(name = "Veículo")
@RestController
@RequestMapping("/veiculo")
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class VeiculoController {

  private final VeiculoService veiculoService;
  private final MotoristaService motoristaService;
  private final String plateRegex = "^[a-zA-Z]{3}[0-9][A-Za-z0-9][0-9]{2}$";

  @PostMapping
  public ResponseEntity<ResponseModel<VeiculoDTO>> cadastrar(@Valid @RequestBody VeiculoCreateDTO veiculoCreateDTO) {
    if (!Pattern.compile(plateRegex).matcher(veiculoCreateDTO.getPlaca()).find()) {
      throw new InvalidDataError("placa", "Placa inválida!");
    }

    if (!CpfValidator.isCpf(veiculoCreateDTO.getCpfProprietario())) {
      throw new InvalidDataError("cpf", "Cpf do proprietário é inválido!");
    }

    Motorista motorista = motoristaService.findByCpf(veiculoCreateDTO.getCpfProprietario());

    if (motorista == null) {
      throw new AppError("Motorista não encontrado!");
    }
    var veiculo = veiculoService.insert(veiculoCreateDTO, motorista);
    ResponseModel<VeiculoDTO> resposta = new ResponseModel<>();
    resposta.setData(VeiculoDTO.fromEntity(veiculo));
    return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
  }

  @PutMapping("/{placa}")
  public ResponseEntity<ResponseModel<Veiculo>> atualizar(@PathVariable String placa,
      @Valid @RequestBody VeiculoUpdateDTO veiculoUpdateDTO) {
    ResponseModel<Veiculo> resposta = new ResponseModel<>();
    if (!Pattern.compile(plateRegex).matcher(placa).find()) {
      throw new InvalidDataError("placa", "Placa inválida!");
    }
    VeiculoDTO veiculoExistente = veiculoService.consultarPorPlaca(placa.toUpperCase());

    Veiculo veiculoUpdate = VeiculoUpdateDTO.mapVeiculoUpdate(veiculoExistente,
        veiculoUpdateDTO);
    if (veiculoUpdate == null) {
      resposta.setMessage("O veiculo com a placa informado não existe!");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
    }

    resposta.setData(veiculoService.atualizar(veiculoUpdate));
    resposta.setMessage("Operação realizada com sucesso!");

    return ResponseEntity.status(HttpStatus.OK).body(resposta);
  }

  @GetMapping("/{placa}")
  public ResponseEntity<ResponseModel<VeiculoDTO>> consultaPorPlaca(@PathVariable String placa) {
    if (!Pattern.compile(plateRegex).matcher(placa).find()) {
      throw new InvalidDataError("placa", "Placa inválida!");
    }
    var resultado = veiculoService.consultarPorPlaca(placa.toUpperCase());
    ResponseModel<VeiculoDTO> resposta = new ResponseModel<>();
    resposta.setData(resultado);
    return ResponseEntity.status(HttpStatus.OK).body(resposta);
  }

  @DeleteMapping("/{placa}")
  public ResponseEntity<Void> delete(@PathVariable String placa) {
    if (!Pattern.compile(plateRegex).matcher(placa).find()) {
      throw new InvalidDataError("placa", "Placa inválida!");
    }
    var resultado = veiculoService.consultarPorPlaca(placa.toUpperCase());
    veiculoService.excluir(placa);
    ResponseModel<VeiculoDTO> resposta = new ResponseModel<>();
    resposta.setData(resultado);
    resposta.setMessage("Operação realizada com sucesso!");
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
