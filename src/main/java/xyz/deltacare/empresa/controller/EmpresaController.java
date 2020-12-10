package xyz.deltacare.empresa.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import xyz.deltacare.empresa.controller.dto.EmpresaDTO;
import xyz.deltacare.empresa.service.EmpresaService;
import xyz.deltacare.empresa.domain.Empresa;
import xyz.deltacare.empresa.domain.exception.EmpresaException;
import xyz.deltacare.empresa.controller.docs.EmpresaControllerDocs;
import xyz.deltacare.empresa.controller.exception.ApiErrors;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/empresas")
public class EmpresaController implements EmpresaControllerDocs {

    private final EmpresaService empresaService;
    private final ModelMapper modelMapper;

    @Autowired
    public EmpresaController(
            @Qualifier("empresaServiceImpl") EmpresaService empresaService,
            ModelMapper modelMapper) {
        this.empresaService = empresaService;
        this.modelMapper = modelMapper;
    }

    @ApiOperation(value = "Cria uma empresa")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Empresa criada")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmpresaDTO criar(@RequestBody @Valid EmpresaDTO empresaDTO) {
        Empresa empresa = modelMapper.map(empresaDTO, Empresa.class);
        empresa = empresaService.criar(empresa);
        return modelMapper.map(empresa, EmpresaDTO.class);
    }

    @GetMapping("{id}")
    public EmpresaDTO obter(@PathVariable UUID id) {
        return empresaService
                .getById(id)
                .map(empresa -> modelMapper.map(empresa, EmpresaDTO.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping()
    public Page<EmpresaDTO> obter(EmpresaDTO empresaDTO, Pageable pageRequest) {
        Empresa filter = modelMapper.map(empresaDTO, Empresa.class);
        Page<Empresa> result = empresaService.obter(filter, pageRequest);
        List<EmpresaDTO> list = result.getContent()
                .stream()
                .map(entity -> modelMapper.map(entity, EmpresaDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageRequest, result.getTotalElements());
    }

    @PatchMapping("{id}")
    public EmpresaDTO atualizar(@PathVariable UUID id, @RequestBody @Valid EmpresaDTO empresaDTO) {
        return empresaService
                .getById(id)
                .map(empresa -> {
                    empresa.setNome(empresaDTO.getNome());
                    empresa = empresaService.atualizar(empresa);
                    return modelMapper.map(empresa, EmpresaDTO.class);
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable UUID id) {
        Empresa empresa = empresaService
                .getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        empresaService.excluir(empresa);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        return new ApiErrors(bindingResult);
    }

    @ExceptionHandler(EmpresaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleEmpresaException(EmpresaException ex) {
        return new ApiErrors(ex);
    }
}