package com.shakalinux.apicardaio.repository;

import com.shakalinux.apicardaio.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
