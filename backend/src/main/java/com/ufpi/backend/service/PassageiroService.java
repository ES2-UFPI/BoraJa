package com.ufpi.backend.service;

import static com.ufpi.backend.model.repository.PassageiroRepository.especificar;
import static com.ufpi.backend.model.repository.PassageiroRepository.paginar;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ufpi.backend.exceptions.NotFoundError;
import com.ufpi.backend.model.dto.passageiro.PassageiroCreateDTO;
import com.ufpi.backend.model.dto.passageiro.PassageiroDTO;
import com.ufpi.backend.model.entity.Passageiro;
import com.ufpi.backend.model.filter.PassageiroFiltroDTO;
import com.ufpi.backend.model.repository.PassageiroRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PassageiroService {

  private final PassageiroRepository passageiroRepository;

  public Page<Passageiro> buscarPagina(PassageiroFiltroDTO filtro) {
    return passageiroRepository.findAll(especificar(filtro), paginar(filtro));
  }

  public Passageiro findById(UUID id) {
    var result = passageiroRepository.findById(id);

    if (!result.isPresent()) {
      var message = String.format("Entidade 'Pessoa' nao encontrada pelo id: %s",
          id);
      log.warn(message);
      throw new NotFoundError(message);
    }

    return result.get();
  }

  public Passageiro insert(PassageiroCreateDTO passageiro) {
    Passageiro passageiroTemp = PassageiroCreateDTO.toEntity(passageiro);
    passageiroTemp.setDataCadastro(LocalDateTime.now());
    return passageiroRepository.save(passageiroTemp);
  }

  public Passageiro atualizar(UUID id, Passageiro passageiro) {
    passageiro.setDataAtualizacao(LocalDateTime.now());
    return passageiroRepository.save(passageiro);
  }

  public PassageiroDTO consultarPassageiroPeloCPF(String cpf) {
    var passageiro = passageiroRepository.findByCpf(cpf);
    return passageiro.map(PassageiroDTO::fromEntity).orElse(null);
  }

  public Page<Passageiro> consultar(Pageable paginacao) {
    return passageiroRepository.findAll(paginacao);
  }

  public PassageiroDTO consultarPorId(UUID id) {
    var resultado = passageiroRepository.findById(id);

    if (!resultado.isPresent()) {
      var mensagem = String.format("Entidade 'Passageiro' nao encontrada pelo id: %s", id);
      log.warn(mensagem);
      throw new NotFoundError(mensagem);
    }

    return PassageiroDTO.fromEntity(resultado.get());
  }

  public void excluir(UUID id) {
    passageiroRepository.deleteById(id);
  }

}
