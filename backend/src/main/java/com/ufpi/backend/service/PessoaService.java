package com.ufpi.backend.service;

import static com.ufpi.backend.model.repository.PessoaRepository.especificar;
import static com.ufpi.backend.model.repository.PessoaRepository.paginar;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.ufpi.backend.exceptions.NotFoundError;
import com.ufpi.backend.model.dto.PessoaCreateDTO;
import com.ufpi.backend.model.dto.PessoaDTO;
import com.ufpi.backend.model.entity.Pessoa;
import com.ufpi.backend.model.filter.PessoaFiltroDTO;
import com.ufpi.backend.model.repository.PessoaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PessoaService {

  private final PessoaRepository pessoaRepository;

  public Page<Pessoa> buscarPagina(PessoaFiltroDTO filtro) {
    return pessoaRepository.findAll(especificar(filtro), paginar(filtro));
  }

  public Pessoa findById(UUID id) {
    var result = pessoaRepository.findById(id);

    if (!result.isPresent()) {
      var message = String.format("Entidade 'Pessoa' nao encontrada pelo id: %s",
          id);
      log.warn(message);
      throw new NotFoundError(message);
    }

    return result.get();
  }

  public Pessoa insert(PessoaCreateDTO pessoa) {
    Pessoa pessoaTemp = PessoaCreateDTO.toEntity(pessoa);
    pessoaTemp.setDataCadastro(LocalDateTime.now());
    return pessoaRepository.save(pessoaTemp);
  }

  // public void excluir(UUID id) {
  // pessoaRepository.deleteById(id);
  // }

  // public Long count() {
  // return pessoaRepository.count();
  // }

  // public boolean existePorId(UUID id) {
  // return pessoaRepository.existsById(id);
  // }

  // public Pessoa atualizar(UUID id, PessoaUpdateDTO pessoaDTO) {
  // PessoaDTO pessoaExistente = consultarPorId(id);

  // Pessoa pessoaUpdate = PessoaMapper.mapPessoaUpdate(pessoaExistente,
  // pessoaDTO);
  // pessoaUpdate.setDataAtualizacao(LocalDateTime.now());

  // return pessoaRepository.save(pessoaUpdate);
  // }

  public PessoaDTO consultarPessoaPeloCPF(String cpf) {
    Pessoa pessoa = pessoaRepository.findByCpf(cpf);
    return PessoaDTO.fromEntity(pessoa);
  }

  // @Transactional
  // public List<Pessoa> buscar(FiltroPessoaDTO filtro) {
  // return pessoaRepository.findAll(specify(filtro));
  // }

  public Page<Pessoa> consultar(Pageable paginacao) {
    log.info("recuperando registros com paginacao... {}", paginacao);
    return pessoaRepository.findAll(paginacao);
  }

  public PessoaDTO consultarPorId(UUID id) {
    var resultado = pessoaRepository.findById(id);

    if (!resultado.isPresent()) {
      var mensagem = String.format("Entidade 'Pessoa' nao encontrada pelo id: %s", id);
      log.warn(mensagem);
      throw new NotFoundError(mensagem);
    }

    return PessoaDTO.fromEntity(resultado.get());
  }
}
