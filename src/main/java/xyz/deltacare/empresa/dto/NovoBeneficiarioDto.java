package xyz.deltacare.empresa.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class NovoBeneficiarioDto implements Serializable {
    String plano;
    String mes;
    Integer qtd;
}
