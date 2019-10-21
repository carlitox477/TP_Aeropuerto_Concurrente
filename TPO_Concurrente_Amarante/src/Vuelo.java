import java.util.concurrent.Semaphore;
public class Vuelo implements Runnable {
	private static int cont=1;
	private int id, cantidadPasajeros;
	private Aerolinea aerolinea;
	private int [] embarque_hora;
	private char terminal;
	private TorreDeControl torre;
	private Semaphore abordajes, oficialDeAbordo;
	
	public Vuelo(TorreDeControl torre, char terminal,int [] embarque_hora, Aerolinea aerolinea) {
		this.id=cont++;
		this.terminal=terminal;
		this.embarque_hora=embarque_hora;
		this.aerolinea=aerolinea;
		this.cantidadPasajeros=0;
		this.torre=torre;
		this.oficialDeAbordo=new Semaphore(0);
		this.abordajes=new Semaphore(0);
		}
	
	public int getCantidadPasajeros() {
		return this.cantidadPasajeros;
		}
	
	public int getId() {
		return this.id;
		}
	
	public int[] getEmbarque_hora() {
		return this.embarque_hora;
		}
	
	public int getEmbarque() {
		return this.embarque_hora[0];
		}

	public int getHora() {
		return this.embarque_hora[1];
		}

	public char getTerminal() {
		return this.terminal;
	}

	public Aerolinea getAerolinea() {
		return this.aerolinea;
		}

	public String toString() {
		return ("VUELO "+this.id+ "(Aerolinea: "+this.aerolinea.getNombre()+"; Terminal: "+this.terminal+"; Embarque: "+this.embarque_hora[0]+"; Hora: "+this.embarque_hora[1]+")");
		}
	
	public synchronized void sacarPasaje() {
		this.cantidadPasajeros++;
		System.out.println("\u2708 \ud83d\udcb5 VUELO "+this.id+": pasajes reservados: "+this.cantidadPasajeros);
		}
	
	public void subirPasajero(Pasajero pasajero) {
		System.out.println("\uD83D\uDEC9 \ud83d\ude0e "+pasajero.toString()+": Espera a que llegue el "+this.toString());
		//el pasajero trata de subir al avion
		try {
			this.oficialDeAbordo.acquire();
			} catch (InterruptedException e) {}
		System.out.println("[\ud83e\udd42 EMBARQUE "+this.toString()+"] Ha subido el "+pasajero.toString());
		//Informo que me subi
		this.abordajes.release();
		//se deja el paso al siguiente pasajero
		this.oficialDeAbordo.release();
		}

	public void run() {
		//primero debemos cerrar las ventas de pasajes
		this.torre.finalizarVentas(this);
		//luego pedimos aterrizar
		if(this.torre.permitirAterrizaje(this)) {
			//embarcamos
			this.embarcar();
			try {Thread.sleep(2000);} catch (InterruptedException e) {}
			//pedimos despegar
			this.torre.ordenarDespegue(this);
			}
		}
	
	private void embarcar() {
		System.out.println("[\u2708 \ud83d\udd11 EMBARQUE]: "+this.toString()+" empiezan a subir pasajeros");
		// permito el abordaje
		this.oficialDeAbordo.release();
		try {
			//controlo que suban todos
			this.abordajes.acquire(this.cantidadPasajeros);
			} catch (InterruptedException e) {}
		System.out.println("[\u2708 \ud83d\udd12 EMBARQUE]: "+this.toString()+" finalizó su embarque");
		}
	
}
