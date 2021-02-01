package xyz.deltacare.empresa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaDto implements Serializable {

    private String id;

    @NotNull
    @NotEmpty
    @Size(max = 255)
    private String cnpj;

    @NotNull
    @NotEmpty
    @Size(max = 255)
    private String nome;

    @NotNull
    @NotEmpty
    @Email
    private String email;

    @NotNull
    @NotEmpty
    @Size(max = 255)
    private String logradouro;

    @NotNull
    @NotEmpty
    @Size(max = 255)
    private String bairro;

    @NotNull
    @NotEmpty
    @Size(max = 255)
    private String uf;

    @NotNull
    @NotEmpty
    @Size(max = 255)
    private String cep;

    @NotNull
    @NotEmpty
    @JsonProperty("produtos")
    private List<ProdutoDto> produtos;

    @NotNull
    @NotEmpty
    @JsonProperty("beneficiarios")
    private List<BeneficiarioDto> beneficiarios;

}