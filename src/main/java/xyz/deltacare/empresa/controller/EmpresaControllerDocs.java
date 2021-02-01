package xyz.deltacare.empresa.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.deltacare.empresa.dto.EmpresaDto;

import java.util.List;

@Api("Gestão de Empresas")
public interface EmpresaControllerDocs {

    @ApiOperation(value = "Criar empresa")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Empresa criada com sucesso."),
            @ApiResponse(code = 400, message = "Mensagem de erro a ser definida.")
    })
    EmpresaDto criar(EmpresaDto empresaDto);

    @ApiOperation(value = "Pesquisar empresas")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Empresa(s) encontrada(s) com sucesso.")
    })
    List<EmpresaDto> pesquisar(
            @RequestParam(value="page", required = false, defaultValue = "0") int page,
            @RequestParam(value="limit", required = false, defaultValue = "10") int limit,
            @RequestParam(value="codigo", required = false, defaultValue = "") String id,
            @RequestParam(value="nome", required = false, defaultValue = "") String nome,
            @RequestParam(value="cnpj", required = false, defaultValue = "") String cnpj
    );

    @ApiOperation(value = "Atualizar empresa por código")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Empresa atualizada com sucesso."),
            @ApiResponse(code = 404, message = "Empresa não encontrada."),
            @ApiResponse(code = 400, message = "Campo obrigatório não informado.")
    })
    EmpresaDto atualizar(EmpresaDto empresaDto);

}
