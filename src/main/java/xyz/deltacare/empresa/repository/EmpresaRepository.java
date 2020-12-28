package xyz.deltacare.empresa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.deltacare.empresa.domain.Empresa;

import java.util.Optional;
import java.util.UUID;

public interface EmpresaRepository extends JpaRepository<Empresa, UUID> {
    Optional<Empresa> findByCnpj(String cnpj);
}
