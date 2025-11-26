package com.shakalinux.apicardaio.service;

import com.shakalinux.apicardaio.exception.NotFoundException;
import com.shakalinux.apicardaio.model.Categoria;
import com.shakalinux.apicardaio.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    public Categoria findById(Long id){
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria ID " + id + " não encontrada"));
    }

    public List<Categoria> findAll(){
        return categoriaRepository.findAll();
    }

    public Categoria save(Categoria categoria){
        categoriaRepository.save(categoria);
        return categoria;
    }

    public Categoria atualizar(Long id, Categoria nova) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));

        categoria.setNome(nova.getNome());

        return categoriaRepository.save(categoria);
    }

    public void remover(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));

        categoriaRepository.delete(categoria);
    }
}
