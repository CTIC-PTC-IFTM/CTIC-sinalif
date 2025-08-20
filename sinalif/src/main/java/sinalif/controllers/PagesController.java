package sinalif.controllers;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sinalif.services.AgendamentoService;
import sinalif.services.AlarmeService;

@Controller
@RequestMapping()
public class PagesController {
    @Autowired
    private AgendamentoService IAgendamentoService;
    @Autowired
    private AlarmeService IAlarmeService;

    @GetMapping(value = {"/adm", "/adm/"})
    public String pageAdmin() {
        return "pages/adm/indexAdmin";
    }

    @GetMapping("/reprodutor")
    public String exibirReprodutor(Model model) {
        model.addAttribute("alarmeList", IAlarmeService.listarAlarmesPendentesParaHoje());
        return "pages/reprodutor";
    }

    @GetMapping("/reprodutor/anterior")
    public String reprodutorAnterior() {
        IAgendamentoService.voltarMusica();
        return "redirect:/reprodutor";
    }

    @GetMapping("/reprodutor/parar")
    public String reprodutorParar() {
        IAgendamentoService.encerrarSessao();
        return "redirect:/reprodutor";
    }

    @GetMapping("/reprodutor/tocar")
    public String reprodutorTocar() {
        IAgendamentoService.testarSessao();
        return "redirect:/reprodutor";
    }

    @GetMapping("/reprodutor/proxima")
    public String reprodutorProxima() {
        IAgendamentoService.proximaMusica();
        return "redirect:/reprodutor";
    }
}
