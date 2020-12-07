package xyz.deltacare.empresa.ports.out;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.deltacare.empresa.domain.Empresa;

import java.util.UUID;

public interface EmpresaRepository extends JpaRepository<Empresa, UUID> {
    boolean existsByCnpj(String cnpj);
}
