import java.util.concurrent.atomic.AtomicInteger;

import Utiles.TecladoIn;

public class Tiempo implements Runnable {
	private int capacidadTren, factorHL, factorHNL;
	private AtomicInteger hora;
	private Aeropuerto aeropuerto;
	private boolean esTest;
	
	public Tiempo (Aeropuerto aeropuerto, Aerolinea[] aerolineas, int capacidadTren, int factorHL, int factorHNL,boolean esTest) {
		this.hora=new AtomicInteger(0);
		this.capacidadTren=capacidadTren;
		this.factorHL=factorHL;
		this.factorHNL=factorHNL;
		this.aeropuerto=aeropuerto;
		this.aeropuerto.setTorre(this.hora, aerolineas);
		this.esTest=esTest;
		}
	
	public int getHora() {
		return this.hora.get();
		}
	
	private void iniciarDiaLaboral() {
		this.aeropuerto.comenzarDiaLaboral();
		}
	
	private void terminarDiaLaboral() {
		this.aeropuerto.terminarDiaLaboral();
		}
	
	private void hacerAndarTren() {
		(new Thread(this.aeropuerto.getTren(),"TREN")).start();
		}
	
	public void run() {
		
		this.hacerAndarTren();
		while(true) {
			this.hora.set(0);
			this.aeropuerto.programarVuelosDelDia();
			System.out.println();
			while(this.hora.get()<25) {
				this.mensajeCambioHora();
				this.aeropuerto.pasarTiempo();
				if(this.hora.get()<6 || this.hora.get()>22) {
					if(this.esTest) {
						this.mensajeTest(false);
						}
					if(this.getHora()==23) {
						this.terminarDiaLaboral();
						}
					this.pasarHoraNoLaboral();
					}else {
						if(this.esTest) {
							this.mensajeTest(true);
							}
						if(this.getHora()==6) {
							this.iniciarDiaLaboral();
							}
						this.pasarHoraLaboral();
						try {
							Thread.sleep(10000);
							} catch (InterruptedException e) {}
						
						}
				
				System.out.println("\n--------------------------\n");
				this.hora.addAndGet(1);
				}
			}
		}
	
	private void mensajeTest(boolean esHL) {
		char aux;
		if(esHL) {
			try {
				Thread.sleep(5000);
				} catch (InterruptedException e) {}
			}else {
				try {
					Thread.sleep(1000);
					} catch (InterruptedException e) {}
				}

		do {
			System.out.println("Introduzca Y para continuar: ");
			aux=TecladoIn.readNonwhiteChar();
			}while(aux!='Y' && aux!='y');
			
		}
	
	private void pasarHoraLaboral() {
		this.generarPasajeros(true);
		//verifico que todos los vuelos programados
		//para esta hora hayan despegado
		this.aeropuerto.verificarDespegues(this.hora.get());
		}
	
	private void pasarHoraNoLaboral() {
		this.generarPasajeros(false);
		}
	
	private void generarPasajeros(boolean horaLaboral) {
		Pasajero pasajero;
		int cantPasajeros;
		if(horaLaboral) {
			cantPasajeros=this.capacidadTren* ((int) (Math.random()*this.factorHL+1));
			}else {
				cantPasajeros=this.capacidadTren* ((int) (Math.random()*(1+this.factorHNL)));
				}
		System.out.println("Cantidad de pasajeros: "+cantPasajeros);
		if(cantPasajeros!=0) {
			for (int i=0; i<cantPasajeros;i++) {
				pasajero=new Pasajero (this.hora,this.aeropuerto);
				(new Thread (pasajero,pasajero.toString())).start();;
				}
			}
		}
	
	private void mensajeCambioHora() {
		System.out.println("HORA "+this.hora.get()+" (Cantidad de Vuelos: "+this.aeropuerto.cantVuelosEnHora()+")");
		}

}
