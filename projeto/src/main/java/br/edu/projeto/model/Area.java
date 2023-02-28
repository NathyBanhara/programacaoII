package br.edu.projeto.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
public class Area
{
	@Id
	@Size(min = 6, max = 60, message = "Mensagem customizada de erro! O endereço da área deve ter no máximo 60 caracteres e no mínimo 6.")
	@Column(name = "ender")
	private String ender;
	
	//Indica validações e mensagens de erro para atributo
	@NotNull
	@Size(min = 1, max = 50, message = "Mensagem customizada de erro! O nome da área deve ter no máximo 50 caracteres.")
	@Pattern(regexp = "[^0-9]*", message = "O nome da área não pode conter digitos.")
	private String nome;
	
	@NotNull
	private float quant_hec;
	
	@Transient
	private Integer novo;
	
	public Integer getNovo() {
		return novo;
	}

	public void setNovo(Integer novo) {
		this.novo = novo;
	}
	
	@ManyToOne(cascade  = CascadeType.MERGE)
	@JoinColumn(name = "safra",
	foreignKey = @ForeignKey(name = "fk_area_safra")
	)
	private Safra safra;

	public Safra getSafra() {
		return safra;
	}

	public void setSafra(Safra safra) {
		this.safra = safra;
	}

	public String getEnder() {
		return ender;
	}

	public void setEnder(String ender) {
		this.ender = ender;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public float getQuant_hec() {
		return quant_hec;
	}

	public void setQuant_hec(float quant_hec) {
		this.quant_hec = quant_hec;
	}
	
}
