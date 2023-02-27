package br.edu.projeto.model;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Anotacao {
	//Chave primária da tabela
    @Id
    //Gerada automaticamente pelo banco
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //Nome da coluna na tabela, necessário indicar quando atributo não tiver o mesmo nome
    @Column(name = "id")
    private Integer idAnotacao;

    //Indica validações e mensagens de erro para atributo
    @NotNull
    @Column(name = "data_inicio")
    private LocalDate dataInicio;
    
	//Indica validações e mensagens de erro para atributo
	@NotNull
	@Size(min = 1, max = 50, message = "Mensagem customizada de erro! A descrição deve ter no máximo 50 caracteres.")
	@Pattern(regexp = "[^0-9]*", message = "A descrição não pode conter digitos.")
	private String descr;

	//Indica validações e mensagens de erro para atributo
	@NotNull
	@Size(min = 1, max = 500, message = "Mensagem customizada de erro! O texto deve ter no máximo 500 caracteres.")
	private String texto;
	
	@Column(name = "data_term")
	private LocalDate dataTerm;
	
	@ManyToOne(cascade  = CascadeType.MERGE)
	@JoinColumn(name = "safra",
	foreignKey = @ForeignKey(name = "fk_anotacao_safra")
	)
	private Safra safra;

	public Safra getSafra() {
		return safra;
	}

	public void setSafra(Safra safra) {
		this.safra = safra;
	}

	public Integer getIdAnotacao() {
		return idAnotacao;
	}

	public void setIdAnotacao(Integer idAnotacao) {
		this.idAnotacao = idAnotacao;
	}

	public LocalDate getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(LocalDate dataInicio) {
		this.dataInicio = dataInicio;
	}
	
	public void setData_inicio_string(String dataInicio)
	{
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy"); 
		LocalDate data = LocalDate.parse(dataInicio, formato);
		this.dataInicio = data;
	}
	
	public String getData_inicio_string()
	{
		if (this.dataInicio == null)
		{
			return "";
		}
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return formato.format(this.dataInicio);
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public LocalDate getDataTerm() {
		return dataTerm;
	}

	public void setDataTerm(LocalDate dataTerm) {
		this.dataTerm = dataTerm;
	}
	
	public void setData_term_string(String dataTerm)
	{
		if (dataTerm.equals(""))
		{
			this.dataTerm = null;
		}
		else
		{
			DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy"); 
			LocalDate data = LocalDate.parse(dataTerm, formato);
			this.dataTerm = data;
		}
	}
	
	public String getData_term_string()
	{
		if (this.dataTerm == null)
		{
			return "";
		}
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return formato.format(this.dataTerm);
	}
}
