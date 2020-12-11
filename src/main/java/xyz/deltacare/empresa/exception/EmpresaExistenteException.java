package xyz.deltacare.empresa.exception;

import javax.persistence.EntityExistsException;

public class EmpresaExistenteException extends EntityExistsException {
    public EmpresaExistenteException(String cnpj) {
        super(String.format("Empresa com CNPJ %s já existe.", cnpj));
    }
}
