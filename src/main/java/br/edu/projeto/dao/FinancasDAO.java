package br.edu.projeto.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.edu.projeto.model.Financas;

@Stateful
public class FinancasDAO implements Serializable
{
	@Inject
	//Cria a conexão e controla a transação com o SGBD (usado pelo Hibernate)
    private EntityManager em;
	
	//Query usando a linguagem HQL do Hibernate
	//Idnicada para consultas simples
	public List<Financas> listarTodos() {
	    return em.createQuery("SELECT a FROM Financas a ", Financas.class).getResultList();      
	}
	
	public void salvar(Financas f) {
		em.persist(f);
	}
	
	public void atualizar(Financas f) {
		em.merge(f);
	}
	
	public void excluir(Financas f) {
		em.remove(em.getReference(Financas.class, f.getIdFinancas()));
	}
}