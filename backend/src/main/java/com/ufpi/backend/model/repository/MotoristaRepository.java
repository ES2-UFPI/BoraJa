package com.ufpi.backend.model.repository;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ufpi.backend.model.entity.Motorista;
import com.ufpi.backend.model.filter.MotoristaFiltroDTO;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public interface MotoristaRepository extends JpaRepository<Motorista, String>, JpaSpecificationExecutor<Motorista> {

  @Query(value = "select * from motorista where cpf = :cpf", nativeQuery = true)
  public Optional<Motorista> findByCpf(String cpf);

  public static Specification<Motorista> especificar(MotoristaFiltroDTO filter) {
    Specification<Motorista> spec = Specification.where(null);
    spec = spec.and(distinct());

    if (isNotBlank(filter.getCpf())) {
      spec = spec.and(porCpf(filter.getCpf()));
    }

    if (isNotBlank(filter.getNome())) {
      spec = spec.and(porNome(filter.getNome()));
    }

    if (isNotBlank(filter.getUsername())) {
      spec = spec.and(porUsername(filter.getUsername()));
    }
    return spec;
  }

  public static PageRequest paginar(MotoristaFiltroDTO filter) {
    final String ORDER_PROPERTY = "nome";
    final Integer DEFAULT_PAGE = 0;
    final Integer DEFAULT_SIZE = 10;

    if (filter.getPage() == null)
      filter.setPage(DEFAULT_PAGE);
    if (filter.getSize() == null)
      filter.setSize(DEFAULT_SIZE);
    return PageRequest.of(filter.getPage(), filter.getSize(), Sort.by(Sort.Direction.ASC, ORDER_PROPERTY));
  }

  public static Specification<Motorista> distinct() {
    return (Root<Motorista> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
      query.distinct(true);
      return null;
    };
  }

  public static Specification<Motorista> porCpf(String cpf) {
    return (Root<Motorista> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> cb.equal(root.get("cpf"), cpf);
  }

  public static Specification<Motorista> porUsername(String username) {
    return (Root<Motorista> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> cb.equal(root.get("username"),
        username);
  }

  public static Specification<Motorista> porNome(String nome) {
    return (Root<Motorista> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> cb.like(cb.upper(root.get("nome")),
        "%" + nome.toUpperCase() + "%");
  }
}
