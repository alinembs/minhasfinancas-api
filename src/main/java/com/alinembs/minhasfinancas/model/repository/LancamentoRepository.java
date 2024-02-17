package com.alinembs.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alinembs.minhasfinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento,Long> {

}
