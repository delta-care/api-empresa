package xyz.deltacare.empresa.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import xyz.deltacare.empresa.domain.Empresa;
import xyz.deltacare.empresa.domain.exception.EmpresaException;
import xyz.deltacare.empresa.ports.out.EmpresaRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class EmpresaServiceTest {

    EmpresaService empresaService;

    @MockBean
    EmpresaRepository empresaRepository;

    @BeforeEach
    public void setup() {
        this.empresaService = new EmpresaServiceImpl(empresaRepository);
    }

    @Test
    @DisplayName("CRIAR: Deve criar uma empresa com sucesso.")
    public void criarEmpresaTest() {

        // given | cenário
        Empresa empresa = Empresa.builder()
                .cnpj("123")
                .nome("Golden")
                .build();

        Empresa empresaCriadaBuild = Empresa.builder()
                .id(UUID.fromString("75bc9277-862d-4379-901e-c37bae7d8af3"))
                .cnpj("123")
                .nome("Golden")
                .build();

        Mockito
                .when(empresaRepository.existsByCnpj(empresa.getCnpj()))
                .thenReturn(false);

        Mockito
                .when(empresaRepository.save(empresa))
                .thenReturn(empresaCriadaBuild);

        // when | execução
        Empresa empresaCriada = empresaService.criar(empresa);

        // then | verificação
        assertThat(empresaCriada.getId()).isEqualTo(UUID.fromString("75bc9277-862d-4379-901e-c37bae7d8af3"));
        assertThat(empresaCriada.getCnpj()).isEqualTo("123");
        assertThat(empresaCriada.getNome()).isEqualTo("Golden");

    }

    @Test
    @DisplayName("CRIAR: Deve lançar erro quando tentar criar uma empresa com CNPJ existente.")
    public void criarEmpresaComCnpjExistente() {

        // given | cenário
        Empresa empresa = Empresa.builder()
                .cnpj("123")
                .nome("Golden")
                .build();

        Mockito
                .when(empresaRepository.existsByCnpj(empresa.getCnpj()))
                .thenReturn(true);

        // when | execução
        Throwable exception = Assertions.catchThrowable(() -> empresaService.criar(empresa));

        // then | verificação
        assertThat(exception)
                .isInstanceOf(EmpresaException.class)
                .hasMessage("Empresa já cadastrada.");

        Mockito
                .verify(empresaRepository, Mockito.never())
                .save(empresa);
    }

    @Test
    @DisplayName("OBTER: Deve obter uma empresa por id.")
    public void obterEmpresaPorId() {

        // given | cenário
        UUID id = UUID.randomUUID();

        Empresa empresaObtidaDoRepository = Empresa.builder()
                .id(id)
                .cnpj("123")
                .nome("Golden")
                .build();

        Mockito
                .when(empresaRepository.findById(id))
                .thenReturn(Optional.of(empresaObtidaDoRepository));

        // when | execução
        Optional<Empresa> empresaObtida = empresaService.getById(id);

        // then | verificação
        assertThat(empresaObtida.isPresent()).isTrue();
        assertThat(empresaObtida.get().getId()).isEqualTo(empresaObtidaDoRepository.getId());
        assertThat(empresaObtida.get().getCnpj()).isEqualTo(empresaObtidaDoRepository.getCnpj());
        assertThat(empresaObtida.get().getNome()).isEqualTo(empresaObtidaDoRepository.getNome());

    }

    @Test
    @DisplayName("OBTER: Deve obter empresa.")
    @SuppressWarnings("unchecked")
    public void obterEmpresa() {

        // given | cenário
        UUID id = UUID.randomUUID();

        Empresa empresa = Empresa.builder()
                .id(id)
                .cnpj("123")
                .nome("Golden")
                .build();

        List<Empresa> lista = Collections.singletonList(empresa);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Empresa> pageObtidaDoRepository = new PageImpl<>(
                lista,
                pageRequest, 1);

        Mockito
                .when(empresaRepository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class)))
                .thenReturn(pageObtidaDoRepository);

        // when | execução
        Page<Empresa> result = empresaService.obter(empresa, pageRequest);

        // then | verificação
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(lista);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);

    }

    @Test
    @DisplayName("OBTER: Deve retornar false quando obter uma empresa por id inexistente.")
    public void obterEmpresaIdInexistente() {

        // given | cenário
        UUID id = UUID.randomUUID();

        Mockito
                .when(empresaRepository.findById(id))
                .thenReturn(Optional.empty());

        // when | execução
        Optional<Empresa> empresaObtida = empresaService.getById(id);

        // then | verificação
        assertThat(empresaObtida.isPresent()).isFalse();

    }

    @Test
    @DisplayName("EXCLUIR: Deve excluir uma empresa.")
    public void excluirEmpresa() {

        // given | cenário
        UUID id = UUID.randomUUID();

        Empresa empresa = Empresa.builder()
                .id(id)
                .cnpj("123")
                .nome("Golden")
                .build();

        // when | execução
        empresaService.excluir(empresa);

        // then | verificação
        Mockito
                .verify(empresaRepository, Mockito.times(1))
                .delete(empresa);

    }

    @Test
    @DisplayName("EXCLUIR: Deve lançar erro ao tentar excluir empresa que id inexistente.")
    public void excluirEmpresaIdInexistente() {

        // given | cenário
        Empresa empresa = Empresa.builder()
                .cnpj("123")
                .nome("Golden")
                .build();

        // when | execução
        Throwable exception = Assertions.catchThrowable(() -> empresaService.excluir(empresa));

        // then | verificação
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Id da empresa inexistente.");

        Mockito
                .verify(empresaRepository, Mockito.never())
                .save(empresa);

    }

    @Test
    @DisplayName("ATUALIZAR: Deve atualizar uma empresa.")
    public void atualizarEmpresa() {

        // given | cenário
        UUID id = UUID.randomUUID();

        Empresa empresa = Empresa.builder()
                .id(id)
                .cnpj("123")
                .nome("Golden")
                .build();

        // when | execução
        empresaService.atualizar(empresa);

        // then | verificação
        Mockito
                .verify(empresaRepository, Mockito.times(1))
                .save(empresa);

    }

    @Test
    @DisplayName("ATUALIZAR: Deve lançar erro ao tentar atualizar empresa que id inexistente.")
    public void atualizarEmpresaIdInexistente() {

        // given | cenário
        Empresa empresa = Empresa.builder()
                .cnpj("123")
                .nome("Golden")
                .build();

        // when | execução
        Throwable exception = Assertions.catchThrowable(() -> empresaService.atualizar(empresa));

        // then | verificação
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Id da empresa inexistente.");

        Mockito
                .verify(empresaRepository, Mockito.never())
                .save(empresa);

    }

    @Test
    @DisplayName("ATUALIZAR: Deve atualizar nome de uma empresa.")
    public void atualizarNomeDeEmpresa() {

        // given | cenário
        UUID id = UUID.randomUUID();

        Empresa empresaASerAtualizada = Empresa.builder()
                .id(id)
                .cnpj("123")
                .nome("Golden")
                .build();

        Empresa empresaAtualizadaRepository = Empresa.builder()
                .id(id)
                .cnpj("123")
                .nome("Golden Cross")
                .build();
        BDDMockito
                .given(empresaRepository.save(empresaASerAtualizada))
                .willReturn(empresaAtualizadaRepository);

        // when | execução
        Empresa empresaAtualizada = empresaService.atualizar(empresaASerAtualizada);

        // then | verificação
        assertThat(empresaAtualizada.getNome()).isEqualTo(empresaAtualizadaRepository.getNome());

    }
}
