package br.edu.projeto.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.edu.projeto.model.Anotacao;
import br.edu.projeto.model.Financas;
import br.edu.projeto.model.Safra;
import br.edu.projeto.util.SafraId;

@Stateful
public class AnotacaoDAO implements Serializable
{
	@Inject
	//Cria a conexão e controla a transação com o SGBD (usado pelo Hibernate)
    private EntityManager em;
	
	public Safra acharSafra()
	{
		Integer safraId = SafraId.getSafra();
		List<Safra> safra = new ArrayList<Safra>();
		TypedQuery<Safra> q = em.createQuery("SELECT s FROM Safra s WHERE s.idSafra = ?1", Safra.class);
		q.setParameter(1, safraId);
		safra.addAll(q.getResultList());
		return safra.get(0);
	}
	
	//Query usando a linguagem HQL do Hibernate
	//Idnicada para consultas simples
	public List<Anotacao> listarTodos() {
		Safra s = acharSafra();
		List<Anotacao> anotacoes = new ArrayList<Anotacao>();
		TypedQuery<Anotacao> q = em.createQuery("SELECT a FROM Anotacao a WHERE a.safra = ?1", Anotacao.class);
		q.setParameter(1, s);
		anotacoes.addAll(q.getResultList());
	    return anotacoes;       
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