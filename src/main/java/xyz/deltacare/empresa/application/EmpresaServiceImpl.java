package xyz.deltacare.empresa.application;

import org.springframework.stereotype.Service;
import xyz.deltacare.empresa.domain.Empresa;
import xyz.deltacare.empresa.ports.out.EmpresaRepository;

@Service
public class EmpresaServiceImpl implements EmpresaService {

    private EmpresaRepository repository;

    public EmpresaServiceImpl(EmpresaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Empresa save(Empresa empresa) {
        return repository.save(empresa);
    }

}
