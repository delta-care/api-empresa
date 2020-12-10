package xyz.deltacare.empresa.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import xyz.deltacare.empresa.domain.Empresa;

import java.util.Optional;
import java.util.UUID;

@Service
public class EmpresaServiceImpl2 implements EmpresaService {
    @Override
    public Empresa criar(Empresa any) {
        return null;
    }

    @Override
    public Optional<Empresa> getById(UUID id) {
        return Optional.empty();
    }

    @Override
    public void excluir(Empresa empresa) {

    }

    @Override
    public Empresa atualizar(Empresa empresa) {
        return null;
    }

    @Override
    public Page<Empresa> obter(Empresa filter, Pageable pageRequest) {
        return null;
    }
}
