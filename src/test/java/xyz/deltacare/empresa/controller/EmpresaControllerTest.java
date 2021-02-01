package xyz.deltacare.empresa.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import xyz.deltacare.empresa.builder.EmpresaDtoBuilder;
import xyz.deltacare.empresa.dto.EmpresaDto;
import xyz.deltacare.empresa.service.EmpresaServiceSpa;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class EmpresaControllerTest {

    @Mock private EmpresaServiceSpa service;
    @InjectMocks private EmpresaController controller;

    private MockMvc mockMvc;
    private Pageable pageable;
    private ObjectMapper objectMapper;
    private EmpresaDto empresaDtoEnviada;
    private List<EmpresaDto> empresaDtoRetornada;
    private final String EMPRESA_API_URI = "/api/v1/empresas";

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        pageable = PageRequest.of(0, 10);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        empresaDtoEnviada = EmpresaDtoBuilder.builder().build().buildEmpresaDto();
        empresaDtoRetornada = Collections.singletonList(EmpresaDtoBuilder.builder().id("637832").build().buildEmpresaDto());
    }

    @Test
    @DisplayName("Deve tentar criar empresa e retornar 201 com dados iguais")
    void criarEmpresaTest() throws Exception {

        // given | cenário
        String json = new ObjectMapper().writeValueAsString(empresaDtoEnviada);

        // when | execução
        when(service.criar(empresaDtoEnviada)).thenReturn(empresaDtoRetornada.get(0));
        MockHttpServletRequestBuilder request = post(EMPRESA_API_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
        ResultActions perform = mockMvc.perform(request);

        // then | verificação
        perform
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());
        assertThat(retornoItemParaString(perform.andReturn()))
                .isEqualTo(itemParaStringIso(empresaDtoRetornada.get(0)));

    }

    @Test
    @DisplayName("Deve tentar pesquisar empresa por codigo e retornar 200 com dados iguais")
    void pesquisarEmpresaPorCodigoTest() throws Exception {

        // given | cenário
        String codigoEnviado = empresaDtoRetornada.get(0).getId();

        // when | execução
        when(service.pesquisar(pageable, codigoEnviado, "", "")).thenReturn(empresaDtoRetornada);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(EMPRESA_API_URI + "/?codigo=" + codigoEnviado)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        ResultActions perform = mockMvc.perform(request);

        // then | verificação
        perform
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
        assertThat(retornoListaParaString(perform.andReturn()))
                .isEqualTo(listaParaStringIso(empresaDtoRetornada));
    }

    @Test
    @DisplayName("Deve tentar pesquisar empresa por cnpj e retornar 200 com dados iguais")
    void pesquisarEmpresaPorCnpjTest() throws Exception {

        // given | cenário
        String cnpjEnviado = empresaDtoRetornada.get(0).getCnpj();

        // when | execução
        when(service.pesquisar(pageable, "", cnpjEnviado, "")).thenReturn(empresaDtoRetornada);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(EMPRESA_API_URI + "/?cnpj=" + cnpjEnviado)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        ResultActions perform = mockMvc.perform(request);

        // then | verificação
        perform
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
        assertThat(retornoListaParaString(perform.andReturn()))
                .isEqualTo(listaParaStringIso(empresaDtoRetornada));
    }

    @Test
    @DisplayName("Deve tentar pesquisar empresa por nome e retornar 200 com dados iguais")
    void pesquisarEmpresaPorNomeTest() throws Exception {

        // given | cenário
        String nomeEnviado = empresaDtoRetornada.get(0).getNome().split(" ")[0];

        // when | execução
        when(service.pesquisar(pageable, "", "", nomeEnviado)).thenReturn(empresaDtoRetornada);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(EMPRESA_API_URI + "/?nome=" + nomeEnviado)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        ResultActions perform = mockMvc.perform(request);

        // then | verificação
        perform
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
        assertThat(retornoListaParaString(perform.andReturn()))
                .isEqualTo(listaParaStringIso(empresaDtoRetornada));
    }

    @Test
    @DisplayName("Deve tentar atualizar empresa com sucesso e retornar 200 com dados iguais")
    void atualizarEmpresaTest() throws Exception {

        // given | cenário
        String json = new ObjectMapper().writeValueAsString(empresaDtoRetornada.get(0));

        // when | execução
        when(service.atualizar(empresaDtoRetornada.get(0))).thenReturn(empresaDtoRetornada.get(0));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(EMPRESA_API_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
        ResultActions perform = mockMvc.perform(request);

        // then | verificação
        perform
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
        assertThat(retornoItemParaString(perform.andReturn()))
                .isEqualTo(itemParaStringIso(empresaDtoRetornada.get(0)));
    }

    private String retornoListaParaString(MvcResult retorno) throws UnsupportedEncodingException, JsonProcessingException {
        EmpresaDto[] response = objectMapper.readValue(retorno.getResponse().getContentAsString(), EmpresaDto[].class);
        return Arrays.asList(response).toString();
    }

    private String retornoItemParaString(MvcResult retorno) throws UnsupportedEncodingException, JsonProcessingException {
        EmpresaDto response = objectMapper.readValue(retorno.getResponse().getContentAsString(), EmpresaDto.class);
        return response.toString();
    }

    private String listaParaStringIso(List<EmpresaDto> lista) {
        return new String(lista.toString().getBytes(), StandardCharsets.ISO_8859_1);
    }

    private String itemParaStringIso(EmpresaDto item) {
        return new String(item.toString().getBytes(), StandardCharsets.ISO_8859_1);
    }
}
