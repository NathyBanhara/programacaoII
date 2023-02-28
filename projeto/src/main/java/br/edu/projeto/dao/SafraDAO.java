package br.edu.projeto.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.edu.projeto.model.Anotacao;
import br.edu.projeto.model.Area;
import br.edu.projeto.model.Financas;
import br.edu.projeto.model.Produtor;
import br.edu.projeto.model.Safra;

@Stateful
public class SafraDAO implements Serializable
{
	@Inject
	//Cria a conexão e controla a transação com o SGBD (usado pelo Hibernate)
    private EntityManager em;
	
	@Inject
	private AreaDAO areaDAO;
	
	private List<Area> listaAreas;
	
	@Inject
	private FinancasDAO financasDAO;
	
	private List<Financas> listaFinancas;
	
	@Inject
	private AnotacaoDAO anotacaoDAO;
	
	private List<Anotacao> listaAnotacoes;
	
	public Safra encontrarId(Integer id) {
        return em.find(Safra.class, id);
    }
	
	public Produtor acharProdutor() {
		String cpfProdutor = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
		List<Produtor> prod = new ArrayList<Produtor>();
		TypedQuery<Produtor> q = em.createQuery("SELECT p FROM Produtor p WHERE p.cpf = ?1", Produtor.class);
		q.setParameter(1, cpfProdutor);
		prod.addAll(q.getResultList());
		return prod.get(0);
	}
	
	//Query usando a linguagem HQL do Hibernate
	//Idnicada para consultas simples
	public List<Safra> listarTodos() {
		Produtor prod = acharProdutor();
		List<Safra> safras = new ArrayList<Safra>();
		TypedQuery<Safra> q = em.createQuery("SELECT s FROM Safra s WHERE s.produtor = ?1", Safra.class);
		q.setParameter(1, prod);
		safras.addAll(q.getResultList());
	    return safras;      
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
	
	//remover todas as informações associadas a safra em outras tabelas quando for remover uma safra
	public void removerInformacoesAssociada(Safra safra)
	{
		listaAreas = listarTodosArea(safra);
		listaFinancas = listarTodosFinancas(safra);
		listaAnotacoes = listarTodosAnotacao(safra);
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
	
