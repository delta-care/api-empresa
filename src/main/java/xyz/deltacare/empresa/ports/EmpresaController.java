package xyz.deltacare.empresa.ports;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.deltacare.empresa.ports.dto.EmpresaDTO;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @PostMapping
    public EmpresaDTO criar() {
        return null;
    }
}