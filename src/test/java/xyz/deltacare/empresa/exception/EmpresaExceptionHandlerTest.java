package xyz.deltacare.empresa.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Random;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.DisplayName.class)
class EmpresaExceptionHandlerTest {

    private static final String EMPRESA_API_URI = "/api/v1/empresas";

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("CRIAR: Deve retornar 400 ao criar empresa com json desformatado.")
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
    @DisplayName("PESQUISAR: Deve retornar 404 ao pesquisar empresa com id inexistente.")
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
