package xyz.deltacare.empresa.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import xyz.deltacare.empresa.dto.EmpresaDto;
import xyz.deltacare.empresa.service.EmpresaService;
import xyz.deltacare.empresa.controller.docs.EmpresaControllerDocs;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/empresas")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EmpresaController implements EmpresaControllerDocs {

    private final EmpresaService service;

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
    public List<EmpresaDto> findAll() {
        return service.findAll();
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