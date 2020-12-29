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

import java.util.Collections;
import java.util.Random;

import static org.mockito.Mockito.doNothing;
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
    @DisplayName("Deve criar uma empresa com sucesso.")
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
    @DisplayName("Deve pesquisar uma empresa com sucesso.")
    void pesquisarEmpresaTest() throws Exception {

        // given | cenário
        Long idEnviado = new Random().nextLong();
        EmpresaDto empresaDtoEncontrada = EmpresaDto.builder()
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
                .andExpect(jsonPath("id").value(empresaDtoEncontrada.getId().toString()))
                .andExpect(jsonPath("cnpj").value(empresaDtoEncontrada.getCnpj()))
                .andExpect(jsonPath("nome").value(empresaDtoEncontrada.getNome()));
    }

    @Test
    @DisplayName("Deve pesquisar empresas com sucesso.")
    void pesquisarEmpresasTest() throws Exception {

        // given | cenário
        EmpresaDto empresaDtoEncontrada = EmpresaDto.builder()
                .id(new Random().nextLong())
                .cnpj("38.067.491/0001-60")
                .nome("Bruno e Oliver Contábil ME")
                .build();

        // when | execução
        when(service.findAll()).thenReturn(Collections.singletonList(empresaDtoEncontrada));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(EMPRESA_API_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        ResultActions perform = mockMvc.perform(request);

        // then | verificação
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(empresaDtoEncontrada.getId().toString()))
                .andExpect(jsonPath("$[0].cnpj").value(empresaDtoEncontrada.getCnpj()))
                .andExpect(jsonPath("$[0].nome").value(empresaDtoEncontrada.getNome()));
    }

    @Test
    @DisplayName("Deve excluir empresa com sucesso.")
    void excluirEmpresaTest() throws Exception {

        // given | cenário
        long idEnviado = new Random().nextLong();

        // when | execução
        doNothing().when(service).delete(idEnviado);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(EMPRESA_API_URI + "/" + idEnviado)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        ResultActions perform = mockMvc.perform(request);

        // then | verificação
        perform
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve atualizar uma empresa com sucesso.")
    void atualizarEmpresaTest() throws Exception {

        // given | cenário
        Long id = new Random().nextLong();

        EmpresaDto empresaDtoEnviada = EmpresaDto.builder()
                .id(id)
                .cnpj("38.067.491/0001-60")
                .nome("Bruno e Oliver Contábil ME")
                .build();
        String json = new ObjectMapper().writeValueAsString(empresaDtoEnviada);

        EmpresaDto empresaDtoAtualizada = EmpresaDto.builder()
                .id(id)
                .cnpj("38.067.491/0001-60")
                .nome("Bruno e Oliver Contábil ME")
                .build();

        // when | execução
        when(service.updateById(id, empresaDtoEnviada)).thenReturn(empresaDtoAtualizada);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(EMPRESA_API_URI + "/" + empresaDtoEnviada.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
        ResultActions perform = mockMvc.perform(request);

        // then | verificação
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(empresaDtoEnviada.getId()))
                .andExpect(jsonPath("cnpj").value(empresaDtoEnviada.getCnpj()))
                .andExpect(jsonPath("nome").value(empresaDtoEnviada.getNome()));
    }
}
