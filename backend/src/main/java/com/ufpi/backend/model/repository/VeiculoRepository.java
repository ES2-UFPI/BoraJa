package com.ufpi.backend.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ufpi.backend.model.entity.Veiculo;

public interface VeiculoRepository extends JpaRepository<Veiculo, String>, JpaSpecificationExecutor<Veiculo> {

  public Optional<Veiculo> findByPlaca(String placa);

  public void deleteByPlaca(String placa);

}
