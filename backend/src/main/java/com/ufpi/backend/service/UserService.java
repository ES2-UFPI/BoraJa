package com.ufpi.backend.service;

import static com.ufpi.backend.model.repository.UserRepository.specify;
import static com.ufpi.backend.model.repository.UserRepository.paginar;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.ufpi.backend.exceptions.NotFoundError;
import com.ufpi.backend.model.dto.UserCreateDTO;
import com.ufpi.backend.model.dto.UserDTO;
import com.ufpi.backend.model.entity.User;
import com.ufpi.backend.model.filter.UserFilterDTO;
import com.ufpi.backend.model.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public Page<User> buscarPagina(UserFilterDTO filtro) {
    return userRepository.findAll(specify(filtro), paginar(filtro));
  }

  public User findById(UUID id) {
    var result = userRepository.findById(id);

    if (!result.isPresent()) {
      var message = String.format("Entidade 'User' nao encontrada pelo id: %s",
          id);
      log.warn(message);
      throw new NotFoundError(message);
    }

    return result.get();
  }

  public User insert(UserCreateDTO pessoa) {
    User pessoaTemp = UserCreateDTO.toEntity(pessoa);
    pessoaTemp.setDataCadastro(LocalDateTime.now());
    return userRepository.save(pessoaTemp);
  }

  // public void excluir(UUID id) {
  // userRepository.deleteById(id);
  // }

  // public Long count() {
  // return userRepository.count();
  // }

  // public boolean existePorId(UUID id) {
  // return userRepository.existsById(id);
  // }

  // public User atualizar(UUID id, UserUpdateDTO pessoaDTO) {
  // UserDTO pessoaExistente = consultarPorId(id);

  // Pessoa pessoaUpdate = PessoaMapper.mapPessoaUpdate(pessoaExistente,
  // pessoaDTO);
  // pessoaUpdate.setDataAtualizacao(LocalDateTime.now());

  // return userRepository.save(pessoaUpdate);
  // }

  public UserDTO consultarPessoaPeloCPF(String cpf) {
    User pessoa = userRepository.findByCpf(cpf);
    return UserDTO.fromEntity(pessoa);
  }

  // @Transactional
  // public List<Pessoa> buscar(FiltroPessoaDTO filtro) {
  // return userRepository.findAll(specify(filtro));
  // }

  public Page<User> consultar(Pageable paginacao) {
    log.info("recuperando registros com paginacao... {}", paginacao);
    return userRepository.findAll(paginacao);
  }
}
