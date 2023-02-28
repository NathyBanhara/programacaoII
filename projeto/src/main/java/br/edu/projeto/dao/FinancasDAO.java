package br.edu.projeto.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.edu.projeto.model.Area;
import br.edu.projeto.model.Financas;
import br.edu.projeto.model.Produtor;
import br.edu.projeto.model.Safra;
import br.edu.projeto.util.SafraId;

@Stateful
public class FinancasDAO implements Serializable
{
	@Inject
	//Cria a conexão e controla a transação com o SGBD (usado pelo Hibernate)
    private EntityManager em;
	
	public Produtor acharProdutor() {
		String cpfProdutor = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
		List<Produtor> prod = new ArrayList<Produtor>();
		TypedQuery<Produtor> q = em.createQuery("SELECT p FROM Produtor p WHERE p.cpf = ?1", Produtor.class);
		q.setParameter(1, cpfProdutor);
		prod.addAll(q.getResultList());
		return prod.get(0);
	}
	
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
	public List<Financas> listarTodos() {
		Safra s = acharSafra();
		List<Financas> financas = new ArrayList<Financas>();
		TypedQuery<Financas> q = em.createQuery("SELECT f FROM Financas f WHERE f.safra = ?1", Financas.class);
		q.setParameter(1, s);
		financas.addAll(q.getResultList());
		for (int i = 0; i < financas.size(); i++)
		{
			if (financas.get(i).getDataPag() == null)
				financas.get(i).setOpcao("Receita");
			else financas.get(i).setOpcao("Despesa");
		}
	    return financas;       
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