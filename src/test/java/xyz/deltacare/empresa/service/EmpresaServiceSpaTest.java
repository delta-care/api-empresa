package xyz.deltacare.empresa.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import xyz.deltacare.empresa.builder.EmpresaDtoBuilder;
import xyz.deltacare.empresa.domain.Beneficiario;
import xyz.deltacare.empresa.domain.Produto;
import xyz.deltacare.empresa.dto.EmpresaDto;
import xyz.deltacare.empresa.domain.Empresa;
import xyz.deltacare.empresa.event.EventSender;
import xyz.deltacare.empresa.mapper.EmpresaMapper;
import xyz.deltacare.empresa.repository.BeneficiarioRepository;
import xyz.deltacare.empresa.repository.EmpresaRepository;
import xyz.deltacare.empresa.repository.ProdutoRepository;

import javax.persistence.EntityExistsException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class EmpresaServiceSpaTest {

    @InjectMocks private EmpresaServiceSpa service;
    @Mock private EmpresaRepository empresaRepository;
    @Mock private ProdutoRepository produtoRepository;
    @Mock private BeneficiarioRepository beneficiarioRepository;
    @Mock private EventSender eventSender;

    private Pageable pageable;
    private EmpresaDto empresaDtoEnviada;
    private EmpresaDto empresaDtoRetornada;
    private List<EmpresaDto> empresasDtoRetornadas;
    private Empresa empresaRetornada;
    private Produto produtoRetornado;
    private Beneficiario beneficiarioRetornado;
    private Page<Empresa> empresasRetornadasPage;
    private final EmpresaMapper empresaMapper = EmpresaMapper.INSTANCE;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);

        empresaDtoEnviada = EmpresaDtoBuilder.builder().build().buildEmpresaDto();
        empresaDtoRetornada = EmpresaDtoBuilder.builder().id("637832").build().buildEmpresaDto();
        empresasDtoRetornadas = Collections.singletonList(EmpresaDtoBuilder.builder().id("637832").build().buildEmpresaDto());

        empresaRetornada = empresaMapper.toModel(empresaDtoRetornada);
        produtoRetornado = new ArrayList<>(empresaRetornada.getProdutos()).get(0);
        beneficiarioRetornado = new ArrayList<>(empresaRetornada.getBeneficiarios()).get(0);
        empresasRetornadasPage = new PageImpl<>(empresasDtoRetornadas.stream().map(empresaMapper::toModel).collect(Collectors.toList()));
    }

    @Test
    @DisplayName("Deve tentar criar uma empresa e retornar dados iguais.")
    void criarEmpresaTest() {

        // given | cenário
        // setUp

        // when | execução
        doNothing().when(eventSender).send(any(Beneficiario.class));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoRetornado);
        when(beneficiarioRepository.save(any(Beneficiario.class))).thenReturn(beneficiarioRetornado);
        when(empresaRepository.save(any(Empresa.class))).thenReturn(empresaRetornada);
        EmpresaDto empresaDtoRetornada = service.criar(empresaDtoEnviada);

        // then | verificação
        assertThat(empresaDtoRetornada).isEqualTo(this.empresaDtoRetornada);

    }

    @Test
    @DisplayName("Deve tentar criar empresa e retornar erro de CNPJ existente.")
    void criarEmpresaComCnpjExistente() {

        // given | cenário
        // setUp

        // when | execução
        when(empresaRepository.findByCnpj(empresaDtoEnviada.getCnpj())).thenReturn(Optional.of(new Empresa()));
        Throwable exception = Assertions.catchThrowable(() -> service.criar(empresaDtoEnviada));

        // then | verificação
        assertThat(exception)
                .isInstanceOf(EntityExistsException.class)
                .hasMessage(String.format("Empresa com CNPJ %s já existe.", empresaDtoEnviada.getCnpj()));

    }

    @Test
    @DisplayName("Deve tentar pesquisar empresa por código e retornar dados iguais.")
    void pesquisarEmpresaPorCodigoTest() {

        // given | cenário
        String codigoEnviado = "637832";

        // when | execução
        when(empresaRepository.findAll(pageable, codigoEnviado, "", "")).thenReturn(empresasRetornadasPage);
        List<EmpresaDto> empresasDtoRetornadas = service.pesquisar(pageable, codigoEnviado, "", "");

        // then | verificação
        assertThat(empresasDtoRetornadas).isEqualTo(this.empresasDtoRetornadas);

    }

    @Test
    @DisplayName("Deve tentar pesquisar empresa por cnpj e retornar dados iguais.")
    void pesquisarEmpresaPorCnpjTest() {

        // given | cenário
        String cnpjEnviado = empresaDtoRetornada.getCnpj();

        // when | execução
        when(empresaRepository.findAll(pageable, "", cnpjEnviado, "")).thenReturn(empresasRetornadasPage);
        List<EmpresaDto> empresasDtoRetornadas = service.pesquisar(pageable, "", cnpjEnviado, "");

        // then | verificação
        assertThat(empresasDtoRetornadas).isEqualTo(this.empresasDtoRetornadas);

    }

    @Test
    @DisplayName("Deve tentar pesquisar empresa por nome e retornar dados iguais.")
    void pesquisarEmpresaPorNomeTest() {

        // given | cenário
        String nomeEnviado = empresaDtoRetornada.getNome().split(" ")[0];

        // when | execução
        when(empresaRepository.findAll(pageable, "", "", nomeEnviado)).thenReturn(empresasRetornadasPage);
        List<EmpresaDto> empresasDtoRetornadas2 = service.pesquisar(pageable, "", "", nomeEnviado);

        // then | verificação
        assertThat(empresasDtoRetornadas2).isEqualTo(this.empresasDtoRetornadas);

    }

    @Test
    @DisplayName("Deve tentar atualizar uma empresa e retornar dados iguais.")
    void atualizarEmpresaTest() {

        // given | cenário
        // setUp

        // when | execução
        doNothing().when(eventSender).send(any(Beneficiario.class));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoRetornado);
        when(beneficiarioRepository.save(any(Beneficiario.class))).thenReturn(beneficiarioRetornado);
        when(empresaRepository.save(any(Empresa.class))).thenReturn(this.empresaRetornada);
        EmpresaDto empresaDtoAtualizada = service.atualizar(this.empresaDtoRetornada);

        // then | verificação
        assertThat(empresaDtoAtualizada).isEqualTo(empresaDtoRetornada);

    }
}
