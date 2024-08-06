package com.ufpi.backend.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.ufpi.backend.model.dto.passageiro.PassageiroInsertDTO;
import com.ufpi.backend.model.dto.viagem.ViagemCreateDTO;
import com.ufpi.backend.model.dto.viagem.ViagemDTO;
import com.ufpi.backend.model.entity.Vaga;
import com.ufpi.backend.model.entity.Viagem;
import com.ufpi.backend.model.enums.StatusViagem;
import com.ufpi.backend.model.filter.FiltroViagemDTO;
import com.ufpi.backend.service.ViagemService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Viagem")
@RestController
@RequiredArgsConstructor
@RequestMapping("/viagem")
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class ViagemController {

  private final ViagemService viagemService;

  @GetMapping("/search")
  public Page<ViagemDTO> buscarPorFiltros(FiltroViagemDTO filtro) {
    return viagemService.buscarPagina(filtro);
  }

  @PostMapping
  public ResponseEntity<ResponseModel<ViagemDTO>> cadastrar(@Valid @RequestBody ViagemCreateDTO viagemCreateDTO) {
    if (viagemCreateDTO == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    viagemService.verificarViagemCreateDTO(viagemCreateDTO);
    Viagem viagem = viagemService.insert(viagemCreateDTO);
    ResponseModel<ViagemDTO> resposta = new ResponseModel<>();
    resposta.setData(ViagemDTO.fromEntity(viagem));
    return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
  }

  @PutMapping("/finalizar/{id}")
  public ResponseEntity<ResponseModel<ViagemDTO>> finalizar(@PathVariable UUID id) {
    if (id == null) {
      throw new InvalidDataError("id", "Id não pode ser nulo");
    }
    Viagem viagem = viagemService.atualizarStatus(id, StatusViagem.FINALIZADA);
    ResponseModel<ViagemDTO> resposta = new ResponseModel<>();
    resposta.setData(ViagemDTO.fromEntity(viagem));
    return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
  }

  @PutMapping("/cancelar/{id}")
  public ResponseEntity<ResponseModel<ViagemDTO>> cancelar(@PathVariable UUID id) {
    if (id == null) {
      throw new InvalidDataError("id", "Id não pode ser nulo");
    }
    Viagem viagem = viagemService.atualizarStatus(id, StatusViagem.CANCELADA);
    ResponseModel<ViagemDTO> resposta = new ResponseModel<>();
    resposta.setData(ViagemDTO.fromEntity(viagem));
    return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
  }

  @PutMapping("/iniciar/{id}")
  public ResponseEntity<ResponseModel<ViagemDTO>> iniciar(@PathVariable UUID id) {
    if (id == null) {
      throw new InvalidDataError("id", "Id não pode ser nulo");
    }
    Viagem viagem = viagemService.atualizarStatus(id, StatusViagem.INICIADA);
    ResponseModel<ViagemDTO> resposta = new ResponseModel<>();
    resposta.setData(ViagemDTO.fromEntity(viagem));
    return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
  }

  @PutMapping("/ingressar/{id}")
  public ResponseEntity<ResponseModel<Vaga>> ingressar(@PathVariable UUID id,
      @RequestBody PassageiroInsertDTO passageiroInsertDTO) {
    if (id == null) {
      throw new InvalidDataError("id", "Id não pode ser nulo");
    }
    if (passageiroInsertDTO == null) {
      throw new InvalidDataError("passageiroId", "Id do passageiro não pode ser nulo");
    }
    if (viagemService.existsViagemAtivaByPassageiro(passageiroInsertDTO.getUsername(), id)) {
      throw new AppError("Passageiro já possui viagem ativa. Finalize-a antes de ingressar em outra.");
    }
    Vaga vaga = viagemService.ingressar(id, passageiroInsertDTO.getUsername());
    ResponseModel<Vaga> resposta = new ResponseModel<>();
    resposta.setData(vaga);
    return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
  }

  @GetMapping("/vagas/{id}")
  public ResponseEntity<ResponseModel<Vaga>> buscarVagas(@PathVariable UUID id) {
    if (id == null) {
      throw new InvalidDataError("id", "Id não pode ser nulo");
    }
    ResponseModel<Vaga> resposta = new ResponseModel<>();
    resposta.setList(viagemService.buscarVagas(id));
    return ResponseEntity.status(HttpStatus.OK).body(resposta);
  }

}
