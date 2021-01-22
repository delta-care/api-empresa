package xyz.deltacare.empresa.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import xyz.deltacare.empresa.dto.EmpresaDto;
import xyz.deltacare.empresa.service.EmpresaService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/empresas")
public class EmpresaController implements EmpresaControllerDocs {

    private final EmpresaService service;

    public EmpresaController(EmpresaService service) {
        this.service = service;
    }

    @Value( "${pool.size.core}" )
    private String core;

    @Value( "${pool.size.max}" )
    private String max;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmpresaDto create(@RequestBody @Valid EmpresaDto empresaDto) {
        return service.create(empresaDto);
    }

    @GetMapping("/{id}")
    public EmpresaDto findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping
    public String findAll() {
        return core + " " + max;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PutMapping("/{id}")
    public EmpresaDto updateById(
            @PathVariable Long id,
            @RequestBody @Valid EmpresaDto empresaDto) {
        return service.updateById(id, empresaDto);
    }

}