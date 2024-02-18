package com.alinembs.minhasfinancas.api.resource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.alinembs.minhasfinancas.api.dto.UsuarioDTO;
import com.alinembs.minhasfinancas.exception.ErroAutenticacao;
import com.alinembs.minhasfinancas.exception.RegraNegocioException;
import com.alinembs.minhasfinancas.model.entity.Usuario;
import com.alinembs.minhasfinancas.service.LancamentoService;
import com.alinembs.minhasfinancas.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith( SpringExtension.class )
@ActiveProfiles("test")
@WebMvcTest(controllers = UsuarioResource.class)
@AutoConfigureMockMvc
public class UsuarioResourceTest {

	
	static final String API = "/api/usuarios";
	static final MediaType JSON = MediaType.APPLICATION_JSON;
	@Autowired
	MockMvc mvc;
	
	@MockBean
	UsuarioService service;
	
	@MockBean
	LancamentoService lancamentoService;
	
	@Test
	public void deveAutentaticarUmUsuario() throws Exception
	{
		String email = "usuario@email.com";
		String senha = "123";
		
		//cenario
		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();
		Usuario usuario = Usuario.builder().id(1l).email(email).senha(senha).build();
		
		Mockito.when(service.autenticar(email, senha)).thenReturn(usuario);
		String json = new ObjectMapper().writeValueAsString(dto);
	
		//verificacao
		MockHttpServletRequestBuilder requisicao = MockMvcRequestBuilders
		.post(API.concat("/autenticar"))
		.accept(JSON).
		contentType(JSON)
		.content(json);
		
		
		mvc.perform(requisicao)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
		.andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
		.andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()))
//		.andExpect(MockMvcResultMatchers.jsonPath("senha").value(usuario.getSenha()))
		;
		
		 
	}
	@Test
	public void deveRetornarBadRequestAoObterErrodeAutenticacao() throws Exception
	{
		String email = "usuario@email.com";
		String senha = "123";
		
		//cenario
		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();
		Usuario usuario = Usuario.builder().id(1l).email(email).senha(senha).build();
		
		Mockito.when(service.autenticar(email, senha)).thenThrow(ErroAutenticacao.class);
		String json = new ObjectMapper().writeValueAsString(dto);
	
		//verificacao
		MockHttpServletRequestBuilder requisicao = MockMvcRequestBuilders
		.post(API.concat("/autenticar"))
		.accept(JSON).
		contentType(JSON)
		.content(json);
		
		
		mvc.perform(requisicao)
		.andExpect(MockMvcResultMatchers.status().isBadRequest())
		
		;
		
		 
	}
	
	@Test
	public void deveCriarUmusuario() throws Exception
	{
		String email = "usuario@email.com";
		String senha = "123";
		
		//cenario
		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();
		Usuario usuario = Usuario.builder().id(1l).email(email).senha(senha).build();
		
		Mockito.when(service.salvarUsuario(Mockito.any(Usuario.class))).thenReturn(usuario);
		String json = new ObjectMapper().writeValueAsString(dto);
	
		//verificacao
		MockHttpServletRequestBuilder requisicao = MockMvcRequestBuilders
		.post(API)
		.accept(JSON).
		contentType(JSON)
		.content(json);
		
		mvc.perform(requisicao)
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
		.andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
		.andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()))
//		.andExpect(MockMvcResultMatchers.jsonPath("senha").value(usuario.getSenha()))
		;
		
		 
	}
	
	@Test
	public void deveRetornarBadRequestaoCriarUmusuarioInvalido() throws Exception
	{
		String email = "usuario@email.com";
		String senha = "123";
		
		//cenario
		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();

		
		Mockito.when(service.salvarUsuario(Mockito.any(Usuario.class))).thenThrow(RegraNegocioException.class);;
		String json = new ObjectMapper().writeValueAsString(dto);
	
		//verificacao
		MockHttpServletRequestBuilder requisicao = MockMvcRequestBuilders
		.post(API)
		.accept(JSON).
		contentType(JSON)
		.content(json);
		
		mvc.perform(requisicao)
		.andExpect(MockMvcResultMatchers.status().isBadRequest())
		;
		
		 
	}
	
	
}
