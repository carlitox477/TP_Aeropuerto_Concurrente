
public class Main {
	public static void main(String[] args) {
		int capacidadTren, capacidadFreeShops, cantidadPuestosDeInformes;
		int factorHL, factorHNL, cantCajasFS;
		Aerolinea[]aerolineas;
		Aeropuerto aeropuerto;
		Tren tren;
		Tiempo tiempo;
		boolean esTest;
		
		esTest=false;
		cantCajasFS=2;
		capacidadTren=10;
		factorHL=3;
		factorHNL=1;
		capacidadFreeShops=5;
		cantidadPuestosDeInformes=5;
		
		aerolineas=inicializarAerolineas(5);
		tren=new Tren(capacidadTren);
		aeropuerto=new Aeropuerto(tren,cantidadPuestosDeInformes,capacidadFreeShops,cantCajasFS);
		
		tiempo=new Tiempo(aeropuerto,aerolineas,capacidadTren, factorHL, factorHNL,esTest);
		new Thread(tiempo, "TIEMPO").start();
		}
	
	public static Aerolinea[] inicializarAerolineas(int puestosDeAtencion) {
		//metodo para inicializar las aerolineas
		//Suponemos que cada aerolinea tiene la misma cantidad
		// de puestos de atención
		String[] nombresAerolineas={"LAN", "AEROLINEAS ARGENTINAS", "QATAR AIRWAY", "FLYBONDI", "KAMIKAZE", "ISIS FLIGHTS", "AL QAEDA AIRWAY", "LOS PEPES", "LINEA CALI", "SINALOA FLOWERS FLIGHTS"};		
		Aerolinea[]aerolineas=new Aerolinea[nombresAerolineas.length];
		
		for (int i=0;i<aerolineas.length;i++) {
			aerolineas[i]=new Aerolinea(nombresAerolineas[i], puestosDeAtencion);
			}
		return aerolineas;
		}

}
