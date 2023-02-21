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

import br.edu.projeto.dao.AreaDAO;
import br.edu.projeto.model.Area;


//Escopo do objeto da classe (Bean)
//ApplicationScoped é usado quando o objeto é único na aplicação (compartilhado entre usuários), permanece ativo enquanto a aplicação estiver ativa
//SessionScoped é usado quando o objeto é único por usuário, permanece ativo enquanto a sessão for ativa
//ViewScoped é usado quando o objeto permanece ativo enquanto não houver um redirect (acesso a nova página)
//RequestScoped é usado quando o objeto só existe naquela requisição específica
//Quanto maior o escopo, maior o uso de memória no lado do servidor (objeto permanece ativo por mais tempo)
//Escopos maiores que Request exigem que classes sejam serializáveis assim como todos os seus atributos (recurso de segurança)
@ViewScoped
//Torna classe disponível na camada de visão (html) - são chamados de Beans ou ManagedBeans (gerenciados pelo JSF/EJB)
@Named
public class CadastroAreaController implements Serializable {

	//Anotação que marca atributo para ser gerenciado pelo CDI
	//O CDI criará uma instância do objeto automaticamente quando necessário
	@Inject
	private FacesContext facesContext;
	
	@Inject
    private AreaDAO areaDAO;
	
	private Area area;
	
	private List<Area> listaAreas;
	
	//Anotação que força execução do método após o construtor da classe ser executado
    @PostConstruct
    public void init() {
    	//Verifica se usuário está autenticado e possui a permissão adequada
    	//Verifica permissão
    	if (!this.facesContext.getExternalContext().isUserInRole("produtor"))
    	//!this.facesContext.getExternalContext().getRemoteUser() -> retorna o login do usuário
    		{
    		try {
				this.facesContext.getExternalContext().redirect("login-error.xhtml");
			} catch (IOException e) {e.printStackTrace();}
    	}
    	//Inicializa elementos importantes
    	this.listaAreas = areaDAO.listarTodos();
    }
	
    //Chamado pelo botão novo
	public void novoCadastro() {
        this.setArea(new Area());
    }
	
	//Chamado ao salvar cadastro de usuário (novo ou edição)
	public void salvar() {
		//Chama método de verificação se usuário é válido (regras negociais)
        if (areaValido()) {
        	try {
        		if (this.area.getEnder() == null) {
		        	this.areaDAO.salvar(this.area);
		        	this.facesContext.addMessage(null, new FacesMessage("Área Criada"));
		        } else {
		        	this.areaDAO.atualizar(this.area);
		        	this.facesContext.addMessage(null, new FacesMessage("Área Atualizada"));
		        }
        		//Após salvar usuário é necessário recarregar lista que popula tabela com os novos dados
		        this.listaAreas = areaDAO.listarTodos();
		        //Atualiza e executa elementos Javascript na tela assincronamente
			    PrimeFaces.current().executeScript("PF('usuarioDialog').hide()");
			    PrimeFaces.current().ajax().update("form:messages", "form:dt-usuarios");
	        } catch (Exception e) {
	            String errorMessage = getMensagemErro(e);
	            this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null));
	        }
        }
	}	
	
	//Realiza validações adicionais (não relizadas no modelo) e/ou complexas/interdependentes
	private boolean areaValido() {
		if (this.area.getEnder() == null && !this.areaDAO.ehAreaUnico(this.area.getEnder())) {
			this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Este endereço já está em uso.", null));
			return false;
		}
		return true;
	}
	
	//Chamado pelo botão remover da tabela
	public void remover() {
		try {
			this.areaDAO.excluir(this.area);
			//Após excluir usuário é necessário recarregar lista que popula tabela com os novos dados
			this.listaAreas = areaDAO.listarTodos();
	        //Limpa seleção de usuário
			this.area = null;
	        this.facesContext.addMessage(null, new FacesMessage("Área Removida"));
	        PrimeFaces.current().ajax().update("form:messages", "form:dt-usuarios");
        } catch (Exception e) {
            String errorMessage = getMensagemErro(e);
            this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null));
        }
	}
	
	/*//Chamado pelo botão alterar da tabela
	public void alterar() {
		this.permissoesSelecionadas.clear();
		for (TipoPermissao p: this.usuario.getPermissoes())
			this.permissoesSelecionadas.add(p.getId());
		this.usuario.setSenha("");
	}*/
	
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

	public AreaDAO getAreaDAO() {
		return areaDAO;
	}

	public void setAreaDAO(AreaDAO areaDAO) {
		this.areaDAO = areaDAO;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public List<Area> getListaAreas() {
		return listaAreas;
	}

	public void setListaAreas(List<Area> listaAreas) {
		this.listaAreas = listaAreas;
	}
}