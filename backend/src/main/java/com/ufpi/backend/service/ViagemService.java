package com.ufpi.backend.service;

import static com.ufpi.backend.model.repository.ViagemRepository.especificar;
import static com.ufpi.backend.model.repository.ViagemRepository.paginar;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ufpi.backend.exceptions.AppError;
import com.ufpi.backend.exceptions.NotFoundError;
import com.ufpi.backend.model.dto.viagem.ViagemCreateDTO;
import com.ufpi.backend.model.dto.viagem.ViagemDTO;
import com.ufpi.backend.model.entity.Motorista;
import com.ufpi.backend.model.entity.Passageiro;
import com.ufpi.backend.model.entity.Vaga;
import com.ufpi.backend.model.entity.Veiculo;
import com.ufpi.backend.model.entity.Viagem;
import com.ufpi.backend.model.enums.StatusViagem;
import com.ufpi.backend.model.enums.TipoVeiculo;
import com.ufpi.backend.model.filter.FiltroViagemDTO;
import com.ufpi.backend.model.repository.LocalidadeRepository;
import com.ufpi.backend.model.repository.MotoristaRepository;
import com.ufpi.backend.model.repository.PassageiroRepository;
import com.ufpi.backend.model.repository.VagaRepository;
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
  private final LocalidadeRepository localidadeRepository;
  private final MotoristaRepository motoristaRepository;
  private final VeiculoRepository veiculoRepository;
  private final PassageiroRepository passageiroRepository;
  private final VagaRepository vagaRepository;

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
    Veiculo veiculo = veiculoRepository.findById(viagemCreateDTO.getVeiculoPlaca()).orElse(null);
    Motorista motorista = motoristaRepository.findById(viagemCreateDTO.getMotoristaId()).orElse(null);
    viagemTemp.setVeiculo(veiculo);
    viagemTemp.setMotorista(motorista);
    viagemTemp.setDataCadastro(LocalDateTime.now());
    viagemTemp.setOrigem(localidadeRepository.save(viagemTemp.getOrigem()));
    viagemTemp.setDestino(localidadeRepository.save(viagemTemp.getDestino()));
    viagemTemp.setFinalizada(false);
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

  public List<Viagem> findViagensByMotorista(String id) {
    List<Viagem> result = viagemRepository.findByMotorista(id);
    if (!result.isEmpty()) {
      var message = String.format("Entidade 'Viagem' nao encontrada pelo id: %s",
          id);
      log.warn(message);
      throw new NotFoundError(message);
    }
    return result;
  }

  public Optional<Viagem> findViagemAtivaByMotorista(String id) {
    Optional<Viagem> result = viagemRepository.findViagemAtivaByMotorista(id);
    if (!result.isEmpty()) {
      var message = String.format("Entidade 'Viagem' nao encontrada pelo id: %s",
          id);
      log.warn(message);
      throw new NotFoundError(message);
    }
    return result;
  }

  public Boolean existsViagemAtivaByMotorista(String id) {
    return viagemRepository.existsViagemByMotorista(id);
  }

  public Boolean existsViagemAtivaByVeiculo(String placa) {
    return viagemRepository.existsViagemByVeiculo(placa);
  }

  public Boolean existsViagemAtivaByPassageiro(String passageiroId, UUID viagemId) {
    return vagaRepository.existsViagemAtivaByPassageiro(passageiroId, viagemId);
  }

  public void verificarViagemCreateDTO(@Valid ViagemCreateDTO viagemCreateDTO) {
    Optional<Veiculo> veiculo = veiculoRepository.findById(viagemCreateDTO.getVeiculoPlaca());
    if (motoristaRepository.findById(viagemCreateDTO.getMotoristaId()).isEmpty()) {
      throw new AppError("Motorista não encontrado.");
    }
    if (veiculo.isEmpty()) {
      throw new AppError("Veículo não encontrado.");
    }
    if (veiculo.get().getTipo() == TipoVeiculo.CARRO) {
      if (viagemCreateDTO.getQuantidadeVagas() > 4) {
        throw new AppError("Carro não pode ter mais de 4 vagas.");
      }
    } else if (veiculo.get().getTipo() == TipoVeiculo.MOTO) {
      if (viagemCreateDTO.getQuantidadeVagas() > 2) {
        throw new AppError("Moto não pode ter mais de 2 vagas.");
      }
    }
    if (viagemRepository.existsViagemByVeiculo(viagemCreateDTO.getVeiculoPlaca())) {
      throw new AppError("Veículo já possui viagem ativa.");
    }
    if (viagemRepository.existsViagemByMotorista(viagemCreateDTO.getMotoristaId())) {
      throw new AppError("Motorista já possui viagem ativa.");
    }
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

  public Vaga ingressar(UUID id, String passageiroId) {
    Viagem viagem = findById(id);
    Passageiro passageiro = passageiroRepository.findById(passageiroId).orElse(null);

    if (viagem == null) {
      throw new NotFoundError("Viagem não encontrada.");
    }
    if (passageiro == null) {
      throw new NotFoundError("Passageiro não encontrado.");
    }

    List<Vaga> vagas = vagaRepository.findByViagem(id);
    if (vagas.size() >= viagem.getQuantidadeVagas()) {
      throw new AppError("Veículo lotado.");
    }

    Vaga vaga = new Vaga();
    vaga.setPassageiro(passageiro);
    vaga.setViagem(viagem);
    return vagaRepository.save(vaga);
  }

  public Page<ViagemDTO> buscarPagina(FiltroViagemDTO filtro) {
    return viagemRepository.findAll(especificar(filtro), paginar(filtro))
        .map(ViagemDTO::fromEntity);
  }

}
