package xyz.deltacare.empresa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaDto {

    private Long id;

    @NotNull
    @NotEmpty
    private String cnpj;

    @NotNull
    @NotEmpty
    @Size(max = 255)
    private String nome;

}