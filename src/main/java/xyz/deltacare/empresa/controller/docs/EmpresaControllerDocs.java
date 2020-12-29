package xyz.deltacare.empresa.controller.docs;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import xyz.deltacare.empresa.dto.EmpresaDto;

import java.util.List;

@Api("Gestão de Empresas")
public interface EmpresaControllerDocs {

    @ApiOperation(value = "Criar empresa")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Empresa criada com sucesso."),
            @ApiResponse(code = 400, message = "Mensagem de erro a ser definida.")
    })
    EmpresaDto create(EmpresaDto empresaDto);

    @ApiOperation(value = "Pesquisar empresa por ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Empresa encontrada com sucesso."),
            @ApiResponse(code = 404, message = "Empresa não encontrada.")
    })
    EmpresaDto findById(Long id);

    @ApiOperation(value = "Pesquisar empresas")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Empresas encontradas com sucesso.")
    })
    List<EmpresaDto> findAll();

    @ApiOperation(value = "Excluir empresa por ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Empresas excluída com sucesso."),
            @ApiResponse(code = 404, message = "Empresa não encontrada.")
    })
    void delete(Long id);

    @ApiOperation(value = "Atualizar empresa por ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Empresa atualizada com sucesso."),
            @ApiResponse(code = 404, message = "Empresa não encontrada."),
            @ApiResponse(code = 400, message = "Campo obrigatório não informado.")
    })
    EmpresaDto updateById(Long id, EmpresaDto empresaDto);
}
