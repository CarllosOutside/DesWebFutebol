package com.jogador.futebol.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jogador.futebol.models.Jogador;
import com.jogador.futebol.models.Pagamento;
import com.jogador.futebol.repositories.JogadorRepo;
import com.jogador.futebol.repositories.PagamentoRepo;

@RestController
@RequestMapping("/api")
public class PagamentoController {

	@Autowired
	PagamentoRepo pagrepo;

	@Autowired
	JogadorRepo jogrepo;

	//cria um novo pagamento para um jogador
	@PostMapping("/jogador/{id}/pagamento")
	public ResponseEntity<Pagamento> createPagamento(@PathVariable("id") long id, @RequestBody Pagamento pagamento) {
		// procura jogador
		Optional<Jogador> jogadordata = jogrepo.findById(id);
		if (jogadordata.isPresent()) {
			try {
				//cria um pagamento
				Pagamento _pagamento = pagrepo.save(
						new Pagamento(pagamento.getAno(), pagamento.getMes(), pagamento.getValor(), jogadordata.get()));
				return new ResponseEntity<>(_pagamento, HttpStatus.CREATED);
			} catch (Exception e) {
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			//jogador nao encontrado
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	//Lista de pagamentos de um jogador
	@GetMapping("/jogador/{id}/pagamento") // id do jogador
	public ResponseEntity<List<Pagamento>> getPagamentoByJogadoId(@PathVariable("id") long id)
	{
		// procura jogador
		Optional<Jogador> jogadordata = jogrepo.findById(id);
		if (jogadordata.isPresent()) {
			// cria uma lista de pagamentos feitos ao jogador
			List<Pagamento> pagamentos = pagrepo.findByJogador(jogadordata.get());
			if (pagamentos.isEmpty() || jogadordata.get() == null) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			// retorna lista de pagamentos do jogador
			return new ResponseEntity<>(pagamentos, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}
	
	//deleta um pagamento por idp
	@DeleteMapping("/jogador/{idj}/pagamento/{idp}")
    public ResponseEntity<HttpStatus> deleteJogador(@PathVariable("idj") long idj, @PathVariable("idp") long idp) {
        try {
            pagrepo.deleteById(idp);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
	@DeleteMapping("/jogador/{id}/pagamento")
    public ResponseEntity<HttpStatus> deleteAllPagamentos(@PathVariable("id") long id) {
		// procura jogador
				Optional<Jogador> jogadordata = jogrepo.findById(id);
				if (jogadordata.isPresent()) {	
					try {
						//deleta pagamentos de um jogador
			            pagrepo.deleteByJogador(jogadordata.get());
			            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			        } catch (Exception e) {
			        	System.out.println("erro: "+ e);
			            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			        }
				} else {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
    }

}
