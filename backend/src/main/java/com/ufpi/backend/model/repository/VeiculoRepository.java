package com.ufpi.backend.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ufpi.backend.model.entity.Veiculo;

import jakarta.transaction.Transactional;

public interface VeiculoRepository extends JpaRepository<Veiculo, String>, JpaSpecificationExecutor<Veiculo> {

  public Optional<Veiculo> findByPlaca(String placa);

  @Transactional
  public void deleteByPlaca(String placa);

  public boolean existsByPlaca(String placa);

  @Query(value = "select case when count(v) > 0 then true else false end from veiculo v inner join motorista m on m.id = v.proprietario_id and m.cpf = :cpf", nativeQuery = true)
  public boolean existsByProprietarioCpf(String cpf);

  public List<Veiculo> findByProprietarioCpf(String cpf);

}
