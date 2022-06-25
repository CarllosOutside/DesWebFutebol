package com.jogador.futebol.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jogador.futebol.models.Jogador;

public interface JogadorRepo extends JpaRepository<Jogador, Long> {

	List<Jogador> findByNome(String nome);
}
