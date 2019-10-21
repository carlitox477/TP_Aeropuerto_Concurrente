import java.util.concurrent.atomic.AtomicInteger;

public class Pasajero implements Runnable{
	private static AtomicInteger cant=new AtomicInteger(0);
	private int idPasajero;
	private Vuelo pasaje;
	private Aeropuerto aeropuerto;
	private AtomicInteger hora;
	
	public Pasajero(AtomicInteger hora, Aeropuerto aeropuerto) {
		this.idPasajero=cant.incrementAndGet();
		this.hora=hora;
		this.aeropuerto=aeropuerto;
		}
	
	public void setPasaje(Vuelo vuelo) {
		this.pasaje=vuelo;
		}
	
	public int getHoraActual() {
		return this.hora.get();
		}
	
	public Aeropuerto getAeropuerto() {
		return this.aeropuerto;
		}
	
	public int getIdPasajero() {
		return this.idPasajero;
		}

	public Vuelo getPasaje() {
		return this.pasaje;
		}

	public String toString() {
		return "PASAJERO "+this.idPasajero;
		}
	
	public void run() {
		//Llega al aeropuerto y va al puesto de informes (Allí se le asignara el vuelo)
		System.out.println("\uD83D\uDEC9 "+this.toString()+": Ha llegado al aeropuerto");
		this.aeropuerto.pedirPuestoDeInforme(this);
		//Va a su puesto de atención
		if(this.pasaje!=null) {
			//seria null si se equivoco de aeropuerto
			this.pasaje.getAerolinea().usarPuestoDeAtencion(this);
			//Va a espera el tren y va su terminal
			this.aeropuerto.usarTren(this);
			
			this.comprarEnFreeShop();
			
			this.pasaje.subirPasajero(this);
			//Espera el avión y se va
			}
		}
	
	private void comprarEnFreeShop() {
		//Trato de ingresar al Freeshop si me da el horario
		//En esta caso es si el pasajero tiene más de una hora de espera
		if(this.hora.get()<this.pasaje.getHora()) {
			this.aeropuerto.comprarFreeShop(this);
			}
		}

}
