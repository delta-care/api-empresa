package xyz.deltacare.empresa.service;

import org.springframework.data.domain.Pageable;
import xyz.deltacare.empresa.dto.EmpresaDto;

import java.util.List;

public interface EmpresaService {
    EmpresaDto criar(EmpresaDto empresaDto);
    List<EmpresaDto> pesquisar(Pageable pageable, String id, String cnpj, String nome);
    EmpresaDto atualizar(EmpresaDto empresaDto);
}
