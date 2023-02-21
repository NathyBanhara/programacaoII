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

import org.primefaces.PrimeFaces;

import br.edu.projeto.dao.AnotacaoDAO;
import br.edu.projeto.model.Anotacao;

@ViewScoped
@Named
public class CadastroAnotacaoController implements Serializable
{
	//Anotação que marca atributo para ser gerenciado pelo CDI
	//O CDI criará uma instância do objeto automaticamente quando necessário
	@Inject
	private FacesContext facesContext;
	
	@Inject
	private AnotacaoDAO anotacaoDAO;
	
	private Anotacao anotacao;
	
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
				this.facesContext.getExternalContext().redirect("login-error.xhtml");
			} catch (IOException e) {e.printStackTrace();}
	  	}
	  	this.listaAnotacoes = anotacaoDAO.listarTodos();
	  }
	
	//Chamado pelo botão novo
	public void novoCadastro()
	{
		this.setAnotacao(new Anotacao());
	}
		
	//Chamado ao salvar cadastro de usuário (novo ou edição)
	public void salvar() {
	//Chama método de verificação se usuário é válido (regras negociais)
	//Limpa lista de permissões de usuário (é mais simples limpar e adicionar todas novamente depois)
	  		//Adiciona todas as permissões selecionadas em tela
		try
		{
			if (this.anotacao.getIdAnotacao() == null)
			{
				this.anotacaoDAO.salvar(this.anotacao);
				this.facesContext.addMessage(null, new FacesMessage("Anotações Criada"));
			}
			else
			{
				this.anotacaoDAO.atualizar(this.anotacao);
				this.facesContext.addMessage(null, new FacesMessage("Anotacao Atualizada"));
			}
			//Após salvar usuário é necessário recarregar lista que popula tabela com os novos dados
			this.listaAnotacoes = anotacaoDAO.listarTodos();
			//Atualiza e executa elementos Javascript na tela assincronamente
			PrimeFaces.current().executeScript("PF('usuarioDialog').hide()");
			PrimeFaces.current().ajax().update("form:messages", "form:dt-usuarios");
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
			this.anotacaoDAO.excluir(this.anotacao);
				//Após excluir usuário é necessário recarregar lista que popula tabela com os novos dados
				this.listaAnotacoes = anotacaoDAO.listarTodos();
		        //Limpa seleção de usuário
				this.anotacao = null;
		        this.facesContext.addMessage(null, new FacesMessage("Anotação Removida"));
		        PrimeFaces.current().ajax().update("form:messages", "form:dt-usuarios");
	      } catch (Exception e) {
	          String errorMessage = getMensagemErro(e);
	          this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null));
	      }
		}
/*
	//Chamado pelo botão alterar da tabela
	public void alterar() {
		this.permissoesSelecionadas.clear();
		for (TipoPermissao p: this.usuario.getPermissoes())
			this.permissoesSelecionadas.add(p.getId());
		this.usuario.setSenha("");
	}
*/
		
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

	public AnotacaoDAO getAnotacaoDAO() {
		return anotacaoDAO;
	}

	public void setAnotacaoDAO(AnotacaoDAO anotacaoDAO) {
		this.anotacaoDAO = anotacaoDAO;
	}

	public Anotacao getAnotacao() {
		return anotacao;
	}

	public void setAnotacao(Anotacao anotacao) {
		this.anotacao = anotacao;
	}

	public List<Anotacao> getListaAnotacoes() {
		return listaAnotacoes;
	}

	public void setListaAnotacoes(List<Anotacao> listaAnotacoes) {
		this.listaAnotacoes = listaAnotacoes;
	}
}
