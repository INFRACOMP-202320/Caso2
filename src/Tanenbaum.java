
/**
 * Thread encargado de ejecutar el algoritmo de envejecimiento cada 1 milisegundo.
 */
public class Tanenbaum extends Thread{


    /**
     * Representa la memoria real.
     */
    private Pagina[] ram;


    /**
     * Constructor del ejecutor del algoritmo de Tanenbaum
     * @param ram estructura de tipo {@code Pagina[]} que representa la
     * memoria real.
     */
    public Tanenbaum(Pagina[] ram){

        this.ram = ram;
    }



    public void run(){
        //envejece y rejuvenece las paginas, cada milisegundo
    }
    
}
