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
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;

import org.primefaces.PrimeFaces;

import br.edu.projeto.dao.ProdutorDAO;
import br.edu.projeto.model.Produtor;

@ViewScoped
@Named
public class CadastroProdutorController implements Serializable
{
	//Anotação que marca atributo para ser gerenciado pelo CDI
	//O CDI criará uma instância do objeto automaticamente quando necessário
	@Inject
	private FacesContext facesContext;
	
	@Inject
	//Cria a conexão e controla a transação com o SGBD (usado pelo Hibernate)
    private EntityManager em;
	
	//atributos que não podem ser serializáveis (normalmente dependências externas) devem ser marcados como transient 
	//(eles são novamente criados a cada nova requisição independente do escopo da classe)
	@Inject
    transient private Pbkdf2PasswordHash passwordHash;
	
	@Inject
	private ProdutorDAO produtorDAO;
	
	private Produtor usuario;
	
	private List<Produtor> listaProdutores;
		
	@PostConstruct
	public void init() {
	  	//Verifica se usuário está autenticado e possui a permissão adequada
	  	//Verifica permissão
	  	if (!this.facesContext.getExternalContext().isUserInRole("admin"))
	  	//!this.facesContext.getExternalContext().getRemoteUser() -> retorna o login do usuário
	  	{
	  		try
	  		{
				this.facesContext.getExternalContext().redirect("login-error.xhtml");
			} catch (IOException e) {e.printStackTrace();}
	  	}
	  	this.listaProdutores = produtorDAO.listarTodos();
	  }
	
	//Chamado pelo botão novo
	public void novoCadastro()
	{
		this.setUsuario(new Produtor());
	}
		
	//Chamado ao salvar cadastro de usuário (novo ou edição)
	public void salvar() {
	//Chama método de verificação se usuário é válido (regras negociais)
		try
		{
			if (this.usuario.getNovo() == null) {
				if (produtorValido())
				{
					try {
						this.usuario.setSenha(this.passwordHash.generate(this.usuario.getSenha().toCharArray()));
						this.produtorDAO.salvar(this.usuario);
						this.facesContext.addMessage(null, new FacesMessage("Produtor Criado"));
					}
					catch (Exception e)
					{
						String errorMessage = getMensagemErro(e);
						this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null));
					}
				}
			}
			else
			{
				if (produtorValidoAlterar())
				{
					try {
						this.usuario.setSenha(this.passwordHash.generate(this.usuario.getSenha().toCharArray()));
						this.produtorDAO.atualizar(this.usuario);
						this.facesContext.addMessage(null, new FacesMessage("Produtor Atualizado"));
					}
					catch (Exception e)
					{
						String errorMessage = getMensagemErro(e);
						this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null));
					}
				}
			}
			//Após salvar usuário é necessário recarregar lista que popula tabela com os novos dados
			this.listaProdutores = produtorDAO.listarTodos();
			//Atualiza e executa elementos Javascript na tela assincronamente
			PrimeFaces.current().executeScript("PF('produtorDialog').hide()");
			PrimeFaces.current().ajax().update("form:messages", "form:dt-produtores");
		}
		catch (Exception e)
		{
			String errorMessage = getMensagemErro(e);
			this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null));
		}
	 }
	
	//procura, após excluir usuário, se há algum outro com o mesmo cpf, retorna false se for o caso 
	public boolean produtorValidoAlterar()
	{
		Produtor altUsuario = this.usuario;
		try {
			this.produtorDAO.excluir(this.usuario);
				//Após excluir usuário é necessário recarregar lista que popula tabela com os novos dados
				this.listaProdutores = produtorDAO.listarTodos();
		        //Limpa seleção de usuário
				this.usuario = null;
		        PrimeFaces.current().ajax().update("form:messages", "form:dt-produtores");
	      } catch (Exception e) {
	          String errorMessage = getMensagemErro(e);
	          this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null));
	      }
		if (!this.produtorDAO.ehProdutorUnico(altUsuario.getCpf())) {
			this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Este cpf já está em uso.", null));
			this.usuario = altUsuario;
			this.produtorDAO.salvar(this.usuario);
			this.listaProdutores = produtorDAO.listarTodos();
			PrimeFaces.current().executeScript("PF('produtorDialog').hide()");
			PrimeFaces.current().ajax().update("form:messages", "form:dt-produtores");
			return false;
		}
		this.usuario = altUsuario;
		this.produtorDAO.salvar(this.usuario);
		this.listaProdutores = produtorDAO.listarTodos();
		PrimeFaces.current().executeScript("PF('produtorDialog').hide()");
		PrimeFaces.current().ajax().update("form:messages", "form:dt-produtores");
		return true;
	}
	
	//Realiza validações adicionais (não relizadas no modelo) e/ou complexas/interdependentes
	private boolean produtorValido()
	{
		if (!this.produtorDAO.ehProdutorUnico(this.usuario.getCpf())) {
				this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Este cpf já está em uso.", null));
				return false;
			}
			return true;
	}
	
	//Chamado pelo botão remover da tabela
	public void remover() {
		try {
			this.produtorDAO.removerSafraAssociada(this.usuario);;
			this.produtorDAO.excluir(this.usuario);
			//Após excluir usuário é necessário recarregar lista que popula tabela com os novos dados
			this.listaProdutores = produtorDAO.listarTodos();
		    //Limpa seleção de usuário
			this.usuario = null;
		    this.facesContext.addMessage(null, new FacesMessage("Produtor Removido"));
		    PrimeFaces.current().ajax().update("form:messages", "form:dt-produtores");
	      } catch (Exception e) {
	          String errorMessage = getMensagemErro(e);
	          this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null));
	      }
		}

	//Chamado pelo botão alterar da tabela
	public void alterar() {
		this.usuario.setNovo(1);
		this.usuario.setEmail("");
		this.usuario.setSenha("");
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

	public Pbkdf2PasswordHash getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(Pbkdf2PasswordHash passwordHash) {
		this.passwordHash = passwordHash;
	}

	public ProdutorDAO getProdutorDAO() {
		return produtorDAO;
	}

	public void setProdutorDAO(ProdutorDAO produtorDAO) {
		this.produtorDAO = produtorDAO;
	}

	public Produtor getUsuario() {
		return usuario;
	}

	public void setUsuario(Produtor usuario) {
		this.usuario = usuario;
	}

	public List<Produtor> getListaProdutores() {
		return listaProdutores;
	}

	public void setListaProdutores(List<Produtor> listaProdutores) {
		this.listaProdutores = listaProdutores;
	}
}