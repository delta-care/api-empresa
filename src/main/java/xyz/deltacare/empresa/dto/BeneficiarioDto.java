package xyz.deltacare.empresa.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiarioDto implements Serializable {

    private Long id;

    @NotNull
    @NotEmpty
    private String plano;

    @NotNull
    @NotEmpty
    private String subplano;

    @NotNull
    @NotEmpty
    private String nome;

    @NotNull
    @NotEmpty
    private String cpf;

}
