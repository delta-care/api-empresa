package xyz.deltacare.empresa.ports.in.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaDTO {

    private UUID id;

    @NotNull
    @NotEmpty
    @Size(max = 255)
    private String cnpj;

    @NotNull
    @NotEmpty
    @Size(max = 255)
    private String nome;

}