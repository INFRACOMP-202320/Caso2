/**
 * Thread encargado de ejecutar el algoritmo de envejecimiento cada 1 milisegundo.
 */
public class Tanenbaum extends Thread{

    /**
     * Monitor encargado de administrar la manipulacion de la tabla de paginas y a la ram,
     * ademas de la manipulacion de las paginas.
     */
    private Monitor monitor;

    /**
     * Constructor del ejecutor del algoritmo de Tanenbaum
     * @param ram estructura de tipo {@code Pagina[]} que representa la
     * memoria real.
     */
    public Tanenbaum(Monitor monitorThreads){

        this.monitor = monitorThreads;
    }

    /**
     * Ejecucion del thread.
     */
    public void run(){
        //envejece y rejuvenece las paginas, cada milisegundo
    	while(monitor.getFinished() == false) {
    		monitor.sincronizacion();
        	try {
    			sleep(1);
    		} catch (InterruptedException e) {
				System.out.println("Ocurrio un error en la espera de Tanenbaum:");
    			e.printStackTrace();
    		}		
    	}
    }
}