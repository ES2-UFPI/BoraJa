package com.ufpi.backend.model.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ufpi.backend.model.entity.Vaga;
import com.ufpi.backend.model.entity.Viagem;

public interface VagaRepository extends JpaRepository<Vaga, UUID>, JpaSpecificationExecutor<Vaga> {

  @Query(value = "SELECT v FROM Vaga v WHERE v.viagem_id = :id", nativeQuery = true)
  List<Vaga> findByViagem(UUID id);

  @Query(value = "SELECT count(v) > 0 FROM Vaga v inner join Viagem ve on v.viagem_id = ve.id AND v.viagem_id = :viagemId AND ve.finalizada = false AND ve.hora_saida != NULL AND ve.hora_chegada = NULL AND v.passageiro_id = :passageiroId", nativeQuery = true)
  Boolean existsViagemAtivaByPassageiro(String passageiroId, UUID viagemId);

}
