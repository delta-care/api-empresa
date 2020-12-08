package xyz.deltacare.empresa.ports.in;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import xyz.deltacare.empresa.application.EmpresaService;
import xyz.deltacare.empresa.domain.Empresa;
import xyz.deltacare.empresa.domain.exception.EmpresaException;
import xyz.deltacare.empresa.ports.in.dto.EmpresaDTO;
import xyz.deltacare.empresa.ports.in.exception.ApiErrors;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService empresaService;
    private final ModelMapper modelMapper;

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

    @PatchMapping("{id}")
    public EmpresaDTO atualizar(@PathVariable UUID id, @RequestBody EmpresaDTO empresaDTO) {
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