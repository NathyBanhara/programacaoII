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

import br.edu.projeto.model.Anotacao;
import br.edu.projeto.model.Area;
import br.edu.projeto.model.Financas;
import br.edu.projeto.model.Produtor;
import br.edu.projeto.model.Safra;

@Stateful
public class ProdutorDAO implements Serializable
{
	@Inject
	private EntityManager em;
	
	@Inject
	private SafraDAO safraDAO;
	
	private List<Safra> listaSafras;
	
	@Inject
	private AreaDAO areaDAO;
	
	private List<Area> listaAreas;
	
	@Inject
	private FinancasDAO financasDAO;
	
	private List<Financas> listaFinancas;
	
	@Inject
	private AnotacaoDAO anotacaoDAO;
	
	private List<Anotacao> listaAnotacoes;
	
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
	
	
	//remover todas informações associadas ao produtor em outras tabelas quando for remover o produtor
	public void removerSafraAssociada(Produtor produtor)
	{
		listaSafras = listarTodosSafra(produtor);
		for (int i = 0; i < listaSafras.size(); i++)
		{
			listaAreas = listarTodosArea(listaSafras.get(i));
			listaFinancas = listarTodosFinancas(listaSafras.get(i));
			listaAnotacoes = listarTodosAnotacao(listaSafras.get(i));
			for (int j = 0; j < listaAreas.size(); j++)
			{
				this.areaDAO.excluir(listaAreas.get(j));
			}
			for (int j = 0; j < listaFinancas.size(); j++)
			{
				this.financasDAO.excluir(listaFinancas.get(j));
			}
			for (int j = 0; j < listaAnotacoes.size(); j++)
			{
				this.anotacaoDAO.excluir(listaAnotacoes.get(j));
			}
			this.safraDAO.excluir(listaSafras.get(i));
		}
	}
	
	public List<Safra> listarTodosSafra(Produtor produtor) {
		Produtor prod = produtor;
		List<Safra> safras = new ArrayList<Safra>();
		TypedQuery<Safra> q = em.createQuery("SELECT s FROM Safra s WHERE s.produtor = ?1", Safra.class);
		q.setParameter(1, prod);
		safras.addAll(q.getResultList());
	    return safras;      
	}
	
	public List<Area> listarTodosArea(Safra safra) {
		Safra s = safra;
		List<Area> areas = new ArrayList<Area>();
		TypedQuery<Area> q = em.createQuery("SELECT a FROM Area a WHERE a.safra = ?1", Area.class);
		q.setParameter(1, s);
		areas.addAll(q.getResultList());
	    return areas;    
	}
	
	public List<Financas> listarTodosFinancas(Safra safra) {
		Safra s = safra;
		List<Financas> financas = new ArrayList<Financas>();
		TypedQuery<Financas> q = em.createQuery("SELECT f FROM Financas f WHERE f.safra = ?1", Financas.class);
		q.setParameter(1, s);
		financas.addAll(q.getResultList());
	    return financas;    
	}
	
	public List<Anotacao> listarTodosAnotacao(Safra safra) {
		Safra s = safra;
		List<Anotacao> anotacoes = new ArrayList<Anotacao>();
		TypedQuery<Anotacao> q = em.createQuery("SELECT a FROM Anotacao a WHERE a.safra = ?1", Anotacao.class);
		q.setParameter(1, s);
		anotacoes.addAll(q.getResultList());
	    return anotacoes;    
	}
}
