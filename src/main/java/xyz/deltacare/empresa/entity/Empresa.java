package xyz.deltacare.empresa.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Empresa extends Auditable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String cnpj;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Integer produtos;

    @Column(nullable = false)
    private Integer coberturas;
}
