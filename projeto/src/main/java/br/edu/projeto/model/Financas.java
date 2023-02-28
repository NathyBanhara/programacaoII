package br.edu.projeto.model;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
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
    @Column(name = "id")
    private Integer idFinancas;

    //Indica validações e mensagens de erro para atributo
    @NotNull
    @Size(min = 1, max = 25, message = "Mensagem customizada de erro! O nome do fornecedor deve ter no máximo 25 caracteres.")
    @Pattern(regexp = "[^0-9]*", message = "O nome de fornecedor não pode conter digitos.")
    @Column(name = "nome_for")
    private String nomeFor;

    @NotNull
    @Column(name = "data_real")
    private LocalDate dataReal;
    
    @Size(max = 100, message = "Mensagem customizada de erro! A observação deve ter no máximo 100 caracteres.")
    private String obs;
    
	//Indica validações e mensagens de erro para atributo
	@NotNull
	@Size(min = 1, max = 50, message = "Mensagem customizada de erro! A descrição deve ter no máximo 50 caracteres.")
	@Pattern(regexp = "[^0-9]*", message = "A descrição não pode conter digitos.")
	private String descr;
    
	
	@NotNull
	private float valor;
	
	
    //Indica validações e mensagens de erro para atributo
    @Column(name = "data_pag")
    private LocalDate dataPag;
    
    //Indica validações e mensagens de erro para atributo
    @Column(name = "data_rec")
    private LocalDate dataRec;
    
    @Transient
    private String opcao;
    
	public String getOpcao() {
		return opcao;
	}

	public void setOpcao(String opcao) {
		this.opcao = opcao;
	}

	@ManyToOne(cascade  = CascadeType.MERGE)
	@JoinColumn(name = "safra",
	foreignKey = @ForeignKey(name = "fk_financas_safra")
	)
	private Safra safra;
	
	public void setSafra(Safra safra) {
		this.safra = safra;
	}

	public Safra getSafra() {
		return safra;
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

	public LocalDate getDataReal() {
		return dataReal;
	}

	public void setDataReal(LocalDate dataReal) {
		this.dataReal = dataReal;
	}
	
	public void setData_real_string(String dataReal) {
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy"); 
		LocalDate data = LocalDate.parse(dataReal, formato);
		this.dataReal = data;
	}
	
	public String getData_real_string()
	{
		if (this.dataReal == null)
		{
			return "";
		}
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return formato.format(this.dataReal);
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

	public LocalDate getDataPag() {
		return dataPag;
	}

	public void setDataPag(LocalDate dataPag) {
		if (dataPag != null && dataPag.compareTo(dataReal) < 0)
		{
			System.out.printf("\n\n\nA data de transação precisa ser maior ou igual a data de realização\n\n\n");
			return;
		}
		this.dataPag = dataPag;
	}
	
	public void setData_pag_string(String dataPag) {
		if (dataPag == null)
		{
			this.dataPag = null;
		}
		else
		{
			DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy"); 
			LocalDate data = LocalDate.parse(dataPag, formato);
			this.dataPag = data;
		}
	}
	
	public String getData_pag_string()
	{
		if (this.dataPag == null)
		{
			return "";
		}
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return formato.format(this.dataPag);
	}

	public LocalDate getDataRec() {
		return dataRec;
	}

	public void setDataRec(LocalDate dataRec) {
		if (dataRec != null && dataRec.compareTo(dataReal) < 0)
		{
			System.out.printf("\n\n\nA data de transação precisa ser maior ou igual a data de realização\n\n\n");
			return;
		}
		this.dataRec = dataRec;
	}
	
	public void setData_rec_string(String dataRec) {
		if (dataRec == null)
		{
			this.dataRec = null;
		}
		else
		{
			DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy"); 
			LocalDate data = LocalDate.parse(dataRec, formato);
			this.dataRec = data;
		}
	}
	
	public String getData_rec_string()
	{
		if (this.dataRec == null)
		{
			return "";
		}
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return formato.format(this.dataRec);
	}
}
