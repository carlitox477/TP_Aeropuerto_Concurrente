import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Aeropuerto {
	private FreeShop[] freeShops;
	private TorreDeControl torre;
	private Semaphore puestosDeInformesDisponibles, turnoPuestoDeInforme;
	private Tren tren;
	
	public Aeropuerto (Tren tren, int cantidadDePuestosDeInformes,int capacidadFS, int cantCajasFS) {
		this.tren=tren;
		this.puestosDeInformesDisponibles=new Semaphore(cantidadDePuestosDeInformes);
		this.turnoPuestoDeInforme=new Semaphore(0);
		this.freeShops=new FreeShop[3];
		
		this.freeShops[0]=new FreeShop(capacidadFS,'A',cantCajasFS,"\uD83C\uDD70");
		this.freeShops[1]=new FreeShop(capacidadFS,'B',cantCajasFS,"\uD83C\uDD71");
		this.freeShops[2]=new FreeShop(capacidadFS,'C',cantCajasFS,"\uD83C\uDD72");
		}
	
	public void setTorre(AtomicInteger hora, Aerolinea[]aerolineas) {
		this.torre=new TorreDeControl(hora,aerolineas);
		}
	
	public Tren getTren() {
		return this.tren;
		}

	public void pedirPuestoDeInforme(Pasajero pasajero) {
		//El pasajero se forma en la fila
		System.out.println("[\uD83D\uDC6E FILA PUESTO INFORME]: "+pasajero.toString()+" se ha formado");
		try {
			this.turnoPuestoDeInforme.acquire();} catch (InterruptedException e) {}
		//El pasajero es atendido por un puesto de informe
		try {
			this.puestosDeInformesDisponibles.acquire();} catch (InterruptedException e) {}
		
		//Le digo al anterior pasajero que avance un lugar
		this.turnoPuestoDeInforme.release();	
		
		System.out.println("\uD83D\uDEC9 \uD83D\uDEC8 "+pasajero.toString()+": Esta siendo atendido por un puesto de informes");
		//Se me da un vuelo (pues no interesa modelar el tema de reserva)
		try {
			//simulo atención
			Thread.sleep(1000);
			} catch (InterruptedException e) {}
		this.torre.obtenerPasaje(pasajero);
		this.puestosDeInformesDisponibles.release();
		}
	
	public void pasarTiempo() {
		this.torre.actualizarVuelosPosibles();
		}
	
	public void programarVuelosDelDia() {
		this.torre.programasVuelosDelDia();
		}
	
	public void comenzarDiaLaboral() {
		//abrimos los puestos de informe, y habilitamos todo el proceso
		System.out.println("AEROPUERTO: Inicio del dia laboral, se abren los puestos de informe, atención y freeshops al público, el conductor del tren llega a tiempo como siempre");
		this.turnoPuestoDeInforme.release();
		}
	
	public void terminarDiaLaboral() {
		//cerramos el puesto de informes, de esta manera evitamos
		//el flujo de procesos y hacemos que todo cierre
		try {this.turnoPuestoDeInforme.acquire();} catch (InterruptedException e) {}
		System.out.println("AEROPUERTO: Todo el personal va a dormir luego de una larga jornada laboral");
		}
	
	public void usarTren(Pasajero pasajero) {
		this.tren.usarTren(pasajero, pasajero.getPasaje().getTerminal());
		}
	
	public void comprarFreeShop(Pasajero pasajero) {
		char terminal=pasajero.getPasaje().getTerminal();
		this.freeShops[(int) terminal-65].comprar(pasajero);
		}
	
	public void verificarDespegues(int hora) {
		if(hora>6&& hora<23) {
			this.torre.verficarCantidadDespegues(hora);
			}
		}
	
	public int cantVuelosEnHora() {
		return this.torre.cantVuelosEnHora();
		}

}
