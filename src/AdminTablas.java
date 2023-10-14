import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Representa el thread encargado de ir actualizando el estado de la tabla de páginas y
 * los marcos de página en memoria real cada 2 milisegundos.
 */
public class AdminTablas extends Thread{


    /**
     * Representa la tabla de paginas
     */
    private Pagina[] tp;

    /**
     * Representa la memoria real
     */
    private Pagina[] ram;
    
    /**
     * Nombre del archivo con las referencias.
     */
    private String nomArchivo;

    /**
     * tamano de la memoria real.
     */
    private int numMarcosPagina;

    /**
     * Constructor del administrador de tablas.
     * @param tp estructura de tipo {@code Pagina[]} que representa la tabla de paginas.
     * @param ram estructura de tipo {@code Pagina[]} que representa la memoria real.
     * @param nomArchivo nombre del archivo con las referencias.
     */
    public AdminTablas(Pagina[] tp, Pagina[] ram, String nomArchivo, int numMarcosPagina){
        this.tp = tp;
        this.ram = ram;
        this.nomArchivo = nomArchivo;
        this.numMarcosPagina = numMarcosPagina;
    }



    /**
     * Busca la pagina mas vieja para reemplazarla por la pagina entrante
     * Se ejecuta solo cuando ocurre un fallo de pagina y la ram esta llena.
     * @return {@code int[]} rta en donde rta[0]= id de pagina a reemplazar y
     * rta[1] = posicion del marco de pagina en ram donde se encuentra la pagina
     * que debe ser reemplazada.
     */
    public int[] buscarPgMasVieja(){

        int[] rta = new int[2];
        rta[0] =-1;
        rta[1] = -1;
        int contadorMasViejo = Integer.MAX_VALUE;
        for (int i = 0; i < ram.length; i++) {
            if(ram[i].getContador() <= contadorMasViejo){
                 rta[0] = ram[i].getId();
                 rta[1] = i;
                contadorMasViejo = ram[i].getContador();
            }
        }
        return rta;
    }



    public void run(){

        try (BufferedReader reader = new BufferedReader(new FileReader(nomArchivo))){
            int numFallas = 0;
            String ref;
            int aux = 1;
            // lee el archivo cargando las referencias una por una.
            while((ref = reader.readLine())!=null){
                if(aux>6){ //referencias empiezan a partir de la fila 7 en el archivo.

                    String[] data = ref.split(",");
                    int numPV = Integer.parseInt(data[1]);

                    if(tp[numPV].isMp()==false){ //fallo de pagina
                        numFallas++;
                        //algoritmo de envejecimiento
                        if(ram.length == numMarcosPagina){ //ram llena, se debe reemplazar un pagina.
                            int[] rta = buscarPgMasVieja(); //rta[0]= id de pagina a reemplazar. rta[1] = posicion del marco de pagina en ram.
                            ram[rta[1]].setMp(false); // cambia el flag de la pagina antes de sacarla
                            ram[rta[1]] = tp[rta[0]]; // saca la pagina vieja de la ram e inserta la necesitada.
                            tp[rta[0]].setMp(true); // marca la pagina recien insertada como tal.
                        }
                    } 
                }
                aux++;
                sleep(2);
            }
            reader.close();
            System.out.println("# Fallas de Pagina: "+ numFallas);
        } catch (Exception e) {
            System.out.println("Ocurrio un problema durante la ejecucion del administrador de tablas:");
            e.printStackTrace();
        }
       
    }

}
