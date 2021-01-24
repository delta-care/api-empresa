package xyz.deltacare.empresa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import xyz.deltacare.empresa.dto.EmpresaDto;
import xyz.deltacare.empresa.domain.Empresa;

@Mapper
public interface EmpresaMapper {
    EmpresaMapper INSTANCE = Mappers.getMapper(EmpresaMapper.class);
    Empresa toModel(EmpresaDto empresaDto);
    EmpresaDto toDto(Empresa empresa);
}
