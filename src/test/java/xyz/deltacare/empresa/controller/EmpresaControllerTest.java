package xyz.deltacare.empresa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.deltacare.empresa.dto.EmpresaDto;
import xyz.deltacare.empresa.service.IEmpresaService;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(EmpresaController.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
class EmpresaControllerTest {

    private static final String EMPRESA_API_URI = "/api/v1/empresas";

    @MockBean
    @Qualifier("empresaService")
    IEmpresaService service;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("CRIAR: Deve criar uma empresa.")
    void criarEmpresaTest() throws Exception {

        // given | cenário
        EmpresaDto empresaDtoEnviada = EmpresaDto.builder()
                .cnpj("38.067.491/0001-60")
                .nome("Bruno e Oliver Contábil ME")
                .build();
        String json = new ObjectMapper().writeValueAsString(empresaDtoEnviada);

        UUID idCriado = UUID.randomUUID();
        EmpresaDto empresaDtoCriada = EmpresaDto.builder()
                .id(idCriado)
                .cnpj("38.067.491/0001-60")
                .nome("Bruno e Oliver Contábil ME")
                .build();

        // when | execução
        when(service.criar(empresaDtoEnviada)).thenReturn(empresaDtoCriada);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(EMPRESA_API_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
        ResultActions perform = mockMvc.perform(request);

        // then | verificação
        perform
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(idCriado.toString()))
                .andExpect(jsonPath("cnpj").value(empresaDtoEnviada.getCnpj()))
                .andExpect(jsonPath("nome").value(empresaDtoEnviada.getNome()));
    }

    @Test
    @DisplayName("CRIAR: Deve lançar erro ao tentar criar empresa com cnpj nulo.")
    void criarEmpresaCnpjNuloTest() throws Exception {

        EmpresaDto empresaDtoEnviada = EmpresaDto.builder()
                .cnpj(null)
                .nome("Bruno e Oliver Contábil ME")
                .build();
        criarEmpresaValidDto(empresaDtoEnviada, "CNPJ");

    }

    @Test
    @DisplayName("CRIAR: Deve lançar erro ao tentar criar empresa com cnpj vazio.")
    void criarEmpresaCnpjVazioTest() throws Exception {

        EmpresaDto empresaDtoEnviada = EmpresaDto.builder()
                .cnpj("")
                .nome("Bruno e Oliver Contábil ME")
                .build();
        criarEmpresaValidDto(empresaDtoEnviada, "CNPJ");

    }

    @Test
    @DisplayName("CRIAR: Deve lançar erro ao tentar criar empresa com nome nulo.")
    void criarEmpresaNomeNuloTest() throws Exception {

        EmpresaDto empresaDtoEnviada = EmpresaDto.builder()
                .cnpj("38.067.491/0001-60")
                .nome(null)
                .build();
        criarEmpresaValidDto(empresaDtoEnviada, "NOME");

    }

    @Test
    @DisplayName("CRIAR: Deve lançar erro ao tentar criar empresa com nome vazio.")
    void criarEmpresaNomeVazioTest() throws Exception {

        EmpresaDto empresaDtoEnviada = EmpresaDto.builder()
                .cnpj("38.067.491/0001-60")
                .nome("")
                .build();
        criarEmpresaValidDto(empresaDtoEnviada, "NOME");

    }

    @Test
    @DisplayName("CRIAR: Deve lançar erro ao tentar criar empresa com nome maior do que 255 caracteres.")
    void criarEmpresaNomeMais255CaracteresTest() throws Exception {

        // given | cenário
        EmpresaDto empresaDtoEnviada = EmpresaDto.builder()
                .cnpj("38.067.491/0001-60")
                .nome(RandomStringUtils.randomAlphabetic(256))
                .build();
        String json = new ObjectMapper().writeValueAsString(empresaDtoEnviada);

        // when | execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(EMPRESA_API_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
        ResultActions perform = mockMvc.perform(request);

        // then | verificação
        perform
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("NOME")));
    }

    void criarEmpresaValidDto(EmpresaDto empresaDtoEnviada, String campo) throws Exception {

        // given | cenário
        String json = new ObjectMapper().writeValueAsString(empresaDtoEnviada);

        // when | execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(EMPRESA_API_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
        ResultActions perform = mockMvc.perform(request);

        // then | verificação
        perform
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString(campo)));
    }
}
