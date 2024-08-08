package com.ufpi.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
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
import com.ufpi.backend.model.dto.passageiro.PassageiroCreateDTO;
import com.ufpi.backend.model.dto.passageiro.PassageiroDTO;
import com.ufpi.backend.model.entity.Passageiro;
import com.ufpi.backend.model.repository.PassageiroRepository;
import com.ufpi.backend.service.PassageiroService;

@ExtendWith(MockitoExtension.class)
public class PassageiroServiceTest {

  @Mock
  private PassageiroRepository passageiroRepository;

  @InjectMocks
  private PassageiroService passageiroService;

  @Test
  public void testFindById() {
    Passageiro passageiroMock = new Passageiro();
    when(passageiroRepository.findById("username")).thenReturn(Optional.of(passageiroMock));

    Passageiro result = passageiroService.findById("username");

    assertEquals(passageiroMock, result);
  }

  @Test
  public void testFindByIdNotFound() {
    when(passageiroRepository.findById("username")).thenReturn(Optional.empty());

    assertThrows(NotFoundError.class, () -> {
      passageiroService.findById("username");
    });
  }

  @Test
  public void testInsert() {
    PassageiroCreateDTO dto = new PassageiroCreateDTO();
    Passageiro passageiroMock = new Passageiro();
    when(passageiroRepository.save(any())).thenReturn(passageiroMock);

    Passageiro result = passageiroService.insert(dto);

    assertEquals(passageiroMock, result);
  }

  @Test
  public void testAtualizar() {
    Passageiro passageiroMock = new Passageiro();
    when(passageiroRepository.save(any())).thenReturn(passageiroMock);

    Passageiro result = passageiroService.atualizar("id", passageiroMock);

    assertEquals(passageiroMock, result);
  }

  @Test
  public void testConsultarPassageiroPeloCPF() {
    Passageiro passageiroMock = new Passageiro();
    when(passageiroRepository.findByCpf("12345678901")).thenReturn(Optional.of(passageiroMock));

    PassageiroDTO result = passageiroService.consultarPassageiroPeloCPF("12345678901");

    assertEquals(PassageiroDTO.fromEntity(passageiroMock), result);
  }

  @Test
  public void testConsultar() {
    Page<Passageiro> page = new PageImpl<>(Collections.singletonList(new Passageiro()));
    when(passageiroRepository.findAll(any(PageRequest.class))).thenReturn(page);

    Page<Passageiro> result = passageiroService.consultar(PageRequest.of(0, 10));

    assertEquals(page, result);
  }

  @Test
  public void testConsultarPorId() {
    Passageiro passageiroMock = new Passageiro();
    when(passageiroRepository.findById("id")).thenReturn(Optional.of(passageiroMock));

    PassageiroDTO result = passageiroService.consultarPorId("id");

    assertEquals(PassageiroDTO.fromEntity(passageiroMock), result);
  }

  @Test
  public void testConsultarPorIdNotFound() {
    when(passageiroRepository.findById("id")).thenReturn(Optional.empty());

    assertThrows(NotFoundError.class, () -> {
      passageiroService.consultarPorId("id");
    });
  }

  @Test
  public void testExcluir() {
    doNothing().when(passageiroRepository).deleteById("id");

    passageiroService.excluir("id");

    verify(passageiroRepository, times(1)).deleteById("id");
  }

  // @Test
  // public void testFindByCpf() {
  // Passageiro passageiroMock = new Passageiro();
  // when(passageiroRepository.findByCpf("12345678901")).thenReturn(Optional.of(passageiroMock));

  // PassageiroDTO result =
  // passageiroService.consultarPassageiroPeloCPF("12345678901");

  // assertEquals(passageiroMock, PassageiroDTO.toEntity(result));
  // }
}
