package br.edu.projeto.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.primefaces.PrimeFaces;

import br.edu.projeto.dao.AnotacaoDAO;
import br.edu.projeto.dao.AreaDAO;
import br.edu.projeto.dao.FinancasDAO;
import br.edu.projeto.dao.SafraDAO;
import br.edu.projeto.model.Anotacao;
import br.edu.projeto.model.Area;
import br.edu.projeto.model.Financas;
import br.edu.projeto.model.Produtor;
import br.edu.projeto.model.Safra;
import br.edu.projeto.util.SafraId;

@ViewScoped
@Named
public class CadastroSafraController implements Serializable
{
	//Anotação que marca atributo para ser gerenciado pelo CDI
	//O CDI criará uma instância do objeto automaticamente quando necessário
	@Inject
	private FacesContext facesContext;
	
	@Inject
	private SafraDAO safraDAO;
	
	private Safra safra;
	
	private List<Safra> listaSafras;
	
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
	
	@PostConstruct
	public void init() {
	  	//Verifica se usuário está autenticado e possui a permissão adequada
	  	//Verifica permissão
	  	if (!this.facesContext.getExternalContext().isUserInRole("produtor"))
	  	//!this.facesContext.getExternalContext().getRemoteUser() -> retorna o login do usuário
	  	{
	  		try
	  		{
				this.facesContext.getExternalContext().redirect("error.xhtml");
			} catch (IOException e) {e.printStackTrace();}
	  	}
    	this.listaSafras = safraDAO.listarTodos();
	  }
	
	//Guardar id da Safra em SafraId
	public void verAreas() throws IOException
	{
		SafraId.setSafra(this.safra.getIdSafra());
		facesContext.getExternalContext().redirect("menu.xhtml");
	}
	
	//Chamado pelo botão novo
	public void novoCadastro()
	{
		this.setSafra(new Safra());
	}
		
	//Chamado ao salvar cadastro de usuário (novo ou edição)
	public void salvar() {
	//Chama método de verificação se usuário é válido (regras negociais)
	//Limpa lista de permissões de usuário (é mais simples limpar e adicionar todas novamente depois)
	  		//Adiciona todas as permissões selecionadas em tela
		try
		{
			this.safra.setProdutor(this.safraDAO.acharProdutor());
			if (this.safra.getIdSafra() == null)
			{
				this.safraDAO.salvar(this.safra);
				this.facesContext.addMessage(null, new FacesMessage("Safra Criada"));
			}
			else
			{
				this.safraDAO.atualizar(this.safra);
				this.facesContext.addMessage(null, new FacesMessage("Safra Atualizada"));
			}
			//Após salvar usuário é necessário recarregar lista que popula tabela com os novos dados
			this.listaSafras = safraDAO.listarTodos();
			//Atualiza e executa elementos Javascript na tela assincronamente
			PrimeFaces.current().executeScript("PF('safraDialog').hide()");
			PrimeFaces.current().ajax().update("form:messages", "form:dt-safras");
		}
		catch (Exception e)
		{
			String errorMessage = getMensagemErro(e);
		    this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null));
		 }
	 }
	
		
	//Chamado pelo botão remover da tabela
	public void remover() {
		try {
			removerInformacoesAssociada();
			this.safraDAO.excluir(this.safra);
				//Após excluir usuário é necessário recarregar lista que popula tabela com os novos dados
				this.listaSafras = safraDAO.listarTodos();
		        //Limpa seleção de usuário
				this.safra = null;
		        this.facesContext.addMessage(null, new FacesMessage("Safra Removida"));
		        PrimeFaces.current().ajax().update("form:messages", "form:dt-safras");
	      } catch (Exception e) {
	          String errorMessage = getMensagemErro(e);
	          this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null));
	      }
	}
	
	public void removerInformacoesAssociada()
	{
		listaAreas = listarTodosArea(this.safra);
		listaFinancas = listarTodosFinancas(this.safra);
		listaAnotacoes = listarTodosAnotacao(this.safra);
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
	

	//Chamado pelo botão alterar da tabela
	public void alterar() {
	}

		
	//Captura mensagem de erro das validações do Hibernate
	private String getMensagemErro(Exception e) {
		String erro = "Falha no sistema!. Contacte o administrador do sistema.";
	    if (e == null) 
	        return erro;
	    Throwable t = e;
	    while (t != null) {
	          erro = t.getLocalizedMessage();
	          t = t.getCause();
	    }
	      return erro;
	  }

	public FacesContext getFacesContext() {
		return facesContext;
	}

	public void setFacesContext(FacesContext facesContext) {
		this.facesContext = facesContext;
	}

	public SafraDAO getSafraDAO() {
		return safraDAO;
	}

	public void setSafraDAO(SafraDAO safraDAO) {
		this.safraDAO = safraDAO;
	}

	public Safra getSafra() {
		return safra;
	}

	public void setSafra(Safra safra) {
		this.safra = safra;
	}

	public List<Safra> getListaSafras() {
		return listaSafras;
	}

	public void setListaSafras(List<Safra> listaSafras) {
		this.listaSafras = listaSafras;
	}
}
