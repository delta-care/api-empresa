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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpresaServiceSpaTest {

    @Mock
    EmpresaRepository repository;

    @InjectMocks
    EmpresaServiceSpa service;

    @Test
    @DisplayName("Deve criar uma empresa com sucesso.")
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
                .id(new Random().nextLong())
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
    @DisplayName("Deve lançar erro quando tentar criar uma empresa com CNPJ existente.")
    void criarEmpresaComCnpjExistente() {

        // given | cenário
        EmpresaDto empresaDto = EmpresaDto.builder()
                .cnpj("38.067.491/0001-60")
                .nome("Bruno e Oliver Contábil ME")
                .build();
        Empresa empresaEncontrada = Empresa.builder()
                .id(new Random().nextLong())
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
    @DisplayName("Deve pesquisar uma empresa com sucesso.")
    void pesquisarEmpresaTest() {

        // given | cenário
        Long idEnviado = new Random().nextLong();
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
    @DisplayName("Deve lançar erro ao tentar pesquisar empresa por id existente.")
    void pesquisarEmpresaComIdInexistenteTest() {

        // given | cenário
        Long idEnviado = new Random().nextLong();
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

    @Test
    @DisplayName("Deve pesquisar empresas com sucesso.")
    void pesquisarEmpresasTest() {

        // given | cenário
        long id = new Random().nextLong();
        Empresa empresaEncontrada = Empresa.builder()
                .id(id)
                .cnpj("38.067.491/0001-60")
                .nome("Bruno e Oliver Contábil ME")
                .build();
        EmpresaDto empresaDtoEsperada = EmpresaDto.builder()
                .id(id)
                .cnpj("38.067.491/0001-60")
                .nome("Bruno e Oliver Contábil ME")
                .build();

        // when | execução
        when(repository.findAll()).thenReturn(Collections.singletonList(empresaEncontrada));
        List<EmpresaDto> empresaDtoEncontrada = service.findAll();

        // then | verificação
        assertThat(empresaDtoEncontrada.size()).isEqualTo(1);
        assertThat(empresaDtoEncontrada.get(0)).isEqualTo(empresaDtoEsperada);

    }

    @Test
    @DisplayName("Deve pesquisar empresas com sucesso quando não encontrar empresa.")
    void pesquisarEmpresasVazioTest() {

        // given | cenário
        List<Empresa> emptyList = Collections.emptyList();

        // when | execução
        when(repository.findAll()).thenReturn(emptyList);
        List<EmpresaDto> empresaDtoEncontrada = service.findAll();

        // then | verificação
        assertThat(empresaDtoEncontrada.size()).isEqualTo(0);

    }

    @Test
    @DisplayName("Deve excluir uma empresa com sucesso.")
    void excluirEmpresaTest() {

        // given | cenário
        long id = new Random().nextLong();
        Empresa empresaEncontrada = Empresa.builder()
                .id(id)
                .cnpj("38.067.491/0001-60")
                .nome("Bruno e Oliver Contábil ME")
                .build();

        // when | execução
        doNothing().when(repository).deleteById(id);
        when(repository.findById(id)).thenReturn(Optional.of(empresaEncontrada));
        service.delete(id);

        // then | verificação
        verify(repository, times(1)).deleteById(id);
        verify(repository, times(1)).findById(id);

    }

    @Test
    @DisplayName("Deve lançar erro ao tentar excluir uma empresa inexistente.")
    void excluirEmpresaInexistenteTest() {

        // given | cenário
        long id = new Random().nextLong();

        // when | execução
        when(repository.findById(id)).thenReturn(Optional.empty());
        Throwable exception = Assertions.catchThrowable(() -> service.delete(id));

        // then | verificação
        assertThat(exception)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(String.format("Empresa com id %s não existe.", id));
    }

    @Test
    @DisplayName("Deve atualizar uma empresa com sucesso.")
    void atualizarEmpresaTest() {

        // given | cenário
        long id = new Random().nextLong();
        EmpresaDto empresaDtoEnviada = EmpresaDto.builder()
                .id(id)
                .cnpj("38.067.491/0001-60")
                .nome("Bruno e Oliver Contábil ME")
                .build();
        Empresa empresaEnviada = Empresa.builder()
                .id(id)
                .cnpj("38.067.491/0001-60")
                .nome("Bruno e Oliver Contábil ME")
                .build();
        Empresa empresaEsperada = Empresa.builder()
                .id(id)
                .cnpj("38.067.491/0001-60")
                .nome("Bruno e Oliver Contábil ME")
                .build();

        // when | execução
        when(repository.findById(id)).thenReturn(Optional.of(empresaEnviada));
        when(repository.save(empresaEnviada)).thenReturn(empresaEsperada);
        EmpresaDto empresaDtoAtualizada = service.updateById(id, empresaDtoEnviada);

        // then | verificação
        assertThat(empresaDtoAtualizada).isEqualTo(empresaDtoEnviada);

    }

    @Test
    @DisplayName("Deve lançar erro ao tentar atualizar uma empresa inexistente.")
    void atualizarEmpresaInexistenteTest() {

        // given | cenário
        long id = new Random().nextLong();
        EmpresaDto empresaDtoEnviada = EmpresaDto.builder()
                .id(id)
                .cnpj("38.067.491/0001-60")
                .nome("Bruno e Oliver Contábil ME")
                .build();
        Empresa empresaEnviada = Empresa.builder()
                .id(id)
                .cnpj("38.067.491/0001-60")
                .nome("Bruno e Oliver Contábil ME")
                .build();
        Empresa empresaEsperada = Empresa.builder()
                .id(id)
                .cnpj("38.067.491/0001-60")
                .nome("Bruno e Oliver Contábil ME")
                .build();

        // when | execução
        when(repository.findById(id)).thenReturn(Optional.empty());
        Throwable exception = Assertions.catchThrowable(() -> service.updateById(id, empresaDtoEnviada));

        // then | verificação
        assertThat(exception)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(String.format("Empresa com id %s não existe.", id));

    }
}
