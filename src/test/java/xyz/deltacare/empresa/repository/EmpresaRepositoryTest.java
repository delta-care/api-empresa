package xyz.deltacare.empresa.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import xyz.deltacare.empresa.domain.Empresa;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class EmpresaRepositoryTest {

    @Autowired
    EmpresaRepository repository;

    @Test
    @DisplayName("Deve pesquisar uma empresa por CNPJ.")
    void criarEmpresaComCnpjExistente() {

        // given | cenário
        Empresa empresaExistente = Empresa.builder()
                .cnpj("38.067.491/0001-60")
                .nome("Bruno e Oliver Contábil ME")
                .produtos(1)
                .coberturas(2)
                .build();
        repository.save(empresaExistente);

        // when | execução
        Optional<Empresa> empresa = repository.findByCnpj(empresaExistente.getCnpj());

        // then | verificação
        assertThat(empresa.get()).isEqualTo(empresaExistente);
    }

    @Test
    @DisplayName("Deve retornar vazio ao pesquisar uma empresa por CNPJ inexistente.")
    void criarEmpresaComCnpjExistente2() {

        // given | cenário
        String cnpj = "38.067.491/0001-60";

        // when | execução
        Optional<Empresa> empresa = repository.findByCnpj(cnpj);

        // then | verificação
        assertThat(empresa).isNotPresent();
    }

}
