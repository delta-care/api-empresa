package xyz.deltacare.empresa.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.deltacare.empresa.dto.EmpresaDto;
import xyz.deltacare.empresa.domain.Empresa;

import java.util.Optional;
import java.util.UUID;

public interface IEmpresaService {
    EmpresaDto criar(EmpresaDto any);
}
