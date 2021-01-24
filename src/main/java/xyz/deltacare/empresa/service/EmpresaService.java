package xyz.deltacare.empresa.service;

import xyz.deltacare.empresa.dto.EmpresaDto;

import java.util.List;

public interface EmpresaService {
    EmpresaDto create(EmpresaDto empresaDto);

    EmpresaDto findById(Long id);

    List<EmpresaDto> findAll();

    EmpresaDto updateById(Long id, EmpresaDto empresaDto);

    void delete(Long id);
}
