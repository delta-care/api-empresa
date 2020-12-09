package xyz.deltacare.empresa.ports.out;

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

import java.util.Optional;
import java.util.UUID;

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

    @Test
    @DisplayName("OBTER: Deve obter uma empresa por id.")
    public void obterEmpresaPorId() {

        // given | cenário
        Empresa empresaPersistida = Empresa.builder()
                .cnpj("321")
                .nome("Golden")
                .build();
        entityManager.persist(empresaPersistida);

        // when | execução
        Optional<Empresa> empresaObtida = repository.findById(empresaPersistida.getId());

        // then | verificação
        assertThat(empresaObtida.isPresent()).isTrue();
        assertThat(empresaObtida.get().getId()).isEqualTo(empresaPersistida.getId());
        assertThat(empresaObtida.get().getCnpj()).isEqualTo(empresaPersistida.getCnpj());
        assertThat(empresaObtida.get().getNome()).isEqualTo(empresaPersistida.getNome());

    }

    @Test
    @DisplayName("OBTER: Deve retornar false quando obter uma empresa por id inexistente.")
    public void obterEmpresaIdInexistente() {

        // given | cenário
        UUID id = UUID.randomUUID();

        // when | execução
        Optional<Empresa> empresaObtida = repository.findById(id);

        // then | verificação
        assertThat(empresaObtida.isPresent()).isFalse();

    }

    @Test
    @DisplayName("EXCLUIR: Deve excluir uma empresa.")
    public void excluirEmpresa() {

        // given | cenário
        Empresa empresaPersistida = Empresa.builder()
                .cnpj("123")
                .nome("Golden")
                .build();
        entityManager.persist(empresaPersistida);

        Empresa empresaEncontrada = entityManager.find(Empresa.class, empresaPersistida.getId());

        // when | execução
        repository.delete(empresaEncontrada);

        // then | verificação
        Empresa empresaExcluida = entityManager.find(Empresa.class, empresaPersistida.getId());
        assertThat(empresaExcluida).isNull();

    }

    @Test
    @DisplayName("CRIAR: Deve criar uma empresa.")
    public void criarEmpresa() {

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
}
