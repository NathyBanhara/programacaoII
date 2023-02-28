package br.edu.projeto.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.edu.projeto.model.Area;
import br.edu.projeto.model.Safra;
import br.edu.projeto.util.SafraId;

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
	
	public Area encontrarArea(String ender)
	{
		List<Area> area = new ArrayList<Area>();
		TypedQuery<Area> q = em.createQuery("SELECT a FROM Area a WHERE a.ender = ?1", Area.class);
		q.setParameter(1, ender);
		area.addAll(q.getResultList());
		return area.get(0);
	}
	
	//acha safra correspondente a área
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
	//Indicada para consultas simples
	public List<Area> listarTodos() {
		Safra s = acharSafra();
		List<Area> areas = new ArrayList<Area>();
		TypedQuery<Area> q = em.createQuery("SELECT a FROM Area a WHERE a.safra = ?1", Area.class);
		q.setParameter(1, s);
		areas.addAll(q.getResultList());
	    return areas;      
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