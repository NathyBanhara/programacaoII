package br.edu.projeto.util;

import java.sql.Date;

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
        if (produtorDAO.ehProdutorUnico("86803717003")){ 	
	    	admin = new Produtor();
	    	admin.setCpf("86803717003");
	        admin.setEmail("admin@admin.com");
	        String senhaPadrao = "admin123";
	        admin.setSenha(passwordHash.generate(senhaPadrao.toCharArray()));
	        admin.setNome("admin");
	        String data_nascimento = "2001-10-02";
	        admin.setData_nascimento_string("2001-10-02");
	        produtorDAO.salvar(admin);
        }
    }
}
