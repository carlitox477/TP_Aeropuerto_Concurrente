import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TorreDeControl {
	//La torre se encargar de los despegues y
	//aterrizajes de los aviones, además regulara
	//la programación de los vuelos.
	private static double probabilidadVuelo=0.1;
	private AtomicInteger hora;
	private HashMap<int[],Vuelo> vuelosDelDia;
	private Vuelo[] vuelosPosibles;
	private Aerolinea[] aerolineas;
	private int cantVuelos[];
	
	public TorreDeControl(AtomicInteger hora, Aerolinea[] aerolineas) {
		this.hora=hora;
		this.vuelosDelDia=new HashMap<int[],Vuelo>(15*25); //15 HL x 25 PE
		this.vuelosPosibles=null;
		this.aerolineas=aerolineas;
		this.cantVuelos=new int[16];
		
		for(int i=0;i<this.cantVuelos.length;i++) {
			this.cantVuelos[i]=0;
			}
		}
	
	public synchronized void programasVuelosDelDia() {
		//la clave sera embarque y hora
		Vuelo vuelo;
		char terminal;
		int[] e_h;
		for (int h=7; h<=22;h++) {
			for (int e=1; e<=25; e++) {
				if (Math.random()<=probabilidadVuelo) {
					terminal=this.obtenerTerminal(e);
					e_h=new int[2];
					e_h[0]=e;
					e_h[1]=h;
					vuelo=new Vuelo(this,terminal,e_h,this.aerolineas[(int)(Math.random()*this.aerolineas.length)]);
					this.vuelosDelDia.put(e_h, vuelo);
					this.cantVuelos[h-7]++;
					new Thread(vuelo,vuelo.toString()).start();
					}
				}
			}
		this.cartelera();
		}
	
	private char obtenerTerminal(int embarque) {
		char terminal=' ';
		if(embarque>=1 && embarque<=7) {
			terminal='A';
			}else if (embarque<=15){
				terminal='B';
				}else {
					terminal='C';
					}
		return terminal;
		}
	
	private void cambioDeHora() {
		this.notifyAll();
		}
	
	public synchronized void finalizarVentas(Vuelo vuelo) {
		//método para no permitir más ventas del
		//vuelo enviado por parametro
		while(this.hora.get()!=(vuelo.getEmbarque_hora()[1]-1)) {
			//Espero a la hora anterior para dejar de vender pasajes
			//Ejemplo: Si el vuelo sale a las 9, los pasajes se dejan
			//de vender a las 8
			try {
				wait();} catch (InterruptedException e) {}
			}
		this.vuelosDelDia.remove(vuelo.getEmbarque_hora());
		System.out.println("[\u2708 \u274c "+vuelo.toString()+"]: Ha finalizado sus ventas");
		}
	
	public synchronized void ordenarDespegue(Vuelo vuelo) {
		System.out.println("[\uD83D\uDEE7 \uD83D\uDEEB DESPEGUE "+vuelo.toString()+"]: ¡No vuelven más! (¡Bon Voyage!)");
		this.cantVuelos[vuelo.getHora()-7]--;
		if(this.cantVuelos[vuelo.getHora()-7]==0) {
			notifyAll();
			}
		}
	
	public synchronized void verficarCantidadDespegues(int hora) {
		//espero hasta que despeguen todos los vuelos
		while(this.cantVuelos[hora-7]!=0) {
			try {
				wait();
				} catch (InterruptedException e) {}
			}
		//Una vez que todos despegaron, podria pasar de hora
		}
	
	public synchronized boolean permitirAterrizaje(Vuelo vuelo) {
		//no permito aterrizar hasta que no sea la hora adecuada
		boolean exito;
		while(this.hora.get()!=vuelo.getHora()) {
			try {
				wait();} catch (InterruptedException e) {}
			}
		if(vuelo.getCantidadPasajeros()!=0) {
			System.out.println("[\uD83D\uDEEC ATERRIZAJE "+vuelo.toString()+"]: Ha aterrizado");
			exito=true;
			}else {
				this.ignorarVuelo(vuelo);
				exito=false;
				}
		return exito;
		}
	
	public synchronized void actualizarVuelosPosibles() {
		this.vuelosPosibles=new Vuelo[this.vuelosDelDia.size()];
		this.vuelosDelDia.values().toArray(this.vuelosPosibles);
		this.cambioDeHora();
		}
	
	public synchronized void obtenerPasaje (Pasajero pasajero) {
		if(this.vuelosPosibles.length!=0) {
			pasajero.setPasaje(this.vuelosPosibles[(int)(Math.random()*this.vuelosPosibles.length) ]);
			pasajero.getPasaje().sacarPasaje();
			System.out.println("\uD83D\uDEC9 \uD83D\uDEC8 \u2714 "+pasajero.toString()+": Ha sido derivado al puesto de atención correspondiente por tener el "+pasajero.getPasaje().toString());
			}else {
				System.out.println("\uD83D\uDEC9 \ud83d\ude02 "+pasajero.toString()+": se ha equivocado de aeropuerto y se retira muy apurado");
				this.notifyAll();
				}
		}
	
	public void cartelera() {
		Vuelo[] cartelera=new Vuelo[this.vuelosDelDia.size()];
		this.vuelosDelDia.values().toArray(cartelera);
		for(int i=0;i<cartelera.length;i++) {
			System.out.println("\u2708 \u2708 \u2708 " +cartelera[i].toString());
			}
		}
	
	public synchronized int cantVuelosEnHora() {
		int cant=0;
		if(this.hora.get()>6 && this.hora.get()<23) {
			cant=this.cantVuelos[this.hora.get()-7];
			}
		return cant;
		}
	
	private void ignorarVuelo(Vuelo vuelo) {
		System.out.println("[\uD83D\uDEE7 \u274c CONTINUA "+vuelo.toString()+"]: Continua su camino porque nadie ha sacado pasajes para este vuelo ("+vuelo.getCantidadPasajeros()+")");
		this.cantVuelos[vuelo.getHora()-7]--;
		if(this.cantVuelos[vuelo.getHora()-7]==0) {
			notifyAll();
			}
		}
	

}
