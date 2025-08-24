package sinalif.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sinalif.models.Alarme;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AlarmeRepository extends JpaRepository<Alarme, Long>{

    @Query(
            value = "SELECT * FROM alarme " +
                    "WHERE " +
                    "  ativo = true AND (pausado = false OR pausado IS NULL) " +
                    "  AND dias_semana LIKE '%' || (EXTRACT(DOW FROM NOW()) + 1)::text || '%' " +
                    "  AND horario_programado <= CAST(NOW() AS time) " +
                    "  AND (data_ultima_execucao IS NULL OR DATE(data_ultima_execucao) < CURRENT_DATE)",
            nativeQuery = true
    )
    List<Alarme> findAlarmesPendentesParaAgora();

    @Query(
            value = "SELECT * FROM alarme " +
                    "WHERE " +
                    "  ativo = true AND (pausado = false OR pausado IS NULL) " +
                    "  AND dias_semana LIKE '%' || (EXTRACT(DOW FROM NOW()) + 1)::text || '%' " +
                    "  AND (data_ultima_execucao IS NULL OR DATE(data_ultima_execucao) < CURRENT_DATE)",
            nativeQuery = true
    )
    List<Alarme> findAlarmesPendentesParaHoje();

    @Transactional
    @Query("""
        UPDATE Alarme a SET
            a.horarioProgramado = :#{#alarme.horarioProgramado},
            a.diasSemana = :#{#alarme.diasSemana},
            a.ativo = :#{#alarme.ativo},
            a.pausado = :#{#alarme.pausado},
            a.dataUltimaExecucao = :#{#alarme.dataUltimaExecucao},
            a.etiqueta = :#{#alarme.etiqueta}
        WHERE
            a.idAlarme = :#{#alarme.idAlarme}
    """)
    void updateSemAlterarDataModificacao(@Param("alarme") Alarme alarme);

    //NÃOA FUNCIONOU, TENHO 2 OPÇÕES:
    // 1. FAZER A TABELA DE LOGS E UZA-LA NO LUGAR DO "dataUltimaExecucao"
    // 2. REMOVER O "DATA DE MODIFICAÇÃO" DO MODEL (MUITO MAIS PRÁTICO)
}
