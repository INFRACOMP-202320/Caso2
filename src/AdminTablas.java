import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Representa el thread encargado de ir actualizando el estado de la tabla de páginas y
 * los marcos de página en memoria real cada 2 milisegundos.
 */
public class AdminTablas extends Thread{


    /**
     * Representa la tabla de paginas
     */
    //private ArrayList<Pagina> paginas;
    
    /**
     * Nombre del archivo con las referencias.
     */
    private String nomArchivo;
    
    /**
     * Monitor que maneja las operaciones
     */
    private Monitor monitor;
    
    /**
     * Constructor del administrador de tablas.
     * @param tp estructura de tipo {@code Pagina[]} que representa la tabla de paginas.
     * @param ram estructura de tipo {@code Pagina[]} que representa la memoria real.
     * @param nomArchivo nombre del archivo con las referencias.
     */
    public AdminTablas(String nomArchivo, Monitor monitorThreads){
        this.nomArchivo = nomArchivo;
        this.monitor = monitorThreads;
    }



    /**
     * Busca la pagina mas vieja para reemplazarla por la pagina entrante
     * Se ejecuta solo cuando ocurre un fallo de pagina y la ram esta llena.
     * @return {@code int[]} rta en donde rta[0]= id de pagina a reemplazar y
     * rta[1] = posicion del marco de pagina en ram donde se encuentra la pagina
     * que debe ser reemplazada.
     */



    public void run(){

        try (BufferedReader br = new BufferedReader(new FileReader(nomArchivo))){
            int aux = 1;
            ArrayList<Pagina> paginas = monitor.getPaginas();
            HashMap <Pagina, Integer> tablaPg = monitor.getTablaPg();
            int numeroMarcosPaginas = monitor.getNumMarcosPagina();
            // lee el archivo cargando las referencias una por una.
            while((br.readLine())!=null){
            	if(aux>6) {
            		//tomar numero pagina de referencia
            		int numPag =Integer.parseInt(br.readLine().split(",")[1]);
            		
            		//tomar pagina
            		int buscar = tablaPg.get(paginas.get(numPag));
            		//si no esta en la tabla de paginas::
            		
            		if(buscar==-1){
            			//inc en numero de fallas
            			monitor.incFallas();
            			if(monitor.getRam().size()<numeroMarcosPaginas) {
            				int posicionAdd = monitor.addPaginaRam(numPag);
            				tablaPg.replace(paginas.get(numPag), posicionAdd);

            			}else {//Inicializacion de memoria
            				int[] tuplaPagina = monitor.intercambio(numPag);
            				paginas.get(tuplaPagina[0]).setContadorCero();
            				tablaPg.replace(paginas.get(tuplaPagina[0]),-1);
            				tablaPg.replace(paginas.get(numPag), tuplaPagina[1]);
            			}
                    
            		}
            		
            		else{
            			monitor.syncR(buscar);
                }
                try {
                    sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
            }
            	
            aux++;
            

        }
            
            monitor.setFinished();
            br.close();
            System.out.println("El n�mero de fallos de pagina es: "+ monitor.getFallas());    
            
        }
            
            catch (IOException e) {
            e.printStackTrace();
            }
       
    }
    
    

}