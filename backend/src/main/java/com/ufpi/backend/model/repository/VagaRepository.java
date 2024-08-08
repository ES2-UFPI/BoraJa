package com.ufpi.backend.model.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ufpi.backend.model.entity.Vaga;

public interface VagaRepository extends JpaRepository<Vaga, UUID>, JpaSpecificationExecutor<Vaga> {

  @Query(value = "SELECT * FROM vaga v WHERE v.viagem_id = :viagemId", nativeQuery = true)
  List<Vaga> findByViagem(@Param("viagemId") UUID viagemId);

  @Query(value = "SELECT count(v) > 0 FROM Vaga v inner join Viagem ve on v.viagem_id = ve.id AND v.viagem_id = :viagemId AND ve.finalizada = false AND ve.hora_saida != NULL AND ve.hora_chegada = NULL AND v.passageiro_id = :passageiroUsername", nativeQuery = true)
  Boolean existsViagemAtivaByPassageiro(@Param("passageiroUsername") String passageiroUsername,
      @Param("viagemId") UUID viagemId);

}
