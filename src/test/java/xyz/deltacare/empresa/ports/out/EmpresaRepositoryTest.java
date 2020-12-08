package xyz.deltacare.empresa.ports.out;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import xyz.deltacare.empresa.domain.Empresa;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class EmpresaRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    EmpresaRepository repository;

    @Test
    @DisplayName("EXISTIR: Deve retornar verdadeiro quando existir uma empresa com CNPJ informado.")
    public void retornaVerdadeiroQuandoExisteEmpresaCNPJInformado() {

        // given | cenário
        String cnpj = "123";
        Empresa empresa = Empresa.builder()
                .cnpj("123")
                .nome("Golden")
                .build();
        entityManager.persist(empresa);

        // when | execução
        boolean exists = repository.existsByCnpj(cnpj);

        // then | verificação
        assertThat(exists).isTrue();

    }

    @Test
    @DisplayName("EXISTIR: Deve retornar falso quando existir uma empresa com CNPJ informado.")
    public void retornaFalsoQuandoExisteEmpresaCNPJInformado() {

        // given | cenário
        String cnpj = "123";
        Empresa empresa = Empresa.builder()
                .cnpj("321")
                .nome("Golden")
                .build();
        entityManager.persist(empresa);

        // when | execução
        boolean exists = repository.existsByCnpj(cnpj);

        // then | verificação
        assertThat(exists).isFalse();

    }
}
