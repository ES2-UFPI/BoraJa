package com.ufpi.backend;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.ufpi.backend.model.dto.viagem.ViagemCreateDTO;
import com.ufpi.backend.model.dto.viagem.ViagemDTO;
import com.ufpi.backend.model.entity.Vaga;
import com.ufpi.backend.model.entity.Viagem;
import com.ufpi.backend.model.enums.StatusViagem;
import com.ufpi.backend.model.filter.FiltroViagemDTO;
import com.ufpi.backend.model.repository.ViagemRepository;
import com.ufpi.backend.service.ViagemService;
import com.ufpi.backend.model.repository.VagaRepository;

@SpringBootTest
public class ViagemServiceTest {

  @Mock
  private ViagemRepository viagemRepository;

  @Mock
  private VagaRepository vagaRepository;

  @InjectMocks
  private ViagemService viagemService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testInsert() {
    ViagemCreateDTO dto = new ViagemCreateDTO();
    Viagem viagem = new Viagem();
    when(viagemRepository.save(any(Viagem.class))).thenReturn(viagem);

    Viagem result = viagemService.insert(dto);

    assertThat(result).isEqualTo(viagem);
    verify(viagemRepository).save(any(Viagem.class));
  }

  @Test
  void testExistsViagemAtivaByPassageiro() {
    String username = "testUser";
    UUID viagemId = UUID.randomUUID();
    when(vagaRepository.existsViagemAtivaByPassageiro(username, viagemId)).thenReturn(true);

    Boolean result = viagemService.existsViagemAtivaByPassageiro(username, viagemId);

    assertThat(result).isTrue();
  }

  @Test
  void testIngressar() {
    UUID viagemId = UUID.randomUUID();
    String passageiroUsername = "passageiro";
    Vaga vaga = new Vaga();
    when(vagaRepository.existsViagemAtivaByPassageiro(passageiroUsername, viagemId)).thenReturn(false);
    when(vagaRepository.save(any(Vaga.class))).thenReturn(vaga);

    Vaga result = viagemService.ingressar(viagemId, passageiroUsername);

    assertThat(result).isEqualTo(vaga);
    verify(vagaRepository).save(any(Vaga.class));
  }

  @Test
  void testBuscarVagas() {
    UUID viagemId = UUID.randomUUID();
    Vaga vaga = new Vaga();
    when(vagaRepository.findByViagem(viagemId)).thenReturn(List.of(vaga));

    List<Vaga> result = viagemService.buscarVagas(viagemId);

    assertThat(result).hasSize(1);
    assertThat(result.get(0)).isEqualTo(vaga);
  }
}