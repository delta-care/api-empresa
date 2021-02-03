package xyz.deltacare.empresa.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import xyz.deltacare.empresa.dto.EmpresaDto;
import xyz.deltacare.empresa.mapper.EmpresaMapper;
import xyz.deltacare.empresa.domain.Empresa;
import xyz.deltacare.empresa.repository.BeneficiarioRepository;
import xyz.deltacare.empresa.repository.EmpresaRepository;
import xyz.deltacare.empresa.repository.ProdutoRepository;

import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EmpresaServiceSpa implements EmpresaService {

    private final EmpresaRepository repository;
    private final BeneficiarioRepository beneficiarioRepository;
    private final ProdutoRepository produtoRepository;
    private static final EmpresaMapper mapper = EmpresaMapper.INSTANCE;

    @Override
    public EmpresaDto criar(EmpresaDto empresaDto) {
        verificarSeExiste(empresaDto);
        return salvar(empresaDto);
    }

    @Override
    //@Cacheable(value="empresa", key="#root.args")
    public List<EmpresaDto> pesquisar(Pageable pageable, String id, String cnpj, String nome) {
        return repository.findAll(pageable, id, cnpj, nome)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    //@CacheEvict(value="empresa", allEntries = true)
    public EmpresaDto atualizar(EmpresaDto empresaDto) {
        return salvar(empresaDto);
    }

    @Transactional
    protected EmpresaDto salvar(EmpresaDto empresaDto) {
        Empresa empresa = mapper.toModel(empresaDto);
        Empresa empresaSalva = repository.save(empresa);

        salvarBeneficiarios(empresa);
        salvarProdutos(empresa);

        return mapper.toDto(empresaSalva);
    }

    private void salvarBeneficiarios(Empresa empresa) {
        empresa.getBeneficiarios().forEach(beneficiario -> {
            beneficiario.setEmpresa(empresa);
            beneficiarioRepository.save(beneficiario);
        });
    }

    private void salvarProdutos(Empresa empresa) {
        empresa.getProdutos().forEach(produto -> {
            produto.setEmpresa(empresa);
            produtoRepository.save(produto);
        });
    }

    private void verificarSeExiste(EmpresaDto empresaDto) {
        repository.findByCnpj(empresaDto.getCnpj())
                .ifPresent(empresa -> {
                    throw new EntityExistsException(
                            String.format("Empresa com CNPJ %s já existe.", empresaDto.getCnpj()));});
    }

}
