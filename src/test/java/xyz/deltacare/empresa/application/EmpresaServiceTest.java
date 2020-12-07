package xyz.deltacare.empresa.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import xyz.deltacare.empresa.domain.Empresa;
import xyz.deltacare.empresa.domain.exception.EmpresaException;
import xyz.deltacare.empresa.ports.out.EmpresaRepository;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class EmpresaServiceTest {

    EmpresaService empresaService;

    @MockBean
    EmpresaRepository empresaRepository;

    @BeforeEach
    public void setup() {
        this.empresaService = new EmpresaServiceImpl(empresaRepository);
    }

    @Test
    @DisplayName("Deve criar uma empresa com sucesso.")
    public void criarEmpresaTest() {

        // given | cenário
        Empresa empresa = Empresa.builder()
                .cnpj("123")
                .nome("Golden")
                .build();

        Empresa empresaCriadaBuild = Empresa.builder()
                .id(UUID.fromString("75bc9277-862d-4379-901e-c37bae7d8af3"))
                .cnpj("123")
                .nome("Golden")
                .build();

        Mockito
                .when(empresaRepository.existsByCnpj(empresa.getCnpj()))
                .thenReturn(false);

        Mockito
                .when(empresaRepository.save(empresa))
                .thenReturn(empresaCriadaBuild);

        // when | execução
        Empresa empresaCriada = empresaService.save(empresa);

        // then | verificação
        assertThat(empresaCriada.getId()).isEqualTo(UUID.fromString("75bc9277-862d-4379-901e-c37bae7d8af3"));
        assertThat(empresaCriada.getCnpj()).isEqualTo("123");
        assertThat(empresaCriada.getNome()).isEqualTo("Golden");

    }

    @Test
    @DisplayName("Deve lançar erro quando tentar criar uma empresa com CNPJ existente.")
    public void criarEmpresaComCnpjExistente() {

        // given | cenário
        Empresa empresa = Empresa.builder()
                .cnpj("123")
                .nome("Golden")
                .build();

        Mockito
                .when(empresaRepository.existsByCnpj(empresa.getCnpj()))
                .thenReturn(true);

        // when | execução
        Throwable exception = Assertions.catchThrowable(() -> empresaService.save(empresa));

        // then | verificação
        assertThat(exception)
                .isInstanceOf(EmpresaException.class)
                .hasMessage("Empresa já cadastrada.");

        Mockito
                .verify(empresaRepository, Mockito.never())
                .save(empresa);
    }
}
