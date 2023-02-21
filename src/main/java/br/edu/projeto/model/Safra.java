package br.edu.projeto.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
public class Safra
{
	//Chave primária da tabela
    @Id
    //Gerada automaticamente pelo banco
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //Nome da coluna na tabela, necessário indicar quando atributo não tiver o mesmo nome
    @Column(name = "id")
    private Integer idSafra;
    
    @NotNull
    private Integer ano;
    
   //Indica validações e mensagens de erro para atributo
  	@NotNull
  	@Size(min = 1, max = 50, message = "Mensagem customizada de erro! O tipo de cultivo deve ter no máximo 50 caracteres.")
  	@Pattern(regexp = "[^0-9]*", message = "O tipo de cultivo não pode conter digitos.")
  	private String nome;
    
		
	//Indica validações e mensagens de erro para atributo
	@NotNull
	@Size(min = 1, max = 50, message = "Mensagem customizada de erro! O nome da área deve ter no máximo 50 caracteres.")
	@Pattern(regexp = "[^0-9]*", message = "O nome da área não pode conter digitos.")
	@Column(name = "tipo_cul")
	private String tipoCul;
	
	@NotNull
	@Column(name = "mes_inicio")
	private Integer mesInicio;
	
	
	@Column(name = "mes_termino")
	private Integer mes_termino;
	
	//Indica mapeamento/relacionamento entre tabela
    //fetchType EAGER indica que atributo será carregado automaticamente, enquanto LAZY (padrão) indicaria carregamento sob demanda 
    //LAZY é mais eficiente pois economiza recursos computacionais mas é mais complexo de se trabalhar
    //cascadeType, MERGE indica que alterações serão transmitidas para elementos dependentes (tabelas relacionadas) automaticamente
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    //Mapeia tabela intermediária (criada em relacionamentos Muitos para muitos), não é necessário uma classe modelo para a tabela intermediária
    @JoinTable(
      name = "area_safra",
      joinColumns = @JoinColumn(name = "safra"),
      inverseJoinColumns = @JoinColumn(name = "area")
    )
    
    private List<Area> areaSafra = new ArrayList<Area>();

	public List<Area> getAreaSafra() {
		return areaSafra;
	}


	public void setAreaSafra(List<Area> areaSafra) {
		this.areaSafra = areaSafra;
	}


	public Integer getIdSafra() {
		return idSafra;
	}


	public void setIdSafra(Integer idSafra) {
		this.idSafra = idSafra;
	}


	public Integer getAno() {
		return ano;
	}


	public void setAno(Integer ano) {
		this.ano = ano;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getTipoCul() {
		return tipoCul;
	}


	public void setTipoCul(String tipoCul) {
		this.tipoCul = tipoCul;
	}


	public Integer getMesInicio() {
		return mesInicio;
	}


	public void setMesInicio(Integer mesInicio) {
		this.mesInicio = mesInicio;
	}


	public Integer getMes_termino() {
		return mes_termino;
	}


	public void setMes_termino(Integer mes_termino) {
		this.mes_termino = mes_termino;
	}
	
	
}
