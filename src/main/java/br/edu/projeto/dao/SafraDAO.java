package br.edu.projeto.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.edu.projeto.model.Safra;

@Stateful
public class SafraDAO implements Serializable
{
	@Inject
	//Cria a conexão e controla a transação com o SGBD (usado pelo Hibernate)
    private EntityManager em;
	
	public Safra encontrarId(Integer id) {
        return em.find(Safra.class, id);
    }
	
	//Query usando a linguagem HQL do Hibernate
	//Idnicada para consultas simples
	public List<Safra> listarTodos() {
	    return em.createQuery("SELECT a FROM Safra a ", Safra.class).getResultList();      
	}
	
	public void salvar(Safra s) {
		em.persist(s);
	}
	
	public void atualizar(Safra s) {
		em.merge(s);
	}
	
	public void excluir(Safra s) {
		em.remove(em.getReference(Safra.class, s.getIdSafra()));
	}
}
	
