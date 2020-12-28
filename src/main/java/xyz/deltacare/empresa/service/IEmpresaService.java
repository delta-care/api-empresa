package xyz.deltacare.empresa.service;

import xyz.deltacare.empresa.dto.EmpresaDto;

public interface IEmpresaService {
    EmpresaDto create(EmpresaDto any);
    EmpresaDto findById(Long id);
}
