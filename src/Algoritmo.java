import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
/**
 * Representa el thread encargado de "ejecutar" el algoritmo de multiplicacion de matrices
 * (leer el archivo de referencias) 
 * Este thread se encargará de ir actualizando el estado de la tabla de páginas y los marcos
 * de página en memoria real, de acuerdo con las referencias generadas por el proceso y el 
 * número de marcos de página asignados. Se ejecuta cada 2 milisegundos.
 */
public class Algoritmo extends Thread{

    /**
     * Nombre del archivo con las referencias.
     */
    private String nomArchivo;
    
    /**
     * Monitor encargado de administrar la manipulacion de la tabla de paginas y a la ram,
     * ademas de la manipulacion de las paginas.
     */
    private Monitor monitor;
    
    /**
     * Constructor del administrador de tablas.
     * @param tp estructura de tipo {@code Pagina[]} que representa la tabla de paginas.
     * @param ram estructura de tipo {@code Pagina[]} que representa la memoria real.
     * @param nomArchivo nombre del archivo con las referencias.
     */
    public Algoritmo(String nomArchivo, Monitor monitorThreads){
        this.nomArchivo = nomArchivo;
        this.monitor = monitorThreads;
    }

    /**
     * Ejecucion del thread
     */
    public void run(){

        try (BufferedReader br = new BufferedReader(new FileReader(nomArchivo))){
            String linea;
            int aux = 1;
            int numeroMarcosPaginas = monitor.getNumMarcosPagina();
            // lee las referencias del archivo:
            while((linea = br.readLine())!=null){
            	if(aux>6){
            		//Tomar el id de la pagina que se referencia.
            		int numPag =Integer.parseInt(linea.split(",")[1]);
            		
            		if(monitor.estaEnRam(numPag)){
            			monitor.syncR(numPag);
            		}else{
                        //inc en numero de fallas
            			monitor.incFallas();
            			if(monitor.getRam().size()<numeroMarcosPaginas) {
            				monitor.addPaginaRam(numPag);
            			}else {// Si no hay espacio en la ram (se reemplaza por la pagina mas vieja en ram):
            				monitor.intercambio(numPag);
            			}
                    }
                    try {
                        sleep(2);
                    } catch (InterruptedException e) {
                        System.out.println("Error en la espera del Administrador de tablas: ");
                        e.printStackTrace();
                    }
                }
                aux++;
            }
            monitor.setFinished();
            br.close();
            System.out.println("El numero de fallos de pagina es: "+ monitor.getFallas());    
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}