package com.ufpi.backend.service;

import static com.ufpi.backend.model.repository.MotoristaRepository.especificar;
import static com.ufpi.backend.model.repository.MotoristaRepository.paginar;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.ufpi.backend.exceptions.NotFoundError;
import com.ufpi.backend.model.dto.motorista.MotoristaCreateDTO;
import com.ufpi.backend.model.dto.motorista.MotoristaDTO;
import com.ufpi.backend.model.entity.Motorista;
import com.ufpi.backend.model.filter.MotoristaFiltroDTO;
import com.ufpi.backend.model.repository.MotoristaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MotoristaService {

  private final MotoristaRepository motoristaRepository;

  public Page<Motorista> buscarPagina(MotoristaFiltroDTO filtro) {
    return motoristaRepository.findAll(especificar(filtro), paginar(filtro));
  }

  public Motorista findById(String username) {
    var result = motoristaRepository.findById(username);

    if (!result.isPresent()) {
      var message = String.format("Entidade 'Pessoa' nao encontrada pelo id: %s",
          username);
      log.warn(message);
      throw new NotFoundError(message);
    }

    return result.get();
  }

  public Motorista insert(MotoristaCreateDTO motorista) {
    Motorista motoristaTemp = MotoristaCreateDTO.toEntity(motorista);
    motoristaTemp.setDataCadastro(LocalDateTime.now());
    return motoristaRepository.save(motoristaTemp);
  }

  public Motorista atualizar(String id, Motorista motorista) {
    motorista.setDataAtualizacao(LocalDateTime.now());
    return motoristaRepository.save(motorista);
  }

  public Motorista avaliar(String id, Float nota) {
    Motorista motorista = findById(id);
    motorista.setAvaliacao(calcularMedia(motorista.getCorridasAvaliadas(), nota, motorista.getAvaliacao()));
    motorista.incrementarCorridasAvaliadas();
    motorista.setDataAtualizacao(LocalDateTime.now());
    return motoristaRepository.save(motorista);
  }

  public MotoristaDTO consultarMotoristaPeloCPF(String cpf) {
    Optional<Motorista> motorista = motoristaRepository.findByCpf(cpf);
    return motorista.map(MotoristaDTO::fromEntity).orElse(null);
  }

  public Page<Motorista> consultar(Pageable paginacao) {
    return motoristaRepository.findAll(paginacao);
  }

  public MotoristaDTO consultarPorId(String id) {
    var resultado = motoristaRepository.findById(id);

    if (!resultado.isPresent()) {
      var mensagem = String.format("Entidade 'Motorista' nao encontrada pelo id: %s", id);
      log.warn(mensagem);
      throw new NotFoundError(mensagem);
    }

    return MotoristaDTO.fromEntity(resultado.get());
  }

  public void excluir(String id) {
    motoristaRepository.deleteById(id);
  }

  public Motorista findByCpf(String cpfProprietario) {
    return motoristaRepository.findByCpf(cpfProprietario).orElse(null);
  }

  public Float calcularMedia(Integer totalAvaliacoes, Float nota, Float notaAtual) {
    Float somaNotas = notaAtual * totalAvaliacoes;

    return (somaNotas + nota) / totalAvaliacoes + 1;
  }
}
