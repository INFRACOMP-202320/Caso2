import java.util.ArrayList;

/**
 * Thread encargado de ejecutar el algoritmo de envejecimiento cada 1 milisegundo.
 */
public class Tanenbaum extends Thread{


    /**
     * Representa la memoria real.
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



    public void run(){
        //envejece y rejuvenece las paginas, cada milisegundo
    	while(monitor.getFinished() == false) {
    		
    		monitor.sincronizacion();
        		try {
    				sleep(1);
    			} catch (InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    			
    		}
    		
    	}
    	
    
}