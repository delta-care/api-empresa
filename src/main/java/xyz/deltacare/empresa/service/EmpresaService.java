package xyz.deltacare.empresa.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.deltacare.empresa.dto.EmpresaDto;
import xyz.deltacare.empresa.mapper.EmpresaMapper;
import xyz.deltacare.empresa.domain.Empresa;
import xyz.deltacare.empresa.repository.EmpresaRepository;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@Service()
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EmpresaService implements IEmpresaService {

    private final EmpresaRepository repository;
    private static final EmpresaMapper empresaMapper = EmpresaMapper.INSTANCE;

    @Override
    public EmpresaDto create(EmpresaDto empresaDto) {
        repository.findByCnpj(empresaDto.getCnpj())
                .ifPresent(empresa -> {
                    throw new EntityExistsException(
                            String.format("Empresa com CNPJ %s já existe.", empresaDto.getCnpj()));});
        Empresa empresaASerCriada = empresaMapper.toModel(empresaDto);
        Empresa empresaCriada = repository.save(empresaASerCriada);
        return empresaMapper.toDto(empresaCriada);
    }

    @Override
    public EmpresaDto findById(Long id) {
        Empresa empresaEncontrada = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Empresa com id %s não existe.", id)));
        return empresaMapper.toDto(empresaEncontrada);
    }

}
