package xyz.deltacare.empresa.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import xyz.deltacare.empresa.dto.EmpresaDto;
import xyz.deltacare.empresa.service.IEmpresaService;

import java.util.Random;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.DisplayName.class)
class EmpresaExceptionHandlerTest {

    private static final String EMPRESA_API_URI = "/api/v1/empresas";

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("Deve retornar 400 ao tentar criar empresa com id existente.")
    void criarEmpresaCnpjExistenteTest() throws Exception {

        // given | cenário
        EmpresaDto empresaDtoEnviada = EmpresaDto.builder()
                .cnpj("38.067.491/0001-60")
                .nome("Bruno e Oliver Contábil ME")
                .build();
        String jsonEnviada = new ObjectMapper().writeValueAsString(empresaDtoEnviada);
        MockHttpServletRequestBuilder requestEnviada = MockMvcRequestBuilders
                .post(EMPRESA_API_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonEnviada);
        ResultActions perform = mockMvc.perform(requestEnviada);
        String retorno = perform.andReturn().getResponse().getContentAsString();
        EmpresaDto empresaDtoCriada = new ObjectMapper().readValue(
                perform.andReturn().getResponse().getContentAsString(), EmpresaDto.class);
        String jsonCriada = new ObjectMapper().writeValueAsString(empresaDtoCriada);

        // when | execução
        MockHttpServletRequestBuilder requestCriada = MockMvcRequestBuilders
                .post(EMPRESA_API_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonCriada);
        ResultActions performCriada = mockMvc.perform(requestCriada);

        // then | verificação
        performCriada
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 400 ao tentar criar empresa com cnpj vazio.")
    void criarEmpresaCnpjVazioTest() throws Exception {

        // given | cenário
        EmpresaDto empresaDtoEnviada = EmpresaDto.builder()
                .cnpj("")
                .nome("Bruno e Oliver Contábil ME")
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
                .andExpect(content().string(org.hamcrest.Matchers.containsString("CNPJ")));

    }

    @Test
    @DisplayName("Deve retornar 400 ao tentar criar empresa com json desformatado.")
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
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 404 ao tentar pesquisar empresa com id inexistente.")
    void pesquisarEmpresaIdInexistenteTest() throws Exception {

        // given | cenário
        long idEnviado = new Random().nextLong();

        // when | execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(EMPRESA_API_URI + "/" + idEnviado)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        ResultActions perform = mockMvc.perform(request);

        // then | verificação
        perform
                .andExpect(status().isNotFound());
    }

}
