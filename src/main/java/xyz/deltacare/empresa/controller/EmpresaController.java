package xyz.deltacare.empresa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import xyz.deltacare.empresa.dto.EmpresaDto;
import xyz.deltacare.empresa.service.IEmpresaService;
import xyz.deltacare.empresa.controller.docs.EmpresaControllerDocs;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/empresas")
public class EmpresaController implements EmpresaControllerDocs {

    private final IEmpresaService service;

    @Autowired
    public EmpresaController(@Qualifier("empresaService") IEmpresaService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmpresaDto create(@RequestBody @Valid EmpresaDto empresaDto) {
        return service.create(empresaDto);
    }
}