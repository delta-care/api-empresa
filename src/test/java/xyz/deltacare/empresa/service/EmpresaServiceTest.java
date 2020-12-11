package xyz.deltacare.empresa.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import xyz.deltacare.empresa.dto.EmpresaDto;
import xyz.deltacare.empresa.domain.Empresa;
import xyz.deltacare.empresa.exception.EmpresaExistenteException;
import xyz.deltacare.empresa.repository.EmpresaRepository;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class EmpresaServiceTest {

    @Autowired
    @Qualifier("empresaService")
    IEmpresaService service;

    @MockBean
    EmpresaRepository repository;

    @Test
    @DisplayName("CRIAR: Deve criar uma empresa.")
    public void criarEmpresaTest() {

        // given | cenário
        EmpresaDto empresaDtoEnviada = EmpresaDto.builder()
                .cnpj("38.067.491/0001-60")
                .nome("Bruno e Oliver Contábil ME")
                .build();
        Empresa empresaEnviada = Empresa.builder()
                .cnpj("38.067.491/0001-60")
                .nome("Bruno e Oliver Contábil ME")
                .build();
        Empresa empresaEsperada = Empresa.builder()
                .id(UUID.randomUUID())
                .cnpj("38.067.491/0001-60")
                .nome("Bruno e Oliver Contábil ME")
                .build();

        // when | execução
        when(repository.existsByCnpj(empresaEnviada.getCnpj())).thenReturn(false);
        when(repository.save(empresaEnviada)).thenReturn(empresaEsperada);
        EmpresaDto empresaDtoCriada = service.criar(empresaDtoEnviada);

        // then | verificação
        assertThat(empresaDtoCriada.getId()).isEqualTo(empresaEsperada.getId());
        assertThat(empresaDtoCriada.getCnpj()).isEqualTo(empresaEsperada.getCnpj());
        assertThat(empresaDtoCriada.getNome()).isEqualTo(empresaEsperada.getNome());

    }

    @Test
    @DisplayName("CRIAR: Deve lançar erro quando tentar criar uma empresa com CNPJ existente.")
    public void criarEmpresaComCnpjExistente() {

        // given | cenário
        EmpresaDto empresaDto = EmpresaDto.builder()
                .cnpj("38.067.491/0001-60")
                .nome("Bruno e Oliver Contábil ME")
                .build();

        // when | execução
        when(repository.existsByCnpj(empresaDto.getCnpj())).thenReturn(true);
        Throwable exception = Assertions.catchThrowable(() -> service.criar(empresaDto));


        // then | verificação
        assertThat(exception)
                .isInstanceOf(EmpresaExistenteException.class)
                .hasMessage(String.format("Empresa com CNPJ %s já existe.", empresaDto.getCnpj()));

    }

}
