package xyz.deltacare.empresa.application;

import xyz.deltacare.empresa.domain.Empresa;

import java.util.Optional;
import java.util.UUID;

public interface EmpresaService {
    Empresa save(Empresa any);

    Optional<Empresa> getById(UUID id);
}
