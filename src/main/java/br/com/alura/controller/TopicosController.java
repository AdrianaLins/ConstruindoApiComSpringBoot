package br.com.alura.controller;


import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.controller.dto.TopicoDto;
import br.com.alura.controller.form.AtualizacaoTopicoForm;
import br.com.alura.controller.form.TopicoForm;
import br.com.alura.controller.repository.CursoRepository;
import br.com.alura.modelo.Topico;
import br.com.alura.repository.TopicoRepository;

@RestController
@RequestMapping(value = "/topicos")
public class TopicosController {
	
	@Autowired
	private TopicoRepository topicoRepository;
	
	@Autowired
	private CursoRepository cursoRepository;

	//Método pra Listar
	//Não é uma boa devolver as classes de domínio no controller e sim criar um DTO para informar somente os campos que precisam devolver ao cliente. 
	@GetMapping
	public List<TopicoDto> lista(String nomeCurso) {
		if (nomeCurso == null) {
			List<Topico> topicos = topicoRepository.findAll();
			return TopicoDto.converter(topicos);
			
		} else {
			List<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso);
			return TopicoDto.converter(topicos);
		}
		
		
		
	}
	
	//Boas práticas do rest para devolver 201 
	@PostMapping
	@Transactional
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) { //@RequestBody o parametro é pra pegar no corpo da requisição
		Topico topico = form.converter(cursoRepository); //salvando no banco
		topicoRepository.save(topico);
		
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<TopicoDto> detalhar(@PathVariable("id") Long codigo) {
		
		Optional<Topico> topico = topicoRepository.findById(codigo);
		if(topico.isPresent()) {
			return ResponseEntity.ok(new TopicoDto(topico.get()));
		}
		return ResponseEntity.notFound().build();
		
		
	}
	
	@PutMapping("/{id}")
	@Transactional //avisa ao spring que é pra comitar a transação no final desse método
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) {
		Optional<Topico> optional = topicoRepository.findById(id);
		if(optional.isPresent()) {
			Topico topico = form.atualizar(id, topicoRepository);
			return ResponseEntity.ok(new TopicoDto(topico));
		}
		
		return ResponseEntity.notFound().build();					
		
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remover(@PathVariable Long id) {
		Optional<Topico> optional = topicoRepository.findById(id);
		if(optional.isPresent()) {
			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.notFound().build();
		
	}
	
	
	
	
}



