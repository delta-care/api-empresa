package xyz.deltacare.empresa.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import xyz.deltacare.empresa.builder.EmpresaDtoBuilder;
import xyz.deltacare.empresa.domain.Empresa;
import xyz.deltacare.empresa.dto.EmpresaDto;
import xyz.deltacare.empresa.mapper.EmpresaMapper;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class EmpresaRepositoryTest {

    @Autowired private EmpresaRepository empresaRepository;
    @Autowired private ProdutoRepository produtoRepository;
    @Autowired private BeneficiarioRepository beneficiarioRepository;

    private Empresa empresaEnviada;
    private final EmpresaMapper empresaMapper = EmpresaMapper.INSTANCE;

    @BeforeEach
    void setUp() {
        EmpresaDto empresaDtoEnviada = EmpresaDtoBuilder.builder().build().buildEmpresaDto();
        empresaEnviada = empresaMapper.toModel(empresaDtoEnviada);
    }

    @Test
    @DisplayName("Deve tentar pesquisar empresa por CNPJ existente e retornar dados iguais.")
    void criarEmpresaComCnpjExistente() {

        // given | cenário
        empresaRepository.save(empresaEnviada);
        produtoRepository.save(new ArrayList<>(empresaEnviada.getProdutos()).get(0));
        beneficiarioRepository.save(new ArrayList<>(empresaEnviada.getBeneficiarios()).get(0));

        // when | execução
        Optional<Empresa> empresaEncontrada = empresaRepository.findByCnpj(empresaEnviada.getCnpj());

        // then | verificação
        assertThat(empresaEncontrada).isEqualTo(Optional.of(empresaEnviada));
    }

    @Test
    @DisplayName("Deve tentar pesquisar empresa por CNPJ inexistente e retornar vazio.")
    void criarEmpresaComCnpjInexistente() {

        // given | cenário
        String cnpj = "38.067.491/0001-60";

        // when | execução
        Optional<Empresa> empresa = empresaRepository.findByCnpj(cnpj);

        // then | verificação
        assertThat(empresa).isNotPresent();
    }

    @Test
    @DisplayName("Deve tentar pesquisar empresa por id existente e retornar dados iguais.")
    void criarEmpresaComIdExistente() {

        // given | cenário
        empresaRepository.save(empresaEnviada);
        produtoRepository.save(new ArrayList<>(empresaEnviada.getProdutos()).get(0));
        beneficiarioRepository.save(new ArrayList<>(empresaEnviada.getBeneficiarios()).get(0));

        // when | execução
        Optional<Empresa> empresaEncontrada = empresaRepository.findById(empresaEnviada.getId());

        // then | verificação
        assertThat(empresaEncontrada).isEqualTo(Optional.of(empresaEnviada));
    }

    @Test
    @DisplayName("Deve tentar pesquisar empresa por id existente e retornar dados iguais.")
    void criarEmpresaComIdInexistente() {

        // given | cenário
        String id = "637833";

        // when | execução
        Optional<Empresa> empresa = empresaRepository.findByCnpj(id);

        // then | verificação
        assertThat(empresa).isNotPresent();
    }

}
