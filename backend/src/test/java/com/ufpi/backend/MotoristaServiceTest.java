package com.ufpi.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.ufpi.backend.exceptions.NotFoundError;
import com.ufpi.backend.model.dto.motorista.MotoristaCreateDTO;
import com.ufpi.backend.model.dto.motorista.MotoristaDTO;
import com.ufpi.backend.model.entity.Motorista;
import com.ufpi.backend.model.filter.MotoristaFiltroDTO;
import com.ufpi.backend.model.repository.MotoristaRepository;
import com.ufpi.backend.service.MotoristaService;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
public class MotoristaServiceTest {

  @Mock
  private MotoristaRepository motoristaRepository;

  @InjectMocks
  private MotoristaService motoristaService;

  // @Test
  // public void testBuscarPagina() {
  // MotoristaFiltroDTO filtro = new MotoristaFiltroDTO();
  // Page<Motorista> page = new PageImpl<>(Collections.singletonList(new
  // Motorista()));
  // when(motoristaRepository.findAll(any(), any())).thenReturn(page);

  // Page<Motorista> result = motoristaService.buscarPagina(filtro);

  // assertEquals(page, result);
  // }

  @Test
  public void testFindById() {
    Motorista motoristaMock = new Motorista();
    when(motoristaRepository.findById("username")).thenReturn(Optional.of(motoristaMock));

    Motorista result = motoristaService.findById("username");

    assertEquals(motoristaMock, result);
  }

  @Test
  public void testFindByIdNotFound() {
    when(motoristaRepository.findById("username")).thenReturn(Optional.empty());

    assertThrows(NotFoundError.class, () -> {
      motoristaService.findById("username");
    });
  }

  @Test
  public void testInsert() {
    MotoristaCreateDTO dto = new MotoristaCreateDTO();
    Motorista motoristaMock = new Motorista();
    when(motoristaRepository.save(any())).thenReturn(motoristaMock);

    Motorista result = motoristaService.insert(dto);

    assertEquals(motoristaMock, result);
  }

  @Test
  public void testAtualizar() {
    Motorista motoristaMock = new Motorista();
    when(motoristaRepository.save(any())).thenReturn(motoristaMock);

    Motorista result = motoristaService.atualizar("id", motoristaMock);

    assertEquals(motoristaMock, result);
  }

  @Test
  public void testAvaliar() {
    Motorista motoristaMock = new Motorista();
    motoristaMock.setCorridasAvaliadas(10);
    motoristaMock.setAvaliacao(4.0f);

    when(motoristaRepository.findById("id")).thenReturn(Optional.of(motoristaMock));
    when(motoristaRepository.save(any())).thenReturn(motoristaMock);

    Motorista result = motoristaService.avaliar("id", 5.0f);

    assertEquals(4.090909, result.getAvaliacao(), 0.000001);
  }

  @Test
  public void testConsultarMotoristaPeloCPF() {
    Motorista motoristaMock = new Motorista();
    when(motoristaRepository.findByCpf("12345678901")).thenReturn(Optional.of(motoristaMock));

    MotoristaDTO result = motoristaService.consultarMotoristaPeloCPF("12345678901");

    assertEquals(MotoristaDTO.fromEntity(motoristaMock), result);
  }

  @Test
  public void testConsultar() {
    Page<Motorista> page = new PageImpl<>(Collections.singletonList(new Motorista()));
    when(motoristaRepository.findAll(any(PageRequest.class))).thenReturn(page);

    Page<Motorista> result = motoristaService.consultar(PageRequest.of(0, 10));

    assertEquals(page, result);
  }

  @Test
  public void testConsultarPorId() {
    Motorista motoristaMock = new Motorista();
    when(motoristaRepository.findById("id")).thenReturn(Optional.of(motoristaMock));

    MotoristaDTO result = motoristaService.consultarPorId("id");

    assertEquals(MotoristaDTO.fromEntity(motoristaMock), result);
  }

  @Test
  public void testConsultarPorIdNotFound() {
    when(motoristaRepository.findById("id")).thenReturn(Optional.empty());

    assertThrows(NotFoundError.class, () -> {
      motoristaService.consultarPorId("id");
    });
  }

  @Test
  public void testExcluir() {
    doNothing().when(motoristaRepository).deleteById("id");

    motoristaService.excluir("id");

    verify(motoristaRepository, times(1)).deleteById("id");
  }

  @Test
  public void testFindByCpf() {
    Motorista motoristaMock = new Motorista();
    when(motoristaRepository.findByCpf("12345678901")).thenReturn(Optional.of(motoristaMock));

    Motorista result = motoristaService.findByCpf("12345678901");

    assertEquals(motoristaMock, result);
  }

  @Test
  public void testCalcularMedia() {
    Float result = motoristaService.calcularMedia(10, 5.0f, 4.0f);

    assertEquals(4.090909, result, 0.000001);
  }
}
