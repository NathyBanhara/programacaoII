package br.edu.projeto.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.br.CPF;

@Entity
public class Produtor
{
	//Chave primária da tabela
	@Id
	//Nome da coluna na tabela, necessário indicar quando atributo não tiver o mesmo nome
	@CPF
	@Column(name = "cpf")
	private String cpf;
	
	//Indica validações e mensagens de erro para atributo
	@NotNull
	@Size(min = 1, max = 50, message = "Mensagem customizada de erro! O nome do usuário deve ter no máximo 50 caracteres.")
	@Pattern(regexp = "[^0-9]*", message = "O nome de usuário não pode conter digitos.")
	private String nome;
	
	@NotNull
	@NotEmpty
	@Size(min = 6, max = 15, message = "Mensagem customizada de erro! A senha do usuário deve ter no minimo 6 caracteres e no máximo 15.")
	private String senha;
	
	@NotNull
	@NotEmpty
	@Email(message = "Não é um endereço de E-mail válido")
	private String email;
	
	@NotNull
	@NotEmpty
	private String data_nasc;

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getData_nasc() {
		return data_nasc;
	}

	public void setData_nasc(String data_nasc) {
		this.data_nasc = data_nasc;
	}
}

//safra
//anotacao da safra
//despesas
//receita