package xyz.deltacare.empresa.ports;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import xyz.deltacare.empresa.ports.dto.EmpresaDTO;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmpresaDTO criar() {
        return null;
    }
}