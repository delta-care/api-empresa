package xyz.deltacare.empresa.controller.docs;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import xyz.deltacare.empresa.dto.EmpresaDto;

@Api("Gestão de Empresas")
public interface EmpresaControllerDocs {

    @ApiOperation(value = "Criação de empresa")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Empresa criada com sucesso."),
            @ApiResponse(code = 400, message = "Mensagem de erro a ser definida.")
    })
    EmpresaDto criar(EmpresaDto empresaDto);
}