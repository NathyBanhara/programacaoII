package br.edu.projeto.security;

import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import javax.security.enterprise.authentication.mechanism.http.LoginToContinue;
import javax.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;

//Configurações de autenticação/autorização para a API de segurança
@CustomFormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(
                loginPage = "login.xhtml",
                errorPage = "login-error.xhtml"
        )
)

//Configura local e forma onde usuários e papéis/permissões/roles serão consultados/autenticados
@DatabaseIdentityStoreDefinition(
        dataSourceLookup = "java:/PostgresDS100",
        callerQuery = "SELECT senha FROM produtor WHERE cpf = ?",
        //groupsQuery = "SELECT permissao FROM tipo_permissao JOIN permissao USING (id_tipo_permissao) JOIN usuario USING (id_usuario) WHERE usuario = ?"
        groupsQuery = "SELECT CASE WHEN cpf = '11111111111' THEN 'admin' ELSE 'produtor' END permissao FROM produtor where cpf = ?"
)

@ApplicationScoped
public class AppConfig {
	
}
