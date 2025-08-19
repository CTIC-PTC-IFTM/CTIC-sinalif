package sinalif.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "etiqueta")
public class Etiqueta {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id_etiqueta")
	private long idEtiqueta;

	@NotBlank(message= "Nome é um campo obrigatório")
	@Column(nullable = false)
	private String nome;

	@NotBlank(message= "Duração é um campo obrigatório")
	@Column(nullable = false)
	private String duracao;

	@OneToMany(mappedBy = "etiqueta", cascade = CascadeType.ALL)
	private List<Alarme> alarmes;
}
