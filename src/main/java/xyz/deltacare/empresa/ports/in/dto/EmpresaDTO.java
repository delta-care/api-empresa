package xyz.deltacare.empresa.ports.in.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaDTO {
    private UUID id;
    private String cnpj;
    private String nome;
}