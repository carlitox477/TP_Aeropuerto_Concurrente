import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Tren implements Runnable{
	private static String emoji="\uD83D\uDE86";
	private char letraTerminal;//' ' simbolizara el hall central
	private int[] pasajerosPorTerminal;
	private int asientosOcupados, capacidad;
	private Lock guardiaTren;
	private Condition esperarHall, esperarEnElTren,avanzarTren;
	
	
	public Tren (int capacidad) {
		this.letraTerminal=' ';
		
		this.pasajerosPorTerminal=new int[3];
		
		this.asientosOcupados=0;
		this.capacidad=capacidad;
		
		this.guardiaTren=new ReentrantLock();
		this.esperarHall=this.guardiaTren.newCondition();
		this.esperarEnElTren=this.guardiaTren.newCondition();
		this.avanzarTren=this.guardiaTren.newCondition();
		
		for (int i=0;i<this.pasajerosPorTerminal.length;i++) {
			this.pasajerosPorTerminal[i]=0;
			}
		}

	public int getCapacidad() {
		return this.capacidad;
		}
	
	public String toString() {
		return "TREN";
		}
	
	public void run() {
		while (true) {
			this.guardiaTren.lock();
			System.out.println("["+emoji+" \uD83C\uDD37 "+this.toString()+"]: Aguarda pasajeros");
			if (this.letraTerminal==' ' && this.asientosOcupados!=10) {
				this.esperarHall.signalAll();
				}
			if(this.asientosOcupados!=10) {
				try {
					this.avanzarTren.await();
					} catch (InterruptedException e) {}
				}
			this.dejarPasajeros();
			this.volverAlHall();
			}
		}
	
	private void volverAlHall() {
		System.out.println("["+emoji+" \u2B31 TREN]: vuelve al Hall central");
		this.letraTerminal=' ';
		}
	
	private void dejarPasajeros() {
		this.visitarTerminal('A',"\uD83C\uDD70");
		this.visitarTerminal('B',"\uD83C\uDD71");
		this.visitarTerminal('C',"\uD83C\uDD72");
		}
	
	private void visitarTerminal(char letra, String emojiT) {
		System.out.println("["+emoji+" "+emojiT+" TREN]: Ha llegado a la terminal "+letra+" (Se bajan "+this.pasajerosPorTerminal[(int) letra-65]+")");
		this.letraTerminal=letra;
		this.esperarEnElTren.signalAll();
		while(this.pasajerosPorTerminal[(int) letra-65]!=0) {
			try {
				this.avanzarTren.await();
				} catch (InterruptedException e) {}
			}
		System.out.println("["+emoji+" \u2B8A TREN]: continua su recorrido");
		}
	
	public void usarTren(Pasajero pasajero, char terminal) {
		this.subirAlTren(pasajero, terminal);
		
		if(this.asientosOcupados==this.capacidad) {
			this.avanzarTren.signalAll();
			}
		
		while(this.letraTerminal!=terminal) {
			try {
				this.esperarEnElTren.await();
				} catch (InterruptedException e) {}
			}
		this.bajarDelTren(pasajero, terminal);
		}
	
	private void subirAlTren(Pasajero pasajero, char terminal) {
		this.guardiaTren.lock();
		while(this.asientosOcupados==10 && this.letraTerminal!=' ') {
			//si el tren esta ocupados me quedo esperandolo
			try {
				this.esperarHall.await();
				} catch (InterruptedException e) {}
			}
		this.asientosOcupados++;
		System.out.println("["+emoji+" \uD83E\uDC45 "+this.toString()+" RECOGE]: Subió el "+pasajero.toString()+" (Asientos ocupados: "+this.asientosOcupados+")");
		//anotamos en que terminal hay que bajarnos
		this.pasajerosPorTerminal[(int) terminal - 65]++;
		}
	
	private void bajarDelTren(Pasajero pasajero, char terminal) {
		//anoto que se bajo alguien en esta terminal
		this.pasajerosPorTerminal[(int) terminal - 65]--;
		this.asientosOcupados--;
		System.out.println("["+emoji+" \uD83E\uDC47"+this.toString()+" BAJA]: El "+pasajero.toString()+": se ha bajado del TREN (Asientos ocupados: "+this.asientosOcupados+")");
		//le aviso al tren que ya nos bajamos
		this.avanzarTren.signalAll();
		this.guardiaTren.unlock();
		}
	
}
