package com.ufpi.backend.model.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ufpi.backend.model.entity.Viagem;
import com.ufpi.backend.model.filter.FiltroViagemDTO;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public interface ViagemRepository extends JpaRepository<Viagem, UUID>, JpaSpecificationExecutor<Viagem> {

  @Query(value = "SELECT v FROM Viagem v inner join Motorista m on m.username = v.motorista_id and m.username = :username", nativeQuery = true)
  List<Viagem> findByMotorista(String username);

  @Query(value = "SELECT v FROM Viagem v WHERE v.motorista_id = :username AND v.horaSaida != NULL AND v.horaChegada = NULL", nativeQuery = true)
  Optional<Viagem> findViagemAtivaByMotorista(String username);

  @Query(value = "select case when count(v) > 0 then true else false end from viagem v inner join motorista m on m.username = v.motorista_id and m.username = :username and v.hora_saida != NULL and v.finalizada = false", nativeQuery = true)
  Boolean existsViagemByMotorista(String username);

  @Query(value = "select case when count(v) > 0 then true else false end from viagem v inner join veiculo m on m.placa = v.veiculo_placa and m.placa = :placa and v.hora_saida != NULL and v.finalizada = false", nativeQuery = true)
  Boolean existsViagemByVeiculo(String placa);

  public static Specification<Viagem> especificar(FiltroViagemDTO filter) {
    Specification<Viagem> spec = Specification.where(null);
    spec = spec.and(distinct());
    if (filter.getOrigem() != null) {
      spec = spec.and(porOrigem(filter.getOrigem().getNome()));
    }
    if (filter.getDestino() != null) {
      spec = spec.and(porDestino(filter.getDestino().getNome()));
    }
    if (filter.getMotoristaUsername() != null) {
      spec = spec.and(porMotorista(filter.getMotoristaUsername()));
    }
    if (filter.getVeiculoPlaca() != null) {
      spec = spec.and(porVeiculo(filter.getVeiculoPlaca()));
    }

    return spec;
  }

  public static PageRequest paginar(FiltroViagemDTO filter) {
    final String ORDER_PROPERTY = "dataCadastro";
    final Integer DEFAULT_PAGE = 0;
    final Integer DEFAULT_SIZE = 10;

    if (filter.getPage() == null)
      filter.setPage(DEFAULT_PAGE);
    if (filter.getSize() == null)
      filter.setSize(DEFAULT_SIZE);
    return PageRequest.of(filter.getPage(), filter.getSize(), Sort.by(Sort.Direction.ASC, ORDER_PROPERTY));
  }

  public static Specification<Viagem> distinct() {
    return (Root<Viagem> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
      query.distinct(true);
      return null;
    };
  }

  public static Specification<Viagem> porOrigem(String origem) {
    return (Root<Viagem> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> cb.like(root.get("origem").get("nome"),
        "%" + origem + "%");
  }

  public static Specification<Viagem> porDestino(String destino) {
    return (Root<Viagem> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> cb.like(root.get("destino").get("nome"),
        "%" + destino + "%");
  }

  public static Specification<Viagem> porMotorista(String username) {
    return (Root<Viagem> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> cb.equal(
        root.get("motorista").get("username"),
        username);
  }

  public static Specification<Viagem> porVeiculo(String placa) {
    return (Root<Viagem> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> cb.equal(root.get("veiculo").get("placa"),
        placa);
  }

}
