package xyz.deltacare.empresa.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Empresa {
    private UUID id;
    private String cnpj;
    private String nome;
}
