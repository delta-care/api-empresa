package xyz.deltacare.empresa.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import xyz.deltacare.empresa.builder.EmpresaDtoBuilder;
import xyz.deltacare.empresa.controller.EmpresaController;
import xyz.deltacare.empresa.dto.EmpresaDto;
import xyz.deltacare.empresa.service.EmpresaServiceSpa;

import javax.persistence.EntityExistsException;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(EmpresaController.class)
class EmpresaExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmpresaServiceSpa service;

    private EmpresaDto empresaDtoEnviada;
    private final String EMPRESA_API_URI = "/api/v1/empresas";

    @BeforeEach
    void setUp() {
        empresaDtoEnviada = EmpresaDtoBuilder.builder().build().buildEmpresaDto();
    }

    @Test
    @DisplayName("Deve tentar criar empresa com id existente e retornar 400")
    void criarEmpresaCnpjExistenteTest() throws Exception {

        // given | cenário
        String json = new ObjectMapper().writeValueAsString(empresaDtoEnviada);

        // when | execução
        when(service.criar(empresaDtoEnviada)).thenThrow(new EntityExistsException(""));
        MockHttpServletRequestBuilder requestCriada = MockMvcRequestBuilders
                .post(EMPRESA_API_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
        ResultActions perform = mockMvc.perform(requestCriada);

        // then | verificação
        perform
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").exists())
                .andExpect(jsonPath("status").exists())
                .andExpect(jsonPath("timestamp").exists())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("errors").exists());
    }

    @Test
    @DisplayName("Deve tentar criar empresa com cnpj vazio e retornar 400")
    void criarEmpresaArgumentoInvalidoTest() throws Exception {

        // given | cenário
        empresaDtoEnviada.setCnpj("");
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
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").exists())
                .andExpect(jsonPath("status").exists())
                .andExpect(jsonPath("timestamp").exists())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("errors").exists());

    }

    @Test
    @DisplayName("Deve tentar criar empresa com json desformatado e retornar 400")
    void criarEmpresaJsonDesformatadoTest() throws Exception {

        // given | cenário
        String jsonDesformatado =
                "{" +
                        "\"cnpj\" \"38.067.491/0001-60\"," +
                        "\"nome\" \"Bruno e Oliver Contábil ME\"" +
                        "}";

        // when | execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(EMPRESA_API_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonDesformatado);
        ResultActions perform = mockMvc.perform(request);

        // then | verificação
        perform
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").exists())
                .andExpect(jsonPath("status").exists())
                .andExpect(jsonPath("timestamp").exists())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("errors").exists());
    }

}
