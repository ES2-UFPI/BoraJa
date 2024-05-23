package com.ufpi.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ufpi.backend.exceptions.AppError;
import com.ufpi.backend.exceptions.NotFoundError;
import com.ufpi.backend.model.dto.viagem.ViagemCreateDTO;
import com.ufpi.backend.model.dto.viagem.ViagemDTO;
import com.ufpi.backend.model.entity.Viagem;
import com.ufpi.backend.model.enums.StatusViagem;
import com.ufpi.backend.model.repository.MotoristaRepository;
import com.ufpi.backend.model.repository.PassageiroRepository;
import com.ufpi.backend.model.repository.VeiculoRepository;
import com.ufpi.backend.model.repository.ViagemRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ViagemService {

  private final ViagemRepository viagemRepository;
  private final MotoristaRepository motoristaRepository;
  private final PassageiroRepository passageiroRepository;
  private final VeiculoRepository veiculoRepository;

  public List<Viagem> buscar() {
    return viagemRepository.findAll();
  }

  public Viagem findById(UUID id) {
    var result = viagemRepository.findById(id);
    if (!result.isPresent()) {
      var message = String.format("Entidade 'Viagem' nao encontrada pela id: %s",
          id);
      log.warn(message);
      throw new NotFoundError(message);
    }
    return result.get();
  }

  public Viagem insert(ViagemCreateDTO viagemCreateDTO) {
    Viagem viagemTemp = ViagemCreateDTO.toEntity(viagemCreateDTO);
    viagemTemp.setDataCadastro(LocalDateTime.now());
    return viagemRepository.save(viagemTemp);
  }

  public Viagem atualizar(UUID id, Viagem viagemUpdate) {
    viagemUpdate.setDataAtualizacao(LocalDateTime.now());
    return viagemRepository.save(viagemUpdate);
  }

  public ViagemDTO consultaPorId(UUID id) {
    var resultado = viagemRepository.findById(id);

    if (!resultado.isPresent()) {
      var mensagem = String.format("Entidade 'Viagem' nao encontrada pelo id: %s", id);
      log.warn(mensagem);
      throw new NotFoundError(mensagem);
    }

    return ViagemDTO.fromEntity(resultado.get());
  }

  public List<Viagem> findViagensByMotorista(UUID id) {
    List<Viagem> result = viagemRepository.findByMotorista(id);
    if (!result.isEmpty()) {
      var message = String.format("Entidade 'Viagem' nao encontrada pelo id: %s",
          id);
      log.warn(message);
      throw new NotFoundError(message);
    }
    return result;
  }

  public Optional<Viagem> findViagemAtivaByMotorista(UUID id) {
    Optional<Viagem> result = viagemRepository.findViagemAtivaByMotorista(id);
    if (!result.isEmpty()) {
      var message = String.format("Entidade 'Viagem' nao encontrada pelo id: %s",
          id);
      log.warn(message);
      throw new NotFoundError(message);
    }
    return result;
  }

  public Boolean existsViagemAtivaByMotorista(UUID id) {
    return viagemRepository.existsViagemByMotorista(id);
  }

  public Boolean existsViagemAtivaByVeiculo(String placa) {
    return viagemRepository.existsViagemByVeiculo(placa);
  }

  public Boolean existsViagemAtivaByPassageiro(UUID id) {
    return viagemRepository.existsViagemByPassageiro(id);
  }

  public void verificarViagemCreateDTO(@Valid ViagemCreateDTO viagemCreateDTO) {
    if (motoristaRepository.findById(viagemCreateDTO.getMotoristaId()).isEmpty()) {
      throw new AppError("Motorista não encontrado.");
    }
    if (passageiroRepository.findById(viagemCreateDTO.getPassageiroId()).isEmpty()) {
      throw new AppError("Passageiro não encontrado.");
    }
    if (veiculoRepository.findById(viagemCreateDTO.getVeiculoPlaca()).isEmpty()) {
      throw new AppError("Veículo não encontrado.");
    }
    if (viagemRepository.existsViagemByPassageiro(viagemCreateDTO.getPassageiroId())) {
      throw new AppError("Passageiro já possui viagem ativa.");
    }
    if (viagemRepository.existsViagemByVeiculo(viagemCreateDTO.getVeiculoPlaca())) {
      throw new AppError("Veículo já possui viagem ativa.");
    }
    if (viagemRepository.existsViagemByMotorista(viagemCreateDTO.getMotoristaId())) {
      throw new AppError("Motorista já possui viagem ativa.");
    }
    throw new AppError("Unimplemented method 'verificarViagemCreateDTO'");
  }

  public Viagem atualizarStatus(UUID id, StatusViagem status) {
    Viagem viagem = findById(id);
    if (viagem == null) {
      throw new NotFoundError("Viagem não encontrada.");
    }
    switch (status) {
      case FINALIZADA:
        viagem.finalizarViagem();
        break;
      case CANCELADA:
        viagem.cancelarViagem();
        break;
      case INICIADA:
        viagem.iniciarViagem();
        break;
      default:
        throw new AppError("Status inválido.");
    }
    return viagemRepository.save(viagem);
  }

  // public Boolean existeByCpfProprietario(String cpfProprietario) {
  // return viagemRepository.existsByProprietarioCpf(cpfProprietario);
  // }

  // public void excluir(String placa) {
  // viagemRepository.deleteByPlaca(placa);
  // }

}