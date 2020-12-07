package xyz.deltacare.empresa.ports;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.deltacare.empresa.application.EmpresaService;
import xyz.deltacare.empresa.domain.Empresa;
import xyz.deltacare.empresa.ports.in.dto.EmpresaDTO;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class EmpresaControllerTest {

    private static final String EMPRESA_API_URI = "/empresas";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EmpresaService empresaService;

    @Test
    @DisplayName("Deve criar uma empresa com sucesso.")
    public void criarEmpresaTest() throws Exception {

        // given | cenário
        EmpresaDTO empresaDTO = EmpresaDTO.builder()
                .cnpj("123")
                .nome("Golden")
                .build();
        String json = new ObjectMapper().writeValueAsString(empresaDTO);

        Empresa empresaCriada = Empresa.builder()
                .id(UUID.fromString("75bc9277-862d-4379-901e-c37bae7d8af3"))
                .cnpj("123")
                .nome("Golden")
                .build();
        BDDMockito
                .given(empresaService.save(Mockito.any(Empresa.class)))
                .willReturn(empresaCriada);

        // when | execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(EMPRESA_API_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
        ResultActions perform = mockMvc.perform(request);

        // then | verificação
        perform
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value("75bc9277-862d-4379-901e-c37bae7d8af3"))
                .andExpect(jsonPath("cnpj").value(empresaDTO.getCnpj()))
                .andExpect(jsonPath("nome").value(empresaDTO.getNome()));

    }

    @Test
    @DisplayName("Deve lançar erro quando não houver dados suficientes para criação de empresa.")
    public void criarEmpresaInvalidaTest() throws Exception {

        // given | cenário
        String json = new ObjectMapper().writeValueAsString(new EmpresaDTO());

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
                .andExpect(jsonPath("errors", hasSize(2)));
    }
}
