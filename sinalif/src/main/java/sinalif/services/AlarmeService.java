package sinalif.services;

import sinalif.models.Alarme;

import java.util.List;

public interface AlarmeService {
    public List<Alarme> listarAlarmes();
    List<Alarme> listarAlarmesPendentesParaHoje();
    List<Alarme> listarAlarmesPendentesParaAgora();
    public Alarme detalharAlarme(Long id);
    public Alarme salvarAlarme(Alarme alarme);
    public Alarme atualizarAlarme(Long id, Alarme alarmeAtualizado);
    public void excluirAlarme(Long id);
    public void excluirAlarme(Alarme alarme);
}
