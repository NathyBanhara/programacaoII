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
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;

import org.primefaces.PrimeFaces;

import br.edu.projeto.dao.AnotacaoDAO;
import br.edu.projeto.dao.AreaDAO;
import br.edu.projeto.dao.FinancasDAO;
import br.edu.projeto.dao.ProdutorDAO;
import br.edu.projeto.dao.SafraDAO;
import br.edu.projeto.model.Anotacao;
import br.edu.projeto.model.Area;
import br.edu.projeto.model.Financas;
import br.edu.projeto.model.Produtor;
import br.edu.projeto.model.Safra;

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
					this.usuario.setSenha(this.passwordHash.generate(this.usuario.getSenha().toCharArray()));
					this.produtorDAO.salvar(this.usuario);
					this.facesContext.addMessage(null, new FacesMessage("Produtor Criado"));
				}
			}
			else
			{
				if (produtorValidoAlterar())
				{
					this.usuario.setSenha(this.passwordHash.generate(this.usuario.getSenha().toCharArray()));
					this.produtorDAO.atualizar(this.usuario);
				    this.facesContext.addMessage(null, new FacesMessage("Produtor Atualizado"));
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
		if (this.usuario.getNovo() == null && !this.produtorDAO.ehProdutorUnico(this.usuario.getCpf())) {
				this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Este cpf já está em uso.", null));
				return false;
			}
			return true;
	}
	
	//Chamado pelo botão remover da tabela
	public void remover() {
		try {
			removerSafraAssociada();
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
	
	public void removerSafraAssociada()
	{
		listaSafras = listarTodosSafra();
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
	
	public List<Safra> listarTodosSafra() {
		Produtor prod = this.usuario;
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