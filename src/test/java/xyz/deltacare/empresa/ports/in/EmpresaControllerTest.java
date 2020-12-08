package xyz.deltacare.empresa.ports.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
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
import xyz.deltacare.empresa.domain.exception.EmpresaException;
import xyz.deltacare.empresa.ports.in.dto.EmpresaDTO;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class EmpresaControllerTest {

    private static final String EMPRESA_API_URI = "/empresas";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EmpresaService empresaService;

    @Test
    @DisplayName("CRIAR: Deve criar uma empresa.")
    public void criarEmpresaTest() throws Exception {

        // given | cenário
        EmpresaDTO empresaDTO = EmpresaDTO.builder()
                .cnpj("123")
                .nome("Golden")
                .build();
        String json = new ObjectMapper().writeValueAsString(empresaDTO);

        Empresa empresaCriada = Empresa.builder()
                .id(UUID.randomUUID())
                .cnpj("123")
                .nome("Golden")
                .build();
        BDDMockito
                .given(empresaService.criar(Mockito.any(Empresa.class)))
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
                .andExpect(jsonPath("id").value(empresaCriada.getId().toString()))
                .andExpect(jsonPath("cnpj").value(empresaCriada.getCnpj()))
                .andExpect(jsonPath("nome").value(empresaCriada.getNome()));
    }

    @Test
    @DisplayName("CRIAR: Deve lançar erro quando não houver dados suficientes para criação de empresa.")
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

    @Test
    @DisplayName("CRIAR: Deve lançar erro quando tentar criar uma empresa com CNPJ existente.")
    public void criarEmpresaComCnpjExistente() throws Exception {

        // given | cenário
        EmpresaDTO empresaDTO = EmpresaDTO.builder()
                .cnpj("123")
                .nome("Golden")
                .build();
        String json = new ObjectMapper().writeValueAsString(empresaDTO);

        String mensagemDeErro = "Empresa já cadastrada.";
        BDDMockito
                .given(empresaService.criar(Mockito.any(Empresa.class)))
                .willThrow(new EmpresaException(mensagemDeErro));

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
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value(mensagemDeErro));

    }

    @Test
    @DisplayName("OBTER: Deve obter informações de uma empresa.")
    public void obterInformacoesDeEmpresa() throws Exception {

        // given | cenário
        UUID id = UUID.randomUUID();

        Empresa empresa = Empresa.builder()
                .id(id)
                .cnpj("123")
                .nome("Golden")
                .build();

        BDDMockito
                .given(empresaService.getById(id))
                .willReturn(Optional.of(empresa));

        // when | execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(EMPRESA_API_URI.concat("/"+id))
                .accept(MediaType.APPLICATION_JSON);
        ResultActions perform = mockMvc.perform(request);

        // then | verificação
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(empresa.getId().toString()))
                .andExpect(jsonPath("cnpj").value(empresa.getCnpj()))
                .andExpect(jsonPath("nome").value(empresa.getNome()));

    }

    @Test
    @DisplayName("OBTER: Deve retornar código not found quando tentar obter empresa com id inexistente.")
    public void obterEmpresaIdInexistente() throws Exception {

        // given | cenário
        UUID id = UUID.randomUUID();

        BDDMockito
                .given(empresaService.getById(id))
                .willReturn(Optional.empty());

        // when | execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(EMPRESA_API_URI.concat("/"+id))
                .accept(MediaType.APPLICATION_JSON);
        ResultActions perform = mockMvc.perform(request);

        // then | verificação
        perform
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("EXCLUIR: Deve excluir uma empresa.")
    public void excluirEmpresa() throws Exception {

        // given | cenário
        Empresa empresa = Empresa.builder()
                .id(UUID.randomUUID())
                .cnpj("123")
                .nome("Golden")
                .build();

        BDDMockito
                .given(empresaService.getById(empresa.getId()))
                .willReturn(Optional.of(empresa));

        // when | execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(EMPRESA_API_URI.concat("/"+empresa.getId()))
                .accept(MediaType.APPLICATION_JSON);
        ResultActions perform = mockMvc.perform(request);

        // then | verificação
        perform
                .andExpect(status().isNoContent());

    }

    @Test
    @DisplayName("EXCLUIR: Deve excluir uma empresa com CNPJ inexistente.")
    public void excluirEmpresaComCnpjInexistente() throws Exception {

        // given | cenário
        Empresa empresa = Empresa.builder()
                .id(UUID.randomUUID())
                .cnpj("123")
                .nome("Golden")
                .build();

        BDDMockito
                .given(empresaService.getById(empresa.getId()))
                .willReturn(Optional.empty());

        // when | execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(EMPRESA_API_URI.concat("/"+empresa.getId()))
                .accept(MediaType.APPLICATION_JSON);
        ResultActions perform = mockMvc.perform(request);

        // then | verificação
        perform
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("ATUALIZAR: Deve atualizar nome de uma empresa.")
    public void atualizarNomeDeEmpresa() throws Exception {

        // given | cenário
        UUID id = UUID.randomUUID();

        EmpresaDTO empresaEnviada = EmpresaDTO.builder()
                .id(id)
                .cnpj("123")
                .nome("Golden")
                .build();
        String jsonEnviado = new ObjectMapper().writeValueAsString(empresaEnviada);

        Empresa empresaExistente = Empresa.builder()
                .id(id)
                .cnpj("123")
                .nome("Golden")
                .build();
        BDDMockito
                .given(empresaService.getById(id))
                .willReturn(Optional.of(empresaExistente));

        Empresa empresaASerAtualizada = Empresa.builder()
                .id(id)
                .cnpj("123")
                .nome("Golden")
                .build();
        Empresa empresaAtualizada = Empresa.builder()
                .id(id)
                .cnpj("123")
                .nome("Golden Cross")
                .build();
        BDDMockito
                .given(empresaService.atualizar(empresaASerAtualizada))
                .willReturn(empresaAtualizada);

        // when | execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .patch(EMPRESA_API_URI.concat("/"+id))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonEnviado);
        ResultActions perform = mockMvc.perform(request);

        // then | verificação
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("nome").value(empresaAtualizada.getNome()));

    }

    @Test
    @DisplayName("ATUALIZAR: Deve retornar código not found quando tentar atualizar empresa com CNPJ inexistente.")
    public void atualizarEmpresaCnpjInexistente() throws Exception {

        // given | cenário
        UUID id = UUID.randomUUID();

        Empresa empresaEnviada = Empresa.builder()
                .id(id)
                .cnpj("123")
                .nome("Golden Cross")
                .build();
        String jsonEnviado = new ObjectMapper().writeValueAsString(empresaEnviada);

        BDDMockito
                .given(empresaService.getById(id))
                .willReturn(Optional.empty());

        // when | execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .patch(EMPRESA_API_URI.concat("/"+id))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonEnviado);
        ResultActions perform = mockMvc.perform(request);

        // then | verificação
        perform
                .andExpect(status().isNotFound());

    }
}
