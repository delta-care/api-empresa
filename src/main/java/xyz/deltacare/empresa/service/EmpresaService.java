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

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EmpresaService {

    private final IEmpresaRepository repository;
    private static final EmpresaMapper mapper = EmpresaMapper.INSTANCE;

    public EmpresaDto create(EmpresaDto empresaDto) {
        verifyIfExists(empresaDto);
        Empresa empresaASerCriada = mapper.toModel(empresaDto);
        Empresa empresaCriada = repository.save(empresaASerCriada);
        return mapper.toDto(empresaCriada);
    }

    public EmpresaDto findById(Long id) {
        Empresa empresaEncontrada = verifyAndGet(id);
        return mapper.toDto(empresaEncontrada);
    }

    public List<EmpresaDto> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public EmpresaDto updateById(Long id, EmpresaDto empresaDto) {
        Empresa empresaEncontrada = verifyAndGet(id);
        Empresa empresaAtualizar = mapper.toModel(empresaDto);
        empresaAtualizar.setCreatedDate(empresaEncontrada.getCreatedDate());
        Empresa empresaAtualizada = repository.save(empresaAtualizar);
        return mapper.toDto(empresaAtualizada);
    }

    public void delete(Long id) {
        verifyAndGet(id);
        repository.deleteById(id);
    }

    private void verifyIfExists(EmpresaDto empresaDto) {
        repository.findByCnpj(empresaDto.getCnpj())
                .ifPresent(empresa -> {
                    throw new EntityExistsException(
                            String.format("Empresa com CNPJ %s já existe.", empresaDto.getCnpj()));});
    }

    private Empresa verifyAndGet(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Empresa com id %s não existe.", id)));
    }

}
