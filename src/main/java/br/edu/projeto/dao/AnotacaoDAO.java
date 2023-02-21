package br.edu.projeto.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.edu.projeto.model.Anotacao;

@Stateful
public class AnotacaoDAO implements Serializable
{
	@Inject
	//Cria a conexão e controla a transação com o SGBD (usado pelo Hibernate)
    private EntityManager em;
	
	//Query usando a linguagem HQL do Hibernate
	//Idnicada para consultas simples
	public List<Anotacao> listarTodos() {
	    return em.createQuery("SELECT a FROM Anotacao a ", Anotacao.class).getResultList();      
	}

	public void salvar(Anotacao a) {
		em.persist(a);
	}

	public void atualizar(Anotacao a) {
		em.merge(a);
	}

	public void excluir(Anotacao a) {
		em.remove(em.getReference(Anotacao.class, a.getIdAnotacao()));
	}
}