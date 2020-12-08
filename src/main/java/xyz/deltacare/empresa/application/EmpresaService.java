package xyz.deltacare.empresa.application;

import xyz.deltacare.empresa.domain.Empresa;

import java.util.Optional;
import java.util.UUID;

public interface EmpresaService {
    Empresa criar(Empresa any);
    Optional<Empresa> getById(UUID id);
    void excluir(Empresa empresa);
    Empresa atualizar(Empresa empresa);
}
