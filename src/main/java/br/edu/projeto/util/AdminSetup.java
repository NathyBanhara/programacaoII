package br.edu.projeto.util;

import javax.inject.Inject;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import br.edu.projeto.dao.ProdutorDAO;
import br.edu.projeto.model.Produtor;

//Executa classe uma única vez ao iniciar a aplicação no servidor
//Recurso usado para criar o primeiro usuário ADMINISTRADOR no sistema, se o mesmo não existir ainda
@WebListener
public class AdminSetup implements ServletContextListener {

	@Inject
    private Pbkdf2PasswordHash passwordHash;

    @Inject
    private ProdutorDAO produtorDAO;

    private Produtor admin;
    
    public void contextInitialized(ServletContextEvent event) {
        if (produtorDAO.ehProdutorUnico("11111111111")){ 	
	    	admin = new Produtor();
	        admin.setEmail("admin@admin.com");
	        String senhaPadrao = "admin";
	        admin.setSenha(passwordHash.generate(senhaPadrao.toCharArray()));
	        admin.setNome("admin");
	        admin.setData_nasc("02/10/2001");
	        produtorDAO.salvar(admin);
        }
    }
}
