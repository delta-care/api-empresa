package xyz.deltacare.empresa.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "beneficiario")
public class Beneficiario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String plano;
    private String subplano;
    private String nome;
    private String cpf;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Beneficiario))
            return false;

        Beneficiario other = (Beneficiario) o;

        return id != null &&
                id.equals(other.getId());
    }
}
