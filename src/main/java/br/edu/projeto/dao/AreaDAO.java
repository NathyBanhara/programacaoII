package br.edu.projeto.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.edu.projeto.model.Area;

@Stateful
public class AreaDAO implements Serializable
{
	@Inject
	//Cria a conexão e controla a transação com o SGBD (usado pelo Hibernate)
    private EntityManager em;
	
	//Query usando a API Criteria do Hibernate
	//Indicada para consultas complexas
	public Boolean ehAreaUnico(String ender) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Area> criteria = cb.createQuery(Area.class);
        Root<Area> area = criteria.from(Area.class);
        criteria.select(area);
        criteria.where(cb.like(area.get("ender"), ender));
        if (em.createQuery(criteria).getResultList().isEmpty())
        	return true;
        return false;
    }
	
	//Query usando a linguagem HQL do Hibernate
	//Idnicada para consultas simples
	public List<Area> listarTodos() {
	    return em.createQuery("SELECT a FROM Area a ", Area.class).getResultList();      
	}

	public void salvar(Area a) {
		em.persist(a);
	}

	public void atualizar(Area a) {
		em.merge(a);
	}

	public void excluir(Area a) {
		em.remove(em.getReference(Area.class, a.getEnder()));
	}
}