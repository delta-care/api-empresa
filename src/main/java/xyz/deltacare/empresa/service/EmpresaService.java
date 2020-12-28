package xyz.deltacare.empresa.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.deltacare.empresa.dto.EmpresaDto;
import xyz.deltacare.empresa.mapper.EmpresaMapper;
import xyz.deltacare.empresa.domain.Empresa;
import xyz.deltacare.empresa.repository.IEmpresaRepository;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service()
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EmpresaService implements IEmpresaService {

    private final IEmpresaRepository repository;
    private static final EmpresaMapper mapper = EmpresaMapper.INSTANCE;

    @Override
    public EmpresaDto create(EmpresaDto empresaDto) {
        repository.findByCnpj(empresaDto.getCnpj())
                .ifPresent(empresa -> {
                    throw new EntityExistsException(
                            String.format("Empresa com CNPJ %s já existe.", empresaDto.getCnpj()));});
        Empresa empresaASerCriada = mapper.toModel(empresaDto);
        Empresa empresaCriada = repository.save(empresaASerCriada);
        return mapper.toDto(empresaCriada);
    }

    @Override
    public EmpresaDto findById(Long id) {
        Empresa empresaEncontrada = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Empresa com id %s não existe.", id)));
        return mapper.toDto(empresaEncontrada);
    }

    @Override
    public List<EmpresaDto> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

}
