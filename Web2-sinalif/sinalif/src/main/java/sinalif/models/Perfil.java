package sinalif.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
@Entity
@Table(name = "perfil")
public class Perfil implements GrantedAuthority {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id_perfil")
	private long idPerfil;

	@NotBlank(message= "Nome é um campo obrigatório")
	@Column(nullable = false)
	private String nome;

	@Override
	public String getAuthority() {
		return this.nome;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
