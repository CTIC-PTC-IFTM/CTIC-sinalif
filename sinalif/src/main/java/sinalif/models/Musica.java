package sinalif.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "musica")
@EntityListeners(AuditingEntityListener.class)
public class Musica {

    //private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_musica")
    private long idMusica;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @NotBlank(message= "URL é um campo obrigatório")
    @Column(name = "url", nullable = false)
    private String url;

    @CreatedDate
    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @OneToMany(mappedBy = "musica", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<LogReproducao> logs;
}
