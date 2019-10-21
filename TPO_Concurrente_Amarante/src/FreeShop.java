import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;

public class FreeShop {
	private static double probabilidadCompra=0.5;
	private char terminal;
	private ArrayBlockingQueue<CajaFreeShop> cajas;
	private Semaphore permisosDeEntrada;
	private String emoji;
	
	public FreeShop(int capacidad, char terminal, int cantCajas, String emoji) {
		this.terminal=terminal;
		this.cajas=new ArrayBlockingQueue<CajaFreeShop>(cantCajas);
		this.permisosDeEntrada=new Semaphore(capacidad);
		this.emoji=	"\uD83D\uDED2 "+emoji;
		
		for(int i=0;i<cantCajas;i++) {
			try {
				this.cajas.put(new CajaFreeShop(this.terminal, emoji));
				} catch (InterruptedException e) {}
			}
		}
	
	public String toString() {
		return "FREE SHOP "+this.terminal;
		}
	
	public void comprar(Pasajero pasajero) {
		CajaFreeShop caja=null;
		if(Math.random()<=probabilidadCompra) {
			System.out.println("["+this.emoji+" "+this.toString()+"]: El "+pasajero.toString()+" trata de ingresar");
			if(this.permisosDeEntrada.tryAcquire()) {
				System.out.println("["+this.emoji+" \ud83d\ude01 "+this.toString()+"]: El "+pasajero.toString()+" ingreso tranquilamente");
				try {
					//Simulo la busqueda de productos
					Thread.sleep(1000);
					System.out.println("["+this.emoji+" \ud83d\ude12 "+"FILA CAJAS "+this.toString()+"]: El "+pasajero.toString()+" eligió sus productos y fue a formarse en la fila");
					caja=this.cajas.take();
					caja.atenderPasajero(pasajero);
					System.out.println("["+this.emoji+" \ud83d\ude0b "+this.toString()+"]: El "+pasajero.toString()+" se retira del lugar para esperar su vuelo");
					} catch (InterruptedException e) {}
				this.permisosDeEntrada.release();
				try {
					this.cajas.put(caja);
					} catch (InterruptedException e) {}
				}else {
					System.out.println("["+this.emoji+" \ud83d\ude34 "+this.toString()+"]: Capacidad agotada, "+pasajero.toString()+" decide irse a dormir una siesta hasta que llegue su vuelo");
					}
			}
		}	

}
