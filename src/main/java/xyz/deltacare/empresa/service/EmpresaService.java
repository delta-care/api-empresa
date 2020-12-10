package xyz.deltacare.empresa.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.deltacare.empresa.domain.Empresa;

import java.util.Optional;
import java.util.UUID;

public interface EmpresaService {
    Empresa criar(Empresa any);
    Optional<Empresa> getById(UUID id);
    void excluir(Empresa empresa);
    Empresa atualizar(Empresa empresa);
    Page<Empresa> obter (Empresa filter, Pageable pageRequest);
}
