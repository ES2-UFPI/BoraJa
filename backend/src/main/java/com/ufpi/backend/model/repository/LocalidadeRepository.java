package com.ufpi.backend.model.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.ufpi.backend.model.entity.Localidade;

public interface LocalidadeRepository extends JpaRepository<Localidade, UUID>, JpaSpecificationExecutor<Localidade> {

}
