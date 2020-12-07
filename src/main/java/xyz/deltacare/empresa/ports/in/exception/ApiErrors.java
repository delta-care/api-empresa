package xyz.deltacare.empresa.ports.in.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;
import xyz.deltacare.empresa.domain.exception.EmpresaException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApiErrors {

    @Getter
    private final List<String> errors;

    public ApiErrors(BindingResult bindingResult) {
        this.errors = new ArrayList<>();
        bindingResult.getAllErrors().forEach(error -> this.errors.add(error.getDefaultMessage()));
    }

    public ApiErrors(EmpresaException ex) {
        this.errors = Collections.singletonList(ex.getMessage());
    }

}