import java.util.concurrent.atomic.AtomicInteger;

public class CajaFreeShop {
	private static AtomicInteger cant=new AtomicInteger(0);
	private int id;
	private char terminal;
	private String emoji;
	
	public CajaFreeShop(char terminal, String emoji2) {
		this.id=cant.addAndGet(1);
		this.terminal=terminal;
		this.emoji="\ud83d\udecd\ufe0f "+emoji2;
		}
	
	public String toString() {
		return "CAJA "+this.id+ "(TERMINAL "+this.terminal+")";
		}
	
	public void atenderPasajero(Pasajero pasajero) {
		System.out.println("["+this.emoji+" "+this.toString()+"]: esta atendiendo al "+pasajero.toString());
		try {
			//simulo atención
			Thread.sleep(1000);
			} catch (InterruptedException e) {}
		System.out.println("["+this.emoji+" \ud83d\ude01 "+this.toString()+"]: termino de atender al "+pasajero.toString());
		}

}
