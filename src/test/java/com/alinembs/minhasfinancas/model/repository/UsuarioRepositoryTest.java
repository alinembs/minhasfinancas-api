package com.alinembs.minhasfinancas.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.alinembs.minhasfinancas.model.entity.Usuario;


@ExtendWith( SpringExtension.class )
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest { 

	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void deveVerificarAExistenciaDeUmEmail() {
		//cenario
		
		Usuario usuario = criarUsuario();
//		repository.save(usuario);
		entityManager.persist(usuario);
		
		//acao / execucao
		boolean result = repository.existsByEmail("usuario@email.com");
		
//		verificacao
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {
		
		//cenario
//		repository.deleteAll();
		
		//acao
		boolean result = repository.existsByEmail("usuario@email.com");
		
		// verificacao
		Assertions.assertThat(result).isFalse();
		
	}
	
	 @Test
	 public void devePersistirUmUsuarioNaBaseDeDados() {
		 
		 //cenario
		 Usuario usuario = criarUsuario();
		 //acao
		 Usuario usuarioSalvo = repository.save(usuario);
		 //verificacao
		 Assertions.assertThat(usuarioSalvo.getId()).isNotNull();		 
	 }
	 
	@Test
	public void deveBuscarUmUsuarioporEmail() {
		 //cenario
		 Usuario usuario = criarUsuario();
		 entityManager.persist(usuario);
		
		 //acao
		Optional<Usuario> result= repository.findByEmail("usuario@email.com");
		
		//verificacao
		Assertions.assertThat(result.isPresent()).isTrue();
		
	}
	@Test
	public void deveRetornarVazioaoBuscarUsuarioporEmailQuandoNãoExisteNaBase() {
		
		 //acao
		Optional<Usuario> result= repository.findByEmail("usuario@email.com");
		
		//verificacao
		Assertions.assertThat(result.isPresent()).isFalse();
		
	}
	
	
	
	public static Usuario criarUsuario() {
		return  Usuario
				 .builder()
				 .nome("usuario")
				 .email("usuario@email.com")
				 .senha("senha")
				 .build();
	}
	
	
}
