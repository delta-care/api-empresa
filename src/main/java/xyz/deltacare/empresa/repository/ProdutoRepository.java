package xyz.deltacare.empresa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.deltacare.empresa.domain.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
