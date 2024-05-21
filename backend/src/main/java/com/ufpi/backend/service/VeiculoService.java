package com.ufpi.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import com.ufpi.backend.exceptions.NotFoundError;
import com.ufpi.backend.model.dto.veiculo.VeiculoCreateDTO;
import com.ufpi.backend.model.dto.veiculo.VeiculoDTO;
import com.ufpi.backend.model.dto.veiculo.VeiculoUpdateDTO;
import com.ufpi.backend.model.entity.Motorista;
import com.ufpi.backend.model.entity.Veiculo;
import com.ufpi.backend.model.repository.VeiculoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class VeiculoService {

  private final VeiculoRepository veiculoRepository;

  public List<Veiculo> buscar() {
    return veiculoRepository.findAll();
  }

  public Veiculo findById(String placa) {
    var result = veiculoRepository.findById(placa);

    if (!result.isPresent()) {
      var message = String.format("Entidade 'Veiculo' nao encontrada pela placa: %s",
          placa);
      log.warn(message);
      throw new NotFoundError(message);
    }

    return result.get();
  }

  public Veiculo insert(VeiculoCreateDTO veiculoCreateDTO, Motorista motorista) {
    Veiculo veiculoTemp = VeiculoCreateDTO.toEntity(veiculoCreateDTO);
    veiculoTemp.setAtivo(true);

    veiculoTemp.setProprietario(motorista);
    veiculoTemp.setDataCadastro(LocalDateTime.now());
    return veiculoRepository.save(veiculoTemp);
  }

  public Veiculo atualizar(String placa, VeiculoUpdateDTO veiculoUpdateDTO) {
    VeiculoDTO veiculoExistente = consultarPorPlaca(placa);

    Veiculo veiculoUpdate = VeiculoUpdateDTO.mapVeiculoUpdate(veiculoExistente,
        veiculoUpdateDTO);
    veiculoUpdate.setDataAtualizacao(LocalDateTime.now());

    return veiculoRepository.save(veiculoUpdate);
  }

  public VeiculoDTO consultarPorPlaca(String placa) {
    var resultado = veiculoRepository.findByPlaca(placa);

    if (!resultado.isPresent()) {
      var mensagem = String.format("Entidade 'Veiculo' nao encontrada pela placa: %s", placa);
      log.warn(mensagem);
      throw new NotFoundError(mensagem);
    }

    return VeiculoDTO.fromEntity(resultado.get());
  }

  public void excluir(String placa) {
    veiculoRepository.deleteByPlaca(placa);
  }

}