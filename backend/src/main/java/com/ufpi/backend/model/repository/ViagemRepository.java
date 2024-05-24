package com.ufpi.backend.model.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ufpi.backend.model.entity.Viagem;

public interface ViagemRepository extends JpaRepository<Viagem, UUID>, JpaSpecificationExecutor<Viagem> {

  @Query(value = "SELECT v FROM Viagem v inner join Motorista m on m.id = v.motorista_id and m.id = :id", nativeQuery = true)
  List<Viagem> findByMotorista(UUID id);

  @Query(value = "SELECT v FROM Viagem v WHERE v.motorista_id = :id AND v.horaEmbarque != NULL AND v.horaDesembarque = NULL", nativeQuery = true)
  Optional<Viagem> findViagemAtivaByMotorista(UUID id);

  @Query(value = "SELECT v FROM Viagem v WHERE v.passageiro_id = :id AND v.horaEmbarque != NULL AND v.horaDesembarque = NULL", nativeQuery = true)
  Optional<Viagem> findViagemAtivaByPassageiro(UUID id);

  @Query(value = "select case when count(v) > 0 then true else false end from viagem v inner join motorista m on m.id = v.motorista_id and m.id = :id and v.finalizada = false", nativeQuery = true)
  Boolean existsViagemByMotorista(UUID id);

  @Query(value = "select case when count(v) > 0 then true else false end from viagem v inner join passageiro p on p.id = v.passageiro_id and  p.id = :id and v.finalizada = false", nativeQuery = true)
  Boolean existsViagemByPassageiro(UUID id);

  @Query(value = "select case when count(v) > 0 then true else false end from viagem v inner join veiculo m on m.placa = v.veiculo_placa and m.placa = :placa and v.finalizada = false", nativeQuery = true)
  Boolean existsViagemByVeiculo(String placa);

}
