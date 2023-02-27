package br.edu.projeto.model;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
	@Size(min = 6, message = "Mensagem customizada de erro! A senha do usuário deve ter no minimo 6 caracteres.")
	private String senha;
	
	@NotNull
	@NotEmpty
	@Email(message = "Não é um endereço de E-mail válido")
	private String email;
	
	@NotNull
	private LocalDate data_nasc;
	
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

	public LocalDate getData_nasc() {
		return data_nasc;
	}

	public void setData_nasc(LocalDate data_nasc) {
		this.data_nasc = data_nasc;
	}
	/*
	public void setData_nascimento_string(String data_nasc) {
		this.data_nasc = Date.valueOf(data_nasc);
	}
	*/
	public void setData_nascimento_string(String data_nasc)
	{
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy"); 
		LocalDate data = LocalDate.parse(data_nasc, formato);
		this.data_nasc = data;
	}
	
	public String getData_nascimento_string()
	{
		if (this.data_nasc == null)
		{
			return "";
		}
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return formato.format(this.data_nasc);
		//return this.data_nasc.toString();
	}
}

//safra
//anotacao da safra
//despesas
//receita