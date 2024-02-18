package com.alinembs.minhasfinancas.model.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.alinembs.minhasfinancas.model.entity.Lancamento;
import com.alinembs.minhasfinancas.model.enums.StatusLancamento;
import com.alinembs.minhasfinancas.model.enums.TipoLancamento;

@ExtendWith( SpringExtension.class )
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class LancamentoRepositoryTest {

	
	@Autowired
	LancamentoRepository repository;
	
	
	@Autowired
	TestEntityManager entityManager;
	
	
public static Lancamento criarLancamento() {
		return Lancamento.builder()
				.ano(2024)
				.mes(2)
				.descricao("Lancamento Teste")
				.valor(BigDecimal.valueOf(10))
				.tipo(TipoLancamento.RECEITA)
				.status(StatusLancamento.PENDENTE)
				.dataCadastro(LocalDate.now())
				.build();
	}
	

private Lancamento criarePersistirumLancamento() {
		Lancamento lancamento = criarLancamento();
		
		entityManager.persist(lancamento);
		return lancamento;
	}
		
	@Test
	public void deveSalvarUmLancamento() {
		
		Lancamento lancamento = criarLancamento();
		
		lancamento = repository.save(lancamento);
		
		assertThat(lancamento.getId()).isNotNull();
	}
	@Test
	public void deveDeletarUmlancamento()
	{
		Lancamento lancamento = criarePersistirumLancamento();
		
		lancamento = entityManager.find(Lancamento.class, lancamento.getId());
		
		repository.delete(lancamento);
		
		Lancamento lancamentoInexistente = entityManager.find(Lancamento.class, lancamento.getId());
	
		assertThat(lancamentoInexistente).isNull();
	
	
	}

	
	@Test
	
	public void deveAtualizarUmLancamento() {
		
		Lancamento lancamento = criarePersistirumLancamento();
		
		lancamento.setAno(2025);
		lancamento.setDescricao("Teste Atualizar");
		lancamento.setStatus(StatusLancamento.CANCELADO);
		
		repository.save(lancamento);
		
		Lancamento lancamentoAtualizado = entityManager.find(Lancamento.class, lancamento.getId());
		
		assertThat(lancamentoAtualizado.getAno()).isEqualTo(2025);
		assertThat(lancamentoAtualizado.getDescricao()).isEqualTo("Teste Atualizar");
		assertThat(lancamentoAtualizado.getStatus()).isEqualTo(StatusLancamento.CANCELADO);
		
		
	}
	
	@Test
	public void deveBuscarumLancamentoPorId()
	{
		
		Lancamento lancamento = criarePersistirumLancamento();
		
		Optional<Lancamento> lancamentoEncontrado = repository.findById(lancamento.getId());
		
		assertThat(lancamentoEncontrado.isPresent()).isTrue();
	}
	
	
	
	
	
	
	
	
	
	
}
