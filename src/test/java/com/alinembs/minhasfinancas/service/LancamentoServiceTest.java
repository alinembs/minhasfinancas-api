package com.alinembs.minhasfinancas.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.alinembs.minhasfinancas.exception.RegraNegocioException;
import com.alinembs.minhasfinancas.model.entity.Lancamento;
import com.alinembs.minhasfinancas.model.entity.Usuario;
import com.alinembs.minhasfinancas.model.enums.StatusLancamento;
import com.alinembs.minhasfinancas.model.enums.TipoLancamento;
import com.alinembs.minhasfinancas.model.repository.LancamentoRepository;
import com.alinembs.minhasfinancas.model.repository.LancamentoRepositoryTest;
import com.alinembs.minhasfinancas.service.impl.LancamentoServiceImp;
import static org.assertj.core.api.Assertions.*;

@ExtendWith( SpringExtension.class )
@ActiveProfiles("test")
public class LancamentoServiceTest {

	@SpyBean
	LancamentoServiceImp service;
	@MockBean
	LancamentoRepository repository;
	@Test
	public void deveSalvarUmLancamento()
	{
		//cenario
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doNothing().when(service).validar(lancamentoASalvar);
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		Mockito.when(repository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);
		
		//execucao
		Lancamento lancamento = service.salvar(lancamentoASalvar);
		
		//verificacao
		assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
		assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);		
	}
	
	@Test
	public void naoDeveSalvarUmLancamentoQuandoHouverErrodeValidacao()
	{
		//cenario
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doThrow( RegraNegocioException.class).when(service).validar(lancamentoASalvar);
		
		//acao
		
		//verificacao
		catchThrowableOfType( () -> service.salvar(lancamentoASalvar),RegraNegocioException.class );
		
		Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
		
	}
	
	@Test
	public void deveAtualizarrUmLancamento()
	{
		//cenario
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		Mockito.doNothing().when(service).validar(lancamentoSalvo);
		
		Mockito.when(repository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);
		
		//execucao
	
		service.atualizar(lancamentoSalvo);
		//verificacao
		Mockito.verify(repository,Mockito.times(1)).save(lancamentoSalvo);
	}
	
	@Test
	public void deveLancarErroAoTentarAtualizarUmLancamentoQueAIndaNãoFoiSalvo()
	{
		//cenario
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		
		//acao
		
		//verificacao
		catchThrowableOfType( () -> service.atualizar(lancamentoASalvar),NullPointerException.class );
		Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
		
	}
	
	@Test 
	public void deveDeletarumLancamento()
	{
				//cenario
				Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
				lancamento.setId(1l);
				//acao
				
				service.deletar(lancamento);
				//verificacao
				Mockito.verify(repository).delete(lancamento);
				
	}
	
	@Test
	public void deveLancarErroAoTentarDeletarUmLancamentoQueAIndaNãoFoiSalvo()
	{
		//cenario
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
	
		//acao
		catchThrowableOfType( () -> service.deletar(lancamento),NullPointerException.class );
		
		//verificacao
		Mockito.verify(repository, Mockito.never()).delete(lancamento);
		
	}
	
	@Test
	public void deveFiltrarLancamentos()
	{
		//cenario
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		
		List<Lancamento> lista = Arrays.asList(lancamento);
		Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(lista);
		
		//acao
		List<Lancamento> resultado = service.buscar(lancamento);
		//verificacao
		assertThat(resultado).isNotEmpty().hasSize(1).contains(lancamento);		
	}
	
	
	@Test
	public void deveAtualizarOStatusDeumLancamento()
	{
		//cenario
				Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
				lancamento.setId(1l);
				lancamento.setStatus(StatusLancamento.PENDENTE);
				StatusLancamento novostatus = StatusLancamento.EFETIVADO;
				Mockito.doReturn(lancamento).when(service).atualizar(lancamento);
				
				//execucao
				service.atualizarStatus(lancamento, novostatus);
				
				//verificacao
				assertThat(lancamento.getStatus()).isEqualTo(novostatus);
				Mockito.verify(service).atualizar(lancamento);
	}
	
	@Test
	public void deveObterUmLancamentoPorId()
	{
		//cenario
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		Mockito.when(repository.findById(1l)).thenReturn(Optional.of(lancamento));
		//execucao
		Optional<Lancamento> resultado = service.obterPorId(1l);
		//verificacao
		assertThat(resultado.isPresent()).isTrue();
	}

	@Test
	public void deveRetornaVazioQuandoLacamentoNaoExiste()
	{
		//cenario
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		Mockito.when(repository.findById(1l)).thenReturn(Optional.empty());
		//execucao
		Optional<Lancamento> resultado = service.obterPorId(1l);
		//verificacao
		assertThat(resultado.isPresent()).isFalse();
	}
	
	@Test
	public void deveLancarErroAoValidarUmlancamento()
	{
		//cenario
				Lancamento lancamento = new Lancamento();
				
				Throwable erro = catchThrowable(()-> service.validar(lancamento));
				assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma descricao válida.");
				
				lancamento.setDescricao("");
				
				erro = catchThrowable(()-> service.validar(lancamento));
				assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma descricao válida.");
				
				lancamento.setDescricao("Salario");
				
				erro = catchThrowable(()-> service.validar(lancamento));
				assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma  mês válido.");
				
				lancamento.setMes(14);
				
				erro = catchThrowable(()-> service.validar(lancamento));
				assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma  mês válido.");
				
				lancamento.setMes(5);
				
				erro = catchThrowable(()-> service.validar(lancamento));
				assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano válido.");
				
				lancamento.setAno(200);
				
				erro = catchThrowable(()-> service.validar(lancamento));
				assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano válido.");
				
				lancamento.setAno(2000);
			
				erro = catchThrowable(()-> service.validar(lancamento));
				assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuario.");
		
				lancamento.setUsuario(new Usuario());;
				
				erro = catchThrowable(()-> service.validar(lancamento));
				assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuario.");
		
				lancamento.getUsuario().setId(1l);
				
				erro = catchThrowable(()-> service.validar(lancamento));
				assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor válido.");
		
				lancamento.setValor(BigDecimal.ZERO);
				
				erro = catchThrowable(()-> service.validar(lancamento));
				assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor válido.");
				
				lancamento.setValor(BigDecimal.valueOf(10));
				
				erro = catchThrowable(()-> service.validar(lancamento));
				assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um tipo de Lancamento.");
				
				lancamento.setTipo(null);
				
				erro = catchThrowable(()-> service.validar(lancamento));
				assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um tipo de Lancamento.");
		
				lancamento.setTipo(TipoLancamento.RECEITA);
		
	}

	
	
}
