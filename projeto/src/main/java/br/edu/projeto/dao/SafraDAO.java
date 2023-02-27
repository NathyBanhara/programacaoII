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
import br.edu.projeto.model.Produtor;
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
}
	
