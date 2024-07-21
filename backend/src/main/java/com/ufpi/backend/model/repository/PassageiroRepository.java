package com.ufpi.backend.model.repository;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ufpi.backend.model.entity.Passageiro;
import com.ufpi.backend.model.filter.PassageiroFiltroDTO;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public interface PassageiroRepository extends JpaRepository<Passageiro, String>, JpaSpecificationExecutor<Passageiro> {

  public Optional<Passageiro> findByCpf(String cpf);

  public static Specification<Passageiro> especificar(PassageiroFiltroDTO filter) {
    Specification<Passageiro> spec = Specification.where(null);
    spec = spec.and(distinct());

    if (isNotBlank(filter.getCpf())) {
      spec = spec.and(porCpf(filter.getCpf()));
    }

    if (isNotBlank(filter.getNome())) {
      spec = spec.and(porNome(filter.getNome()));
    }
    return spec;
  }

  public static PageRequest paginar(PassageiroFiltroDTO filter) {
    final String ORDER_PROPERTY = "nome";
    final Integer DEFAULT_PAGE = 0;
    final Integer DEFAULT_SIZE = 10;

    if (filter.getPage() == null)
      filter.setPage(DEFAULT_PAGE);
    if (filter.getSize() == null)
      filter.setSize(DEFAULT_SIZE);
    return PageRequest.of(filter.getPage(), filter.getSize(), Sort.by(Sort.Direction.ASC, ORDER_PROPERTY));
  }

  public static Specification<Passageiro> distinct() {
    return (Root<Passageiro> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
      query.distinct(true);
      return null;
    };
  }

  public static Specification<Passageiro> porCpf(String cpf) {
    return (Root<Passageiro> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> cb.equal(root.get("cpf"), cpf);
  }

  public static Specification<Passageiro> porNome(String nome) {
    return (Root<Passageiro> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> cb.like(cb.upper(root.get("nome")),
        "%" + nome.toUpperCase() + "%");
  }
}
