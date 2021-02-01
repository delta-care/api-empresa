package xyz.deltacare.empresa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@EnableCaching
@SpringBootApplication
public class EmpresaApplication {

    public static final Locale LOCALE = new Locale("pt",
            "br");

    public static void main(String[] args) {
        SpringApplication.run(EmpresaApplication.class, args);
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        var mapper = new ObjectMapper();
        var timeModule = new JavaTimeModule();
        mapper.registerModule(timeModule);
        timeModule.addDeserializer(LocalDate.class,
                new LocalDateDeserializer(
                        DateTimeFormatter.
                                ofPattern("dd/MM/yyyy",
                                        LOCALE)));
        return mapper;
    }


}
