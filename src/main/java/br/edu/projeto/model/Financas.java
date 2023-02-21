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
public class Financas
{
	//Chave primária da tabela
    @Id
    //Gerada automaticamente pelo banco
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //Nome da coluna na tabela, necessário indicar quando atributo não tiver o mesmo nome
    @Column(name = "id_financas")
    private Integer idFinancas;

    //Indica validações e mensagens de erro para atributo
    @NotNull
    @Size(min = 1, max = 25, message = "Mensagem customizada de erro! O nome do fornecedor deve ter no máximo 25 caracteres.")
    @Pattern(regexp = "[^0-9]*", message = "O nome de fornecedor não pode conter digitos.")
    @Column(name = "nome_for")
    private String nomeFor;

    @NotNull
    @NotEmpty
    @Column(name = "data_real")
    private String dataReal;
    
    @Size(min = 1, max = 100, message = "Mensagem customizada de erro! A observação deve ter no máximo 100 caracteres.")
    private String obs;
    
	//Indica validações e mensagens de erro para atributo
	@NotNull
	@Size(min = 1, max = 50, message = "Mensagem customizada de erro! A descrição deve ter no máximo 50 caracteres.")
	@Pattern(regexp = "[^0-9]*", message = "A descrição não pode conter digitos.")
	private String descr;
    
	
	@NotNull
	private float valor;
	
	
    //Indica validações e mensagens de erro para atributo
    @NotEmpty
    @Column(name = "data_pag")
    private String dataPag;
    
    //Indica validações e mensagens de erro para atributo
    @NotEmpty
    @Column(name = "data_rec")
    private String dataRec;
    
    @ManyToOne
    private Safra safra;

	public Safra getSafra() {
		return safra;
	}

	public void setSafra(Safra safra) {
		this.safra = safra;
	}

	public Integer getIdFinancas() {
		return idFinancas;
	}

	public void setIdFinancas(Integer idFinancas) {
		this.idFinancas = idFinancas;
	}

	public String getNomeFor() {
		return nomeFor;
	}

	public void setNomeFor(String nomeFor) {
		this.nomeFor = nomeFor;
	}

	public String getDataReal() {
		return dataReal;
	}

	public void setDataReal(String dataReal) {
		this.dataReal = dataReal;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public float getValor() {
		return valor;
	}

	public void setValor(float valor) {
		this.valor = valor;
	}

	public String getDataPag() {
		return dataPag;
	}

	public void setDataPag(String dataPag) {
		this.dataPag = dataPag;
	}

	public String getDataRec() {
		return dataRec;
	}

	public void setDataRec(String dataRec) {
		this.dataRec = dataRec;
	}
}
