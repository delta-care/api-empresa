package xyz.deltacare.empresa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.deltacare.empresa.domain.Empresa;

import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, String> {
    Optional<Empresa> findByCnpj(String cnpj);
    Optional<Empresa> findById(String id);
    @Query( value =
                " select distinct e " +
                " from Empresa e " +
                " inner join fetch e.produtos " +
                " inner join fetch e.beneficiarios " +
                " where 1=1 " +
                "   and e.id like concat(:id,'%') " +
                "   and e.cnpj like concat(:cnpj,'%') " +
                "   and e.nome like concat(:nome,'%')",
            countQuery =
                " select count(e) " +
                " from Empresa e " +
                " inner join e.produtos " +
                " inner join e.beneficiarios" +
                " where 1=1 " +
                "   and e.id like concat(:id,'%') " +
                "   and e.cnpj like concat(:cnpj,'%') " +
                "   and e.nome like concat(:nome,'%')")
    Page<Empresa> findAll(
            Pageable pageable,
            @Param("id") String id,
            @Param("cnpj") String cnpj,
            @Param("nome") String nome);
}
