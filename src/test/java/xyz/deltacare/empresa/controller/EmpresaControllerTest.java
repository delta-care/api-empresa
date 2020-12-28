package xyz.deltacare.empresa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Random;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    @DisplayName("CRIAR: Deve criar uma empresa com sucesso.")
    void criarEmpresaTest() throws Exception {

        // given | cenário
        EmpresaDto empresaDtoEnviada = EmpresaDto.builder()
                .cnpj("38.067.491/0001-60")
                .nome("Bruno e Oliver Contábil ME")
                .build();
        String json = new ObjectMapper().writeValueAsString(empresaDtoEnviada);

        Long idCriado = new Random().nextLong();
        EmpresaDto empresaDtoCriada = EmpresaDto.builder()
                .id(idCriado)
                .cnpj("38.067.491/0001-60")
                .nome("Bruno e Oliver Contábil ME")
                .build();

        // when | execução
        when(service.create(empresaDtoEnviada)).thenReturn(empresaDtoCriada);
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
    @DisplayName("PESQUISAR: Deve pesquisar uma empresa com sucesso.")
    void pesquisarEmpresaTest() throws Exception {

        // given | cenário
        Long idEnviado = new Random().nextLong();
        EmpresaDto empresaDtoEncontrada = EmpresaDto.builder()
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
        when(service.findById(idEnviado)).thenReturn(empresaDtoEncontrada);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(EMPRESA_API_URI + "/" + idEnviado)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        ResultActions perform = mockMvc.perform(request);

        // then | verificação
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(empresaDtoEsperada.getId().toString()))
                .andExpect(jsonPath("cnpj").value(empresaDtoEsperada.getCnpj()))
                .andExpect(jsonPath("nome").value(empresaDtoEsperada.getNome()));
    }

}
