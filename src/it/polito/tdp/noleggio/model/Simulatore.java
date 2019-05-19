package it.polito.tdp.noleggio.model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import it.polito.tdp.noleggio.model.Evento.TipoEvento;

public class Simulatore {
	
	private PriorityQueue<Evento> queue = new PriorityQueue<>() ;
	

	// Stato del mondo
	private int totCars ;
	private int availableCars ;
	
	// Parametri di simulazione
	private Duration tempoArrivoCliente = Duration.ofMinutes(10) ;
	private List<Duration> tempiNoleggio = Arrays.asList(Duration.ofHours(1), Duration.ofHours(2), Duration.ofHours(3)) ;
	private LocalTime oraInizio = LocalTime.of(8, 00) ;
	private LocalTime oraFine = LocalTime.of(20, 00) ;
	
	
	// Statistiche
	private int clientiTotali ;
	private int clientiInsoddisfatti ;
	
	// Internals
	Random rand = new Random() ;
	
	public Simulatore(int totCars) {
		this.totCars = totCars ;
	}
	
	public void init() {
		queue.clear();
		
		for(LocalTime time = oraInizio ; time.isBefore(oraFine); time = time.plus(tempoArrivoCliente) ) {
			queue.add(new Evento(time, TipoEvento.CLIENTE_ARRIVA)) ;
		}
		
		// reset mondo
		availableCars = totCars ;
		
		// reset statistiche
		clientiTotali = 0 ;
		clientiInsoddisfatti = 0 ;
	}
	
	public void run() {
		while(!queue.isEmpty()) {
			Evento ev = queue.poll() ;
			
			switch (ev.getTipo()) {
			case CLIENTE_ARRIVA:
				clientiTotali++ ;
				
				if(availableCars==0) {
					clientiInsoddisfatti++ ;
				} else {
					availableCars-- ;
					Duration durataNoleggio = tempiNoleggio.get(rand.nextInt(tempiNoleggio.size())) ;
					LocalTime restituzione = ev.getTempo().plus(durataNoleggio) ;
					queue.add(new Evento(restituzione, TipoEvento.AUTO_RESTITUITA)) ;
					System.out.format("OUT %s, IN %s\n", ev.getTempo(), restituzione) ;
				}
				
				break;

			case AUTO_RESTITUITA:
				availableCars++ ;
				break ;
			}
		}
	}

	public int getTotCars() {
		return totCars;
	}

	public void setTotCars(int totCars) {
		this.totCars = totCars;
	}

	public Duration getTempoArrivoCliente() {
		return tempoArrivoCliente;
	}

	public void setTempoArrivoCliente(Duration tempoArrivoCliente) {
		this.tempoArrivoCliente = tempoArrivoCliente;
	}

	public List<Duration> getTempiNoleggio() {
		return tempiNoleggio;
	}

	public void setTempiNoleggio(List<Duration> tempiNoleggio) {
		this.tempiNoleggio = tempiNoleggio;
	}

	public LocalTime getOraInizio() {
		return oraInizio;
	}

	public void setOraInizio(LocalTime oraInizio) {
		this.oraInizio = oraInizio;
	}

	public LocalTime getOraFine() {
		return oraFine;
	}

	public void setOraFine(LocalTime oraFine) {
		this.oraFine = oraFine;
	}

	public int getAvailableCars() {
		return availableCars;
	}

	public int getClientiTotali() {
		return clientiTotali;
	}

	public int getClientiInsoddisfatti() {
		return clientiInsoddisfatti;
	}

	
}
