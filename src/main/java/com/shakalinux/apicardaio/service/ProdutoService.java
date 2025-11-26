package com.shakalinux.apicardaio.service;

import com.shakalinux.apicardaio.exception.NotFoundException;
import com.shakalinux.apicardaio.model.Categoria;
import com.shakalinux.apicardaio.model.Produto;
import com.shakalinux.apicardaio.repository.CategoriaRepository;
import com.shakalinux.apicardaio.repository.PedidoRepository;
import com.shakalinux.apicardaio.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {
    @Autowired
    private ProdutoRepository  produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public Produto save(Produto produto){
        return produtoRepository.save(produto);
    }
    public Optional<Produto> findById(Long id){
        return produtoRepository.findById(id);
    }

    public List<Produto> findAll(){
        return produtoRepository.findAll();
    }

    public void  delete(Produto produto){
        produtoRepository.delete(produto);
    }

    public Produto atualizar(Long id, Produto novo) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));

        produto.setNome(novo.getNome());
        produto.setDescricao(novo.getDescricao());
        produto.setPreco(novo.getPreco());
        produto.setTipo(novo.getTipo());

        if (novo.getCategoria() != null) {
            Categoria categoria = categoriaRepository.findById(novo.getCategoria().getIdCategoria())
                    .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));
            produto.setCategoria(categoria);
        }

        return produtoRepository.save(produto);
    }


    public Produto trocarCategoria(Long idProduto, Long idCategoria) {
        Produto produto = produtoRepository.findById(idProduto)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));

        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));

        produto.setCategoria(categoria);

        return produtoRepository.save(produto);
    }

    public void deletar(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));

        produtoRepository.delete(produto);
    }
}
