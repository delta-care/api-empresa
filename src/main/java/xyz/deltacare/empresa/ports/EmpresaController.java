package xyz.deltacare.empresa.ports;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import xyz.deltacare.empresa.application.EmpresaService;
import xyz.deltacare.empresa.domain.Empresa;
import xyz.deltacare.empresa.ports.dto.EmpresaDTO;

@RestController
@RequestMapping("/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService empresaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmpresaDTO criar(@RequestBody EmpresaDTO empresaDTO) {
        Empresa empresa = Empresa.builder().cnpj(empresaDTO.getCnpj()).nome(empresaDTO.getNome()).build();
        empresa = empresaService.save(empresa);
        return EmpresaDTO.builder().id(empresa.getId()).nome(empresa.getNome()).cnpj(empresa.getCnpj()).build();
    }
}