package xyz.deltacare.empresa.builder;

import lombok.Builder;
import xyz.deltacare.empresa.dto.BeneficiarioDto;
import xyz.deltacare.empresa.dto.EmpresaDto;
import xyz.deltacare.empresa.dto.ProdutoDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
public class EmpresaDtoBuilder {

    private final String id;

    @Builder.Default
    private final String cnpj = "38067491000160";

    @Builder.Default
    private final String nome = "Bruno e Oliver Contábil ME";

    @Builder.Default
    private final String email = "a@a.com";

    @Builder.Default
    private final String logradouro = "Av. das Américas, 1024";

    @Builder.Default
    private final String bairro = "Barra da Tijuca";

    @Builder.Default
    private final String uf = "RJ";

    @Builder.Default
    private final String cep = "22333444";

    public EmpresaDto buildEmpresaDto() {

        List<ProdutoDto> produtoDtos = new ArrayList<>();
        produtoDtos.add(ProdutoDto.builder()
                .plano("Essencial")
                .subplano("Ambulatorial")
                .dataInicioVigencia(LocalDate.of(2020, 1,1))
                .dataFimVigencia(null)
                .build());
        produtoDtos.add(ProdutoDto.builder()
                .plano("essencial")
                .subplano("Hospitalar")
                .dataInicioVigencia(LocalDate.of(2020,1,1))
                .dataFimVigencia(null)
                .build());

        List<BeneficiarioDto> beneficiarioDtos = new ArrayList<>();
        beneficiarioDtos.add(BeneficiarioDto.builder()
                .plano("Essencial")
                .subplano("Ambulatorial")
                .cpf("11122233344")
                .nome("José da Silva")
                .build());
        beneficiarioDtos.add(BeneficiarioDto.builder()
                .plano("Essencial")
                .subplano("Ambulatorial")
                .cpf("22233344455")
                .nome("Roberto Alencar")
                .build());
        beneficiarioDtos.add(BeneficiarioDto.builder()
                .plano("Essencial")
                .subplano("Hospitalar")
                .cpf("33344455566")
                .nome("João da Silva")
                .build());
        beneficiarioDtos.add(BeneficiarioDto.builder()
                .plano("Essencial")
                .subplano("Hospitalar")
                .cpf("44455566677")
                .nome("João da Silva")
                .build());

        return new EmpresaDto(
                id,
                cnpj,
                nome,
                email,
                logradouro,
                bairro,
                uf,
                cep,
                produtoDtos,
                beneficiarioDtos
        );

    }
}
