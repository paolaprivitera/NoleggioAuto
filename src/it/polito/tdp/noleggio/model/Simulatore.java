package it.polito.tdp.noleggio.model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import it.polito.tdp.noleggio.model.Evento.TipoEvento;

public class Simulatore {
	
	// Coda prioritaria
	private PriorityQueue<Evento> queue = new PriorityQueue<>() ;
	
	// 1. STATO DEL MONDO: se facessi una fotografia cosa mi interesserebbe vedere?
	// (nel momento in cui non accade nulla, cioe' tra un evento e il successivo)
	private int autoTotali ; // parametro fisso
	private int autoDisponibili ; // parametro variabile
	
	// 2. PARAMETRI DI SIMULAZIONE: dati sulla base dei quali il simulatore decide
	// cosa fare quando succede un evento
	private LocalTime oraInizio = LocalTime.of(8,0); // of costruisce un nuovo oggetto -> ore 8 e minuti 0
	private LocalTime oraFine = LocalTime.of(20, 0);
	private Duration intervalloArrivoCliente = Duration.ofMinutes(10);
	private List<Duration> durateNoleggio ; // insieme di valori possibili che il parametro puo' assumere
											// -> lista che popolo nel costruttore
	
	//3.  STATISTICHE RACCOLTE: esito simulazione
	private int numeroClientiTotali ;
	private int numeroClientiInsoddisfatti ;
	
	// *Variabili interne
	private Random rand = new Random();
	
	public Simulatore() { // Costruttore
		durateNoleggio = new ArrayList<Duration>() ;
		durateNoleggio.add(Duration.ofHours(1)) ;
		durateNoleggio.add(Duration.ofHours(2)) ;
		durateNoleggio.add(Duration.ofHours(3)) ;
	}
	
	public void init(int autoTotali) { // Inizializzazione avendo le autoTotali passate 
		// preparare i valori indispensabili perche' l'esecuzione
		// della simulazione possa avvenire senza problemi
		
		this.autoTotali = autoTotali ;
		this.autoDisponibili = this.autoTotali ; // all'inizio sono tutte disponibili
		
		// Azzeramento statistiche
		this.numeroClientiTotali = 0 ;
		this.numeroClientiInsoddisfatti = 0 ;
		
		// Resetto la coda degli eventi
		this.queue.clear();
		
		// carico gli eventi iniziali:
		// arriva un cliente ogni intervalloArrivoCliente
		// a partire dalle oraInizio
		
		for(LocalTime ora = oraInizio; ora.isBefore(oraFine); 
				ora = ora.plus(intervalloArrivoCliente)) {
			// la variabile ora corrisponde all'istante di tempo in cui arrivano i clienti
			
			// in ciascun istante di tempo inserisco un evento
			queue.add(new Evento(ora, TipoEvento.CLIENTE_ARRIVA)) ;
			
			// Non posso creare qui l'evento di restituzione perche' non ho ancora affittato l'auto
		}
	}
	
	public void run() { // Iniziare la simulazione vera e propria
						// dopo aver fatto la init
		while(!queue.isEmpty()) { // condizione di terminazione
			Evento ev = queue.poll() ; // Estraggo un evento
			
			// Metodi principali della coda:
			// - pick: restituisce il primo evento della coda ->
			// 		   lo guarda ma lo lascia al suo posto
			// - poll: restituisce il primo evento della coda ->
			//		   lo legge e lo estrae dalla lista
			
			System.out.println(ev) ;
			
			// Devo fare cose diverse a seconda del tipo di eventi
			// Faccio uno switch
			
			switch(ev.getTipo()) {
			
			case CLIENTE_ARRIVA:
				
				// Aggiornare lo stato del mondo
				// Aggiornare le statistiche
				// Eventualmente schedulare nuovi eventi
				
				this.numeroClientiTotali++ ;
				
				if(this.autoDisponibili==0) { // Ho un'auto da dargli?
					this.numeroClientiInsoddisfatti++ ;
					// non ho eventi nuovi da generare
				} else {
					// noleggio l'auto al cliente
					this.autoDisponibili-- ;
					
					// poiche' gli ho affittato un'auto, il cliente la deve restituire
					// quindi devo creare un evento di restituzione
					
					// Devo estrarre una durata casuale tra le durate presenti nella mia lista
					// Utilizzo un oggetto random che devo inizializzare (vedi *Variabili interne)
					
					int i = rand.nextInt(durateNoleggio.size()) ; // java.util.random
					
					// mi restituisce un numero tra 0 e 2 
					
					Duration noleggio = durateNoleggio.get(i) ;
					
					// questa durata la uso per calcolare l'ora di rientro dell'auto
					// sommandola al tempo corrente (cioe' l'ora simulata -> ev.getTempo())
					
					LocalTime rientro = ev.getTempo().plus(noleggio) ;
					
					// Creo il nuovo evento
					queue.add(new Evento(rientro, TipoEvento.AUTO_RESTITUITA)) ;
					
				}
				break;
				
			case AUTO_RESTITUITA:
				this.autoDisponibili++ ;
				break;
			
			}
			
		}
		
	}

	// Bisogna stare attenti a non fare il set di variabili la cui modifica dall'esterno
	// non influisca sull'andamento della simulazione
	// es. non devo fare il set delle autoTotali (perche' le imposto con il metodo init())
	
	/* In generale:
	 * - i valori statistici sono disponibili solo in lettura (quindi solo getter)
	 * - i parametri di simulazione normalmente sono disponibili anche in modifica dall'esterno (quindi anche setter)
	 */
	
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
	
	// Un esterno puo' cambiare il valore della durata
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
