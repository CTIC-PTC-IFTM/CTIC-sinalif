package sinalif.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sinalif.models.Alarme;
import sinalif.repositories.AlarmeRepository;
import sinalif.repositories.EtiquetaRepository;
import sinalif.services.AlarmeService;

@Service
@RequiredArgsConstructor
public class AlarmeServiceImpl implements AlarmeService {
	@Autowired
	private AlarmeRepository alarmeRepository;

	@Autowired
	private EtiquetaRepository etiquetaRepository;

	@Override
    public List<Alarme> listarAlarmes(){
		return alarmeRepository.findAll();
	}

	@Override
	public List<Alarme> listarAlarmesPendentesParaHoje(){
		return alarmeRepository.findAlarmesPendentesParaHoje();
	}

	@Override
	public List<Alarme> listarAlarmesPendentesParaAgora(){
		return alarmeRepository.findAlarmesPendentesParaAgora();
	}

	@Override
	public Alarme detalharAlarme(Long id) {
		return alarmeRepository.findById(id).get();
	}

	@Override
	public Alarme salvarAlarme(Alarme alarme){
		alarme.setEtiqueta(etiquetaRepository.getReferenceById(alarme.getEtiqueta().getIdEtiqueta()));
		return alarmeRepository.save(alarme);
	}

	@Override
	public Alarme atualizarAlarme(Long id, Alarme alarmeAtualizado) {
	    Alarme alarmeExistente = alarmeRepository.findById(id)
	        .orElseThrow(() -> new RuntimeException("Alarme não encontrado com ID: " + id));
	    alarmeExistente.setHorarioProgramado(alarmeAtualizado.getHorarioProgramado());
	    alarmeExistente.setDiasSemana(alarmeAtualizado.getDiasSemana());
	    alarmeExistente.setAtivo(alarmeAtualizado.isAtivo());
	    alarmeExistente.setPausado(alarmeAtualizado.isPausado());
	    alarmeExistente.setDataModificacao(LocalDateTime.now());
	    alarmeExistente.setEtiqueta(alarmeAtualizado.getEtiqueta());
	    return alarmeRepository.save(alarmeExistente);
	}

	@Override
	public void excluirAlarme(Long id) {
		if (alarmeRepository.existsById(id)) {
			alarmeRepository.deleteById(id);
		} else {
			throw new RuntimeException("Alarme não encontrada com ID: " + id);
		}
	}

	@Override
	public void excluirAlarme(Alarme alarme) {
		alarmeRepository.deleteById(alarme.getIdAlarme());
	}
}
