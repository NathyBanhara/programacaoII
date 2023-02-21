package br.edu.projeto.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
	
	@ManyToOne
	private Produtor produtor;
    
    //No relacionamento ManytoMany uma das classes deve ser "dominada" pela outra
    //A classe dominada possui o parâmetro mappedBy, essa classe nunca persistirá alterações na classe/tabela dominante
    @ManyToMany(mappedBy = "areaSafra", fetch = FetchType.EAGER)
    private List<Safra> safras = new ArrayList<Safra>();

	public Produtor getProdutor() {
		return produtor;
	}

	public List<Safra> getSafras() {
		return safras;
	}

	public void setSafras(List<Safra> safras) {
		this.safras = safras;
	}

	public void setProdutor(Produtor produtor) {
		this.produtor = produtor;
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
	
	//produtor varchar(11) not null,
	
	
}
