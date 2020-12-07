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
    private final ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmpresaDTO criar(@RequestBody EmpresaDTO empresaDTO) {
        Empresa empresa = modelMapper.map(empresaDTO, Empresa.class);
        empresa = empresaService.save(empresa);
        return modelMapper.map(empresa, EmpresaDTO.class);
    }
}