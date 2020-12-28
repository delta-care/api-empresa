package xyz.deltacare.empresa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.deltacare.empresa.domain.Empresa;

import java.util.Optional;

public interface IEmpresaRepository extends JpaRepository<Empresa, Long> {
    Optional<Empresa> findByCnpj(String cnpj);
}
