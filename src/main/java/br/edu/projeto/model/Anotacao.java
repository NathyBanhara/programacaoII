package br.edu.projeto.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    @NotEmpty
    @Column(name = "data_inicio")
    private String dataInicio;
    
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
	private String dataTerm;
	
	@ManyToOne
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

	public String getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(String dataInicio) {
		this.dataInicio = dataInicio;
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

	public String getDataTerm() {
		return dataTerm;
	}

	public void setDataTerm(String dataTerm) {
		this.dataTerm = dataTerm;
	}
}
