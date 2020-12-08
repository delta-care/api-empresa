package xyz.deltacare.empresa.application;

import org.springframework.stereotype.Service;
import xyz.deltacare.empresa.domain.Empresa;
import xyz.deltacare.empresa.domain.exception.EmpresaException;
import xyz.deltacare.empresa.ports.out.EmpresaRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class EmpresaServiceImpl implements EmpresaService {

    private final EmpresaRepository repository;

    public EmpresaServiceImpl(EmpresaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Empresa criar(Empresa empresa) {
        if (repository.existsByCnpj(empresa.getCnpj())) {
            throw new EmpresaException("Empresa já cadastrada.");
        }
        return repository.save(empresa);
    }

    @Override
    public Optional<Empresa> getById(UUID id) {
        return this.repository.findById(id);
    }

    @Override
    public void excluir(Empresa empresa) {
        if ((empresa == null) || (empresa.getId() == null))
            throw new IllegalArgumentException("Id da empresa inexistente.");
        this.repository.delete(empresa);
    }

    @Override
    public Empresa atualizar(Empresa empresa) {
        if ((empresa == null) || (empresa.getId() == null))
            throw new IllegalArgumentException("Id da empresa inexistente.");
        return this.repository.save(empresa);
    }

}
