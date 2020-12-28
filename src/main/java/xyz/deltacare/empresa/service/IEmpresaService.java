package xyz.deltacare.empresa.service;

import xyz.deltacare.empresa.dto.EmpresaDto;

import java.util.List;

public interface IEmpresaService {
    EmpresaDto create(EmpresaDto any);
    EmpresaDto findById(Long id);
    List<EmpresaDto> findAll();
    void delete(Long id);
}
