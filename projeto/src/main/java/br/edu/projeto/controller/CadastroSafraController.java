package br.edu.projeto.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.primefaces.PrimeFaces;

import br.edu.projeto.dao.SafraDAO;
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
		
	//Chamado ao salvar cadastro de safra (novo ou edição)
	public void salvar() {
	//Chama método de verificação se safra é válido (regras negociais)
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
			//Após salvar é necessário recarregar lista que popula tabela com os novos dados
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
			this.safraDAO.removerInformacoesAssociada(this.safra);
			this.safraDAO.excluir(this.safra);
				//Após excluir safra é necessário recarregar lista que popula tabela com os novos dados
				this.listaSafras = safraDAO.listarTodos();
		        //Limpa seleção de safra
				this.safra = null;
		        this.facesContext.addMessage(null, new FacesMessage("Safra Removida"));
		        PrimeFaces.current().ajax().update("form:messages", "form:dt-safras");
	      } catch (Exception e) {
	          String errorMessage = getMensagemErro(e);
	          this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null));
	      }
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
