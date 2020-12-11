package xyz.deltacare.empresa.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import xyz.deltacare.empresa.dto.EmpresaDto;
import xyz.deltacare.empresa.mapper.EmpresaMapper;
import xyz.deltacare.empresa.domain.Empresa;
import xyz.deltacare.empresa.exception.EmpresaExistenteException;
import xyz.deltacare.empresa.repository.EmpresaRepository;

import java.util.Optional;
import java.util.UUID;

@Service()
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EmpresaService implements IEmpresaService {

    private final EmpresaRepository repository;
    private final static EmpresaMapper empresaMapper = EmpresaMapper.INSTANCE;

    @Override
    public EmpresaDto criar(EmpresaDto empresaDto) {
        if (repository.existsByCnpj(empresaDto.getCnpj())) {
            throw new EmpresaExistenteException(empresaDto.getCnpj());
        }
        Empresa empresaASerCriada = empresaMapper.toModel(empresaDto);
        Empresa empresaCriada = repository.save(empresaASerCriada);
        return empresaMapper.toDto(empresaCriada);
    }

}
