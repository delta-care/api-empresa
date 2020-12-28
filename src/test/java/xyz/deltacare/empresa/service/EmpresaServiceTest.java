package xyz.deltacare.empresa.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import xyz.deltacare.empresa.dto.EmpresaDto;
import xyz.deltacare.empresa.domain.Empresa;
import xyz.deltacare.empresa.repository.EmpresaRepository;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
class EmpresaServiceTest {

    @Mock
    EmpresaRepository repository;

    @InjectMocks
    EmpresaService service;

    @Test
    @DisplayName("CRIAR: Deve criar uma empresa com sucesso.")
    void criarEmpresaTest() {

        // given | cenário
        EmpresaDto empresaDtoEnviada = EmpresaDto.builder()
                .cnpj("38.067.491/0001-60")
                .nome("Bruno e Oliver Contábil ME")
                .build();
        Empresa empresaEnviada = Empresa.builder()
                .cnpj("38.067.491/0001-60")
                .nome("Bruno e Oliver Contábil ME")
                .build();
        Empresa empresaEsperada = Empresa.builder()
                .id(UUID.randomUUID())
                .cnpj("38.067.491/0001-60")
                .nome("Bruno e Oliver Contábil ME")
                .build();

        // when | execução
        when(repository.findByCnpj(empresaEnviada.getCnpj())).thenReturn(Optional.empty());
        when(repository.save(empresaEnviada)).thenReturn(empresaEsperada);
        EmpresaDto empresaDtoCriada = service.create(empresaDtoEnviada);

        // then | verificação
        assertThat(empresaDtoCriada.getId()).isEqualTo(empresaEsperada.getId());
        assertThat(empresaDtoCriada.getCnpj()).isEqualTo(empresaEsperada.getCnpj());
        assertThat(empresaDtoCriada.getNome()).isEqualTo(empresaEsperada.getNome());

    }

    @Test
    @DisplayName("CRIAR: Deve lançar erro quando tentar criar uma empresa com CNPJ existente.")
    void criarEmpresaComCnpjExistente() {

        // given | cenário
        EmpresaDto empresaDto = EmpresaDto.builder()
                .cnpj("38.067.491/0001-60")
                .nome("Bruno e Oliver Contábil ME")
                .build();
        Empresa empresaEncontrada = Empresa.builder()
                .id(UUID.randomUUID())
                .cnpj("38.067.491/0001-60")
                .nome("Bruno e Oliver Contábil ME")
                .build();

        // when | execução
        when(repository.findByCnpj(empresaDto.getCnpj())).thenReturn(Optional.of(empresaEncontrada));
        Throwable exception = Assertions.catchThrowable(() -> service.create(empresaDto));

        // then | verificação
        assertThat(exception)
                .isInstanceOf(EntityExistsException.class)
                .hasMessage(String.format("Empresa com CNPJ %s já existe.", empresaDto.getCnpj()));

    }

    @Test
    @DisplayName("PESQUISAR: Deve pesquisar uma empresa com sucesso.")
    void pesquisarEmpresaTest() {

        // given | cenário
        UUID idEnviado = UUID.randomUUID();
        Empresa empresaEncontrada = Empresa.builder()
                .id(idEnviado)
                .cnpj("38.067.491/0001-60")
                .nome("Bruno e Oliver Contábil ME")
                .build();
        EmpresaDto empresaDtoEsperada = EmpresaDto.builder()
                .id(idEnviado)
                .cnpj("38.067.491/0001-60")
                .nome("Bruno e Oliver Contábil ME")
                .build();

        // when | execução
        when(repository.findById(idEnviado)).thenReturn(Optional.of(empresaEncontrada));
        EmpresaDto empresaDtoEncontrada = service.findById(idEnviado);

        // then | verificação
        assertThat(empresaDtoEncontrada.getId()).isEqualTo(empresaDtoEsperada.getId());
        assertThat(empresaDtoEncontrada.getCnpj()).isEqualTo(empresaDtoEsperada.getCnpj());
        assertThat(empresaDtoEncontrada.getNome()).isEqualTo(empresaDtoEsperada.getNome());

    }

    @Test
    @DisplayName("PESQUISAR: Deve lançar erro ao tentar pesquisar empresa por Id que não existe.")
    void pesquisarEmpresaComIdInexistenteTest() {

        // given | cenário
        UUID idEnviado = UUID.randomUUID();
        EmpresaDto empresaDtoEsperada = EmpresaDto.builder()
                .id(idEnviado)
                .cnpj("38.067.491/0001-60")
                .nome("Bruno e Oliver Contábil ME")
                .build();

        // when | execução
        when(repository.findById(idEnviado)).thenReturn(Optional.empty());
        Throwable exception = Assertions.catchThrowable(() -> service.findById(idEnviado));


        // then | verificação
        assertThat(exception)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(String.format("Empresa com id %s não existe.", empresaDtoEsperada.getId()));

    }

}
