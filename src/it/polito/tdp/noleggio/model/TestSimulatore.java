package it.polito.tdp.noleggio.model;

public class TestSimulatore {
	public static void main(String[] args) {
		Simulatore sim = new Simulatore( 14 ) ;
		sim.init();
		sim.run();
		System.out.format("%d clienti, %d insoddisfatti\n", sim.getClientiTotali(), sim.getClientiInsoddisfatti()) ;
		
	}
}
