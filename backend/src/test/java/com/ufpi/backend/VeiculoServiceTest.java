package com.ufpi.backend;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.ufpi.backend.exceptions.NotFoundError;
import com.ufpi.backend.model.dto.veiculo.VeiculoCreateDTO;
import com.ufpi.backend.model.dto.veiculo.VeiculoDTO;
import com.ufpi.backend.model.entity.Motorista;
import com.ufpi.backend.model.entity.Veiculo;
import com.ufpi.backend.model.repository.VeiculoRepository;
import com.ufpi.backend.service.VeiculoService;

@SpringBootTest
public class VeiculoServiceTest {

  @Mock
  private VeiculoRepository veiculoRepository;

  @InjectMocks
  private VeiculoService veiculoService;

  // @Test
  // void testBuscar() {
  // Veiculo veiculo = new Veiculo();
  // when(veiculoRepository.findAll()).thenReturn(List.of(veiculo));

  // List<Veiculo> veiculos = veiculoService.buscar();

  // assertThat(veiculos).hasSize(1);
  // assertThat(veiculos.get(0)).isEqualTo(veiculo);
  // }

  @Test
  void testFindByIdFound() {
    String placa = "ABC1234";
    Veiculo veiculo = new Veiculo();
    when(veiculoRepository.findById(placa)).thenReturn(Optional.of(veiculo));

    Veiculo result = veiculoService.findById(placa);

    assertThat(result).isEqualTo(veiculo);
  }

  @Test
  void testFindByIdNotFound() {
    String placa = "ABC1234";
    when(veiculoRepository.findById(placa)).thenReturn(Optional.empty());

    NotFoundError thrown = org.junit.jupiter.api.Assertions.assertThrows(NotFoundError.class, () -> {
      veiculoService.findById(placa);
    });

    assertThat(thrown.getMessage()).contains(placa);
  }

  @Test
  void testInsert() {
    VeiculoCreateDTO dto = new VeiculoCreateDTO();
    dto.setPlaca("abc1234");
    Motorista motorista = new Motorista();
    Veiculo veiculo = new Veiculo();
    when(veiculoRepository.save(any(Veiculo.class))).thenReturn(veiculo);

    Veiculo result = veiculoService.insert(dto, motorista);

    assertThat(result).isEqualTo(veiculo);
    verify(veiculoRepository).save(any(Veiculo.class));
  }

  @Test
  void testAtualizar() {
    Veiculo veiculo = new Veiculo();
    when(veiculoRepository.save(any(Veiculo.class))).thenReturn(veiculo);

    Veiculo result = veiculoService.atualizar(veiculo);
    veiculo.setDataAtualizacao(result.getDataAtualizacao());
    assertThat(result).isEqualTo(veiculo);
    verify(veiculoRepository).save(any(Veiculo.class));
  }

  @Test
  void testConsultarPorPlacaFound() {
    String placa = "ABC1234";
    Veiculo veiculo = new Veiculo();
    when(veiculoRepository.findByPlaca(placa)).thenReturn(Optional.of(veiculo));

    VeiculoDTO result = veiculoService.consultarPorPlaca(placa);

    assertThat(result).isNotNull();
  }

  @Test
  void testConsultarPorPlacaNotFound() {
    String placa = "ABC1234";
    when(veiculoRepository.findByPlaca(placa)).thenReturn(Optional.empty());

    NotFoundError thrown = org.junit.jupiter.api.Assertions.assertThrows(NotFoundError.class, () -> {
      veiculoService.consultarPorPlaca(placa);
    });

    assertThat(thrown.getMessage()).contains(placa);
  }

  // @Test
  // void testFindByProprietarioCpf() {
  // String cpf = "12345678901";
  // Veiculo veiculo = new Veiculo();
  // when(veiculoRepository.findByProprietarioCpf(cpf)).thenReturn(List.of(veiculo));

  // List<Veiculo> result = veiculoService.findByProprietarioCpf(cpf);

  // assertThat(result).hasSize(1);
  // assertThat(result.get(0)).isEqualTo(veiculo);
  // }

}
