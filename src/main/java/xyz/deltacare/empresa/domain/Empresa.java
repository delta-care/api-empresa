package xyz.deltacare.empresa.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import xyz.deltacare.empresa.domain.audit.Auditable;

import javax.persistence.*;
import java.util.UUID;

@Data
@Table
@Entity
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Empresa extends Auditable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String cnpj;

    @Column(nullable = false)
    private String nome;

}
