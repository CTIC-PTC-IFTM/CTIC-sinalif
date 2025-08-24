package sinalif.services;

import sinalif.models.Alarme;

public interface AgendamentoService {
    public String verificarEExecutarAlarmes();
    public void voltarMusica();
    public void testarSessao();
    public void iniciarSessao(Alarme alarme);
    public void proximaMusica();
    public void encerrarSessao();
}
