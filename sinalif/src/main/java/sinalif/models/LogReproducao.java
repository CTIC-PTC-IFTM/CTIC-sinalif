package sinalif.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "LogReproducao")
@EntityListeners(AuditingEntityListener.class)
public class LogReproducao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_log_reproducao")
    private long idLogReproducao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_musica")
    private Musica musica;

    @CreatedDate
    @Column(name = "data_reproducao")
    private LocalDateTime dataReproducao;
}
