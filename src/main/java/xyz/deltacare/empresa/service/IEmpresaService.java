package xyz.deltacare.empresa.service;

import xyz.deltacare.empresa.dto.EmpresaDto;

import java.util.UUID;

public interface IEmpresaService {
    EmpresaDto create(EmpresaDto any);
    EmpresaDto findById(UUID id);
}
