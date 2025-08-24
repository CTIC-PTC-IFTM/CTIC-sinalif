package sinalif.services.impl;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sinalif.models.Alarme;
import sinalif.models.Musica;
import sinalif.repositories.AlarmeRepository;
import sinalif.repositories.MusicaRepository;
import sinalif.services.AgendamentoService;
import sinalif.services.MusicaService;
import sinalif.services.ReprodutorService;

import java.io.File;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AgendamentoServiceImpl implements AgendamentoService {

    private final AlarmeRepository alarmeRepository;
    private final ReprodutorService reprodutorService;

    // Estado da sessão de alarme atual
    private Timer tempoSessao;
    private List<String> musicasPlaylist;
    private int musicaIndex;
    private volatile boolean ativoSessao = false;
    private volatile boolean ativoSessaoTeste = false;
    private String tocandoAgora = "";

    public AgendamentoServiceImpl(AlarmeRepository alarmeRepository, ReprodutorService reprodutorService) {
        this.alarmeRepository = alarmeRepository;
        this.reprodutorService = reprodutorService;
    }

    //A cada 10 segundos: verificar
    @Scheduled(fixedRate = 10000)
    @Transactional
    public String verificarEExecutarAlarmes() {
        if (ativoSessao || ativoSessaoTeste) {
            System.out.println("Sessão de alarme já está ativa. Pulando verificação de novos alarmes.");
            return tocandoAgora;
        }

        System.out.println("["+Instant.now()+"]: Verificando alarmes recorrentes para disparar...");
        List<Alarme> alarmesParaTocar = alarmeRepository.findAlarmesPendentesParaAgora();

        if (!alarmesParaTocar.isEmpty()) {
            Alarme alarmeParaDisparar = alarmesParaTocar.get(0);
            iniciarSessao(alarmeParaDisparar);
        } else {
            System.out.println("Nenhum alarme pendente encontrado.");
            System.out.println();
        }
        return "Não há nenhuma música tocando neste momento ";
    }

    //Inciiar uma sessão (tempo em que as músicas ficam a tocar)
    public void iniciarSessao(Alarme alarme) {
        System.out.println("["+Instant.now()+"]: Iniciando nova sessão de alarme para o ID: " + alarme.getIdAlarme());
        ativoSessao = true;

        try {
            // Atualiza o alarme no banco para não ser pego novamente hoje
            alarme.setDataUltimaExecucao(Instant.now());
            alarmeRepository.updateSemAlterarDataModificacao(alarme);

            String diretorioPath = "../recursos/musicas";
            System.out.println("diretorioPath"+diretorioPath);
            File diretorio = new File(diretorioPath);

            File[] files = diretorio.listFiles((dir, name) -> name.toLowerCase().endsWith(".mp3"));
            if (files == null || files.length == 0) {
                System.err.println("Nenhum arquivo .mp3 encontrado no diretório: " + diretorioPath);
                encerrarSessao();
            }

            //A playlist é uma coleção de caminhos do repositório
            musicasPlaylist = Arrays.stream(files).map(File::getAbsolutePath).collect(Collectors.toList());
            Collections.shuffle(musicasPlaylist);
            musicaIndex = -1;

            // Agenda o fim da sessão de alarme
            tempoSessao = new Timer();
            tempoSessao.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("Tempo total da sessão de alarme atingido.");
                    encerrarSessao();
                }
            }, Long.parseLong(alarme.getEtiqueta().getDuracao()) * 60000);

            // Inicia a playlist
            proximaMusica();

        } catch (Exception e) {
            System.err.println("Falha ao iniciar a sessão de alarme: " + e.getMessage());
            encerrarSessao(); // Garante que o estado seja limpo em caso de erro
        }
    }

    public void testarSessao() {
        ativoSessaoTeste = true;

        try {
            String diretorioPath = "../recursos/musicas";
            System.out.println("diretorioPath"+diretorioPath);
            File diretorio = new File(diretorioPath);

            File[] files = diretorio.listFiles((dir, name) -> name.toLowerCase().endsWith(".mp3"));
            if (files == null || files.length == 0) {
                System.err.println("Nenhum arquivo .mp3 encontrado no diretório: " + diretorioPath);
                encerrarSessao();
            }

            //A playlist é uma coleção de caminhos do repositório
            musicasPlaylist = Arrays.stream(files).map(File::getAbsolutePath).collect(Collectors.toList());
            Collections.shuffle(musicasPlaylist);
            musicaIndex = -1;

            // Inicia a playlist
            proximaMusica();

        } catch (Exception e) {
            System.err.println("Falha ao iniciar a sessão de alarme: " + e.getMessage());
            encerrarSessao(); // Garante que o estado seja limpo em caso de erro
        }
    }

    //Próxima música
    public void proximaMusica() {
        if (!ativoSessao && !ativoSessaoTeste) return;

        if (musicasPlaylist == null || musicasPlaylist.isEmpty()) {
            encerrarSessao();
            return;
        }

        musicaIndex++;
        if (musicaIndex >= musicasPlaylist.size()) {
            musicaIndex = 0; // Reinicia a playlist
        }

        String proximaMusicaPath = musicasPlaylist.get(musicaIndex);
        System.out.println("["+Instant.now()+"]: Tocando música (" + (musicaIndex + 1) + "/" + musicasPlaylist.size() + "): " + proximaMusicaPath);

        // Toca a música e, quando ela terminar, chama este mesmo metodo novamente.
        reprodutorService.play(proximaMusicaPath, this::proximaMusica);

        String nomeDaMusica = proximaMusicaPath.substring(proximaMusicaPath.lastIndexOf("\\") + 1);
        tocandoAgora = "(" + (musicaIndex + 1) + "/" + musicasPlaylist.size() + ") Tocando música: " + nomeDaMusica;
    }

    //Volta música
    public void voltarMusica() {
        if (!ativoSessao && !ativoSessaoTeste) return;

        if (musicasPlaylist == null || musicasPlaylist.isEmpty()) {
            encerrarSessao();
            return;
        }

        musicaIndex--;
        if (musicaIndex < 0) {
            musicaIndex = musicasPlaylist.size()-1; // Reinicia a playlist inversamente
        }

        String proximaMusicaPath = musicasPlaylist.get(musicaIndex);
        System.out.println("["+Instant.now()+"]: Tocando música (" + (musicaIndex + 1) + "/" + musicasPlaylist.size() + "): " + proximaMusicaPath);

        // Toca a música e, quando ela terminar, chama proximaMusica normalmente.
        reprodutorService.play(proximaMusicaPath, this::proximaMusica);

        String nomeDaMusica = proximaMusicaPath.substring(proximaMusicaPath.lastIndexOf("\\") + 1);
        tocandoAgora = "(" + (musicaIndex + 1) + "/" + musicasPlaylist.size() + ") Tocando música: " + nomeDaMusica;
    }

    //Encerra a sessão (tempo em que as músicas ficam a tocar)
    public void encerrarSessao() {
        if (!ativoSessao && !ativoSessaoTeste) return;

        System.out.println("["+Instant.now()+"]: Encerrando sessão de alarme.");
        ativoSessao = false;
        ativoSessaoTeste = false;

        if (tempoSessao != null) {
            tempoSessao.cancel();
        }

        reprodutorService.stop();

        musicasPlaylist = null;
        tempoSessao = null;
    }
}