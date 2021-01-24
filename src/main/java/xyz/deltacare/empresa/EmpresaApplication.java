package xyz.deltacare.empresa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class EmpresaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmpresaApplication.class, args);
    }

}
