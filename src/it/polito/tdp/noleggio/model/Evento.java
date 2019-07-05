package it.polito.tdp.noleggio.model;

import java.time.LocalTime;

public class Evento implements Comparable<Evento> {
	
	public enum TipoEvento { // insieme di costanti che rappresentano
							 // i vari tipi di eventi che possono verificarsi
		CLIENTE_ARRIVA,
		AUTO_RESTITUITA
	}
	
	private LocalTime tempo ;
	private TipoEvento tipo ;
	
	public Evento(LocalTime tempo, TipoEvento tipo) {
		super();
		this.tempo = tempo;
		this.tipo = tipo;
	}

	@Override
	public int compareTo(Evento other) { // Perche' devono essere ordinati in base al tempo
		// Priorita' della coda
		return this.tempo.compareTo(other.tempo);
	}

	public LocalTime getTempo() {
		return tempo;
	}

	public TipoEvento getTipo() {
		return tipo;
	}

	@Override
	public String toString() {
		return String.format("tempo=%s, tipo=%s", tempo, tipo);
	}

	
}
