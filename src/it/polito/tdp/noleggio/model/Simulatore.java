package it.polito.tdp.noleggio.model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import it.polito.tdp.noleggio.model.Evento.TipoEvento;

public class Simulatore {
	
	private PriorityQueue<Evento> queue = new PriorityQueue<>() ;
	
	// Stato del mondo
	private int autoTotali ;
	private int autoDisponibili ;
	
	// Parametri di simulazione
	private LocalTime oraInizio = LocalTime.of(8,0);
	private LocalTime oraFine = LocalTime.of(20, 0);
	private Duration intervalloArrivoCliente = Duration.ofMinutes(10);
	private List<Duration> durateNoleggio ;
	
	// Statistiche raccolte
	private int numeroClientiTotali ;
	private int numeroClientiInsoddisfatti ;
	
	// Variabili interne
	private Random rand = new Random();
	
	public Simulatore() {
		durateNoleggio = new ArrayList<Duration>() ;
		durateNoleggio.add(Duration.ofHours(1)) ;
		durateNoleggio.add(Duration.ofHours(2)) ;
		durateNoleggio.add(Duration.ofHours(3)) ;
	}
	
	public void init(int autoTotali) {
		this.autoTotali = autoTotali ;
		this.autoDisponibili = this.autoTotali ;
		this.numeroClientiTotali = 0 ;
		this.numeroClientiInsoddisfatti = 0 ;
		
		this.queue.clear();
		
		// carico gli eventi iniziali:
		// arriva un cliente ogni intervalloArrivoCliente
		// a partire dalle oraInizio
		
		for(LocalTime ora = oraInizio; ora.isBefore(oraFine); 
				ora = ora.plus(intervalloArrivoCliente)) {
			queue.add(new Evento(ora, TipoEvento.CLIENTE_ARRIVA)) ;
		}
	}
	
	public void run() {
		
		while(!queue.isEmpty()) {
			Evento ev = queue.poll() ;
			System.out.println(ev) ;
			
			switch(ev.getTipo()) {
			
			case CLIENTE_ARRIVA:
				this.numeroClientiTotali++ ;
				
				if(this.autoDisponibili==0) {
					this.numeroClientiInsoddisfatti++ ;
				} else {
					// noleggio l'auto al cliente
					this.autoDisponibili-- ;
					
					int i = rand.nextInt(durateNoleggio.size()) ;
					Duration noleggio = durateNoleggio.get(i) ;
					LocalTime rientro = ev.getTempo().plus(noleggio) ;
					
					queue.add(new Evento(rientro, TipoEvento.AUTO_RESTITUITA)) ;
					
				}
				break;
				
			case AUTO_RESTITUITA:
				this.autoDisponibili++ ;
				break;
			
			}
			
		}
		
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

	public Duration getIntervalloArrivoCliente() {
		return intervalloArrivoCliente;
	}

	public void setIntervalloArrivoCliente(Duration intervalloArrivoCliente) {
		this.intervalloArrivoCliente = intervalloArrivoCliente;
	}

	public List<Duration> getDurateNoleggio() {
		return durateNoleggio;
	}

	public void setDurateNoleggio(List<Duration> durateNoleggio) {
		this.durateNoleggio = durateNoleggio;
	}

	public int getAutoTotali() {
		return autoTotali;
	}

	public int getAutoDisponibili() {
		return autoDisponibili;
	}

	public int getNumeroClientiTotali() {
		return numeroClientiTotali;
	}

	public int getNumeroClientiInsoddisfatti() {
		return numeroClientiInsoddisfatti;
	}
	
	
	

}
