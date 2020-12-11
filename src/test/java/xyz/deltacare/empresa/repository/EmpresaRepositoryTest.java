package xyz.deltacare.empresa.repository;

import org.junit.jupiter.api.*;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import xyz.deltacare.empresa.domain.Empresa;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestMethodOrder(MethodOrderer.DisplayName.class)
class EmpresaRepositoryTest {

    @Autowired
    EmpresaRepository repository;

    @Test
    @DisplayName("CRIAR: Deve criar uma empresa.")
    void criarEmpresaTest() {

        // given | cenário
        Empresa empresa = Empresa.builder()
                .cnpj("123")
                .nome("Golden")
                .build();

        // when | execução
        Empresa empresaSalva = repository.save(empresa);

        // then | verificação
        assertThat(empresaSalva.getId()).isNotNull();

    }

    @Test
    @DisplayName("CRIAR: Deve lançar erro quando tentar criar uma empresa com CNPJ existente.")
    void criarEmpresaComCnpjExistente() {

        // given | cenário
        Empresa empresaExistente = Empresa.builder()
                .cnpj("38.067.491/0001-60")
                .nome("Bruno e Oliver Contábil ME")
                .build();
        repository.save(empresaExistente);

        Empresa empresaEnviada = Empresa.builder()
                .cnpj("38.067.491/0001-60")
                .nome("Bruno e Oliver Contábil ME")
                .build();

        // when | execução
        Throwable exception = Assertions.catchThrowable(() -> repository.saveAndFlush(empresaEnviada));

        // then | verificação
        assertThat(exception).isInstanceOf(DataIntegrityViolationException.class);
    }

}
