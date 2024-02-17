package com.alinembs.minhasfinancas.service;

import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import com.alinembs.minhasfinancas.exception.ErroAutenticacao;
import com.alinembs.minhasfinancas.exception.RegraNegocioException;
import com.alinembs.minhasfinancas.model.entity.Usuario;
import com.alinembs.minhasfinancas.model.repository.UsuarioRepository;
import com.alinembs.minhasfinancas.service.impl.UsuarioServiceImp;

@ExtendWith( SpringExtension.class )
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	@SpyBean
	UsuarioServiceImp service;
	@MockBean
	UsuarioRepository repository;
	
//	@BeforeEach
//	public void setUp()
//	{
//		service =  Mockito.spy(UsuarioServiceImp.class);
////		repository = Mockito.mock(UsuarioRepository.class);
////		service = new UsuarioServiceImp(repository);
//	}

	@Test
	public void deveSalvarUsuario() {
		
		//cenario
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = criarUsuario();
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		//acao
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
		//verificacao
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
		Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("usuario");
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("usuario@email.com");
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
		
	}
	
	@Test
	public void nãoDeveSalvarUmUsuarioComEMailJaCadastrado() {
		assertThrows(RegraNegocioException .class, () -> {
		//cenario
		Usuario usuario = criarUsuario();
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail("usuario@email.com");
		
		//acao
		service.salvarUsuario(usuario);
		
		Mockito.verify( repository , Mockito.never() ).save(usuario);
		});
		
	}
	 
	
	
	@Test
	public void deveAutenticarUmUsuarioComSucesso(){
		//cenario
		String email = "usuario@email.com";
		String senha = "senha";
		
		Usuario usuario = criarUsuario();
		Mockito.when( repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		//Acao
		Usuario result = service.autenticar(email, senha);
		
		//Verificacao
		Assertions.assertThat(result).isNotNull();
		
	}
	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComEmailInformado(){
//		cenario
		Mockito.when( repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		//acao
		
		Throwable exception = Assertions.catchThrowable(()-> service.autenticar("teste@email.com", "senha"));
		 //verificacao
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Usuario não encontrado para o email informado.");
	   
		  
		
	}

	@Test
	public void deveLancarErroQuandoSenhaNãoBater(){
		
		//cenario
		
			Usuario usuario = criarUsuario();
			Mockito.when( repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
			
			//acao
			Throwable exception = Assertions.catchThrowable(()-> service.autenticar("usuario@email.com", "testesenha"));
			 //verificacao
			Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha Invalida.");
		   
		
	}

	@Test
	public void deveValidarEmail()
	{
//		
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
//		repository.deleteAll();
		service.validarEmail("email@email.com");
	}
	
	
	@Test
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		
		assertThrows(RegraNegocioException .class, () -> {
//		Usuario usuario = Usuario.builder().nome("usuario").email("usuario@email.com").build();
//		repository.save(usuario);
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		service.validarEmail("usuario@email.com"); });
	}
	
	
	public static Usuario criarUsuario() {
		return  Usuario
				 .builder()
				 .nome("usuario")
				 .email("usuario@email.com")
				 .senha("senha")
				 .id(1l)
				 .build();
	}

}
