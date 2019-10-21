import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Aerolinea {
	private String nombre;
	private Semaphore puestosDeAtencion;
	private Lock guardia;
	private static String[]  emojis= {"\uD83D\uDC82","\uD83D\uDC69","\uD83D\uDC69 \u2714"};
	// fila="\uD83D\uDC82";
	// atencion="\uD83D\uDC69";
	// atencionCheck="\uD83D\uDC69 \u2714";
	
	public Aerolinea (String nombre, int cantidadDePuestosDeAtencion) {
		this.nombre=nombre;
		this.guardia=new ReentrantLock();
		this.puestosDeAtencion=new Semaphore(cantidadDePuestosDeAtencion);
		}

	public String getNombre() {
		return this.nombre;
		}

	public void setNombre(String nombre) {
		this.nombre = nombre;
		}
	
	public String toString() {
		return "AEROLINEA "+this.nombre;
		}
	
	public void usarPuestoDeAtencion (Pasajero pasajero) {
		//el pasajero se forma en la fila
		this.guardia.lock();
		System.out.println("["+emojis[0]+" FILA ATENCIÓN "+this.nombre+"]: El "+pasajero.toString()+" se ha formado ");
		this.guardia.unlock();;
		
		try {
			//Aqui voy a ver si hay un puesto de atención disponible
			this.puestosDeAtencion.acquire();} catch (InterruptedException e) {}
		System.out.println("["+emojis[1]+" ATENCIÓN "+this.nombre+"]: El "+pasajero.toString()+" está siendo atendido");
		try {
			//Simulo la atencion
			Thread.sleep(2000);
			} catch (InterruptedException e) {}
		System.out.println("["+emojis[2]+" ATENCIÓN "+this.nombre+"]: El "+pasajero.toString()+" es mandado al EMBARQUE "+pasajero.getPasaje().getEmbarque()+" (TERMINAL "+pasajero.getPasaje().getTerminal()+")");
		//libero el puesto de atención
		this.puestosDeAtencion.release();
		}
	
}
