package br.edu.projeto.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.edu.projeto.model.Produtor;

@Stateful
public class ProdutorDAO implements Serializable
{
	@Inject
	private EntityManager em;
	
	public Produtor encontrarCpf(String cpf)
	{
		return em.find(Produtor.class, cpf);
	}
	
	//Query usando a API Criteria do Hibernate
	//Indicada para consultas complexas
	public Boolean ehProdutorUnico(String p) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Produtor> criteria = cb.createQuery(Produtor.class);
        Root<Produtor> produtor = criteria.from(Produtor.class);
        criteria.select(produtor);
        criteria.where(cb.like(produtor.get("cpf"), p));
        if (em.createQuery(criteria).getResultList().isEmpty())
        	return true;
        return false;
    }
	
	//Query usando a linguagem HQL do Hibernate
	//Idnicada para consultas simples
	public List<Produtor> listarTodos() {
	    return em.createQuery("SELECT a FROM Produtor a ", Produtor.class).getResultList();      
	}
	
	public void salvar(Produtor p) {
		em.persist(p);
	}
	
	public void atualizar(Produtor p) {
		em.merge(p);
	}
	
	public void excluir(Produtor p) {
		em.remove(em.getReference(Produtor.class, p.getCpf()));
	}
}
