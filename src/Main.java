import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Clase principal del programa
 */
public class Main {

    /************************************************************
     * ATRIBUTOS
     ************************************************************/

    /**
     * Tamano de un entero segun el enunciado (en Bytes).
     */
    private static final int TAMANO_ELEMENTO = 4;

    /**
     * Tamano de una pagina en Bytes.
     */
    private static int tamPagina;

    /**
     * Numero de filas en la matriz 1.
     */
    private static int numFilasMatriz1;

    /**
     * Numero de columnas en la matriz 1 (tambien representa el numero de filas en la matriz 2).
     */
    private static int numColumnasMatriz1;

    /**
     * Numero de columnas en la matriz 2.
     */
    private static int numColumnasMatriz2;

    /**
     * Numero de paginas que ocupa la matriz 1
     */
    private static int numPaginasMatriz1;

    /**
     * Numero de paginas que ocupa la matriz 2
     */
    private static int numPaginasMatriz2;

    /**
     * Numero de paginas que ocupa la matriz 3
     */
    private static int numPaginasMatriz3;

    /**
     * Numero de marcos de pagina para la simulacion.
     */
    private static int numMarcosPagina;


    /**
     * Metodo principal
     * Aqui inicia la ejecucion del programa.
     * @param args no se utiliza.
     */
    public static void main(String[] args) {

        // Scanner scanner = new Scanner(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean salir = false;
        boolean mostrarMenu = true;
        while (!salir && mostrarMenu == true) {
            System.out.println("Menú de opciones:");
            System.out.println("1. Crear archivo de referencias");
            System.out.println("2. Calcular el número de fallas de página");
            System.out.println("3. Salir");

            System.out.println("Seleccione una opción: ");
            int opcion = 0;
            try{
                opcion = Integer.parseInt(reader.readLine());
            }catch(Exception e){
                System.out.println("Error: debe ingresar un numero\n");
                continue;
            }
            switch (opcion) {
                case 1: // Opción 1: Crear archivo de referencias.
                    try {
                        // parte 1: Generación de las referencias
                    	System.out.println("El documento 'referencias' se actualizara automáticamente al colocar los datos.");
                        System.out.println("Ingrese el tamaño de página (TP en bytes): ");
                        tamPagina = Integer.parseInt(reader.readLine());
                        System.out.println("Ingrese el número de filas de la matriz 1 (NF): ");
                        numFilasMatriz1 = Integer.parseInt(reader.readLine());
                        System.out.println("Ingrese el número de columnas de la matriz 1 (NC1, y filas de la matriz 2): ");
                        numColumnasMatriz1 = Integer.parseInt(reader.readLine());
                        System.out.println("Ingrese el número de columnas de la matriz 2 (NC2): ");
                        numColumnasMatriz2 = Integer.parseInt(reader.readLine());
                        //Crea o actualiza el archivo referencias.txt:
                        System.out.println("Generando archivo...");
                        generarArchivo();
                        System.out.println("Archivo referencias.txt creado exitosamente!\n");
                    } catch (Exception e) {
                        System.out.println("Error. Debe ingresar un numero valido. \n");
                        continue;
                    }
                    break;
                case 2:// Opción 2: Calcular el número de fallas de página
                    try{
                    	mostrarMenu = false;
                        System.out.println("Ingrese el numero de marcos de pagina: ");
                        numMarcosPagina = Integer.parseInt(reader.readLine());
                        System.out.println("Ingrese el nombre del archivo de referencias (incluya la extension, e.g: referencias.txt): ");
                        String archivo = reader.readLine();
                        System.out.println("\n            SIMULANDO...");
                        Monitor monitor = new Monitor();
                        monitor.initMacros(archivo, numMarcosPagina);
                    }catch(Exception e){
                        System.out.println("Error ingresando los datos para la simulacion.\n");
                        continue;
                    }
                    break;
                case 3: // Opción 3: Salir
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, seleccione una opción válida.\n");
                    break;
            }
        }
    }

    /**
     * Metodo encargado de ejecutar la primera opcion del menu de opciones: Generar referencias y crear archivo.
     */
    private static void generarArchivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("referencias.txt"))) {

            int numElementosMatriz1 = numFilasMatriz1 * numColumnasMatriz1;
            int numElementosMatriz2 = numColumnasMatriz1 * numColumnasMatriz2;
            int numReferencias = (numColumnasMatriz1*2+1)*numFilasMatriz1*numColumnasMatriz2;
            double aux = (double) numElementosMatriz1*TAMANO_ELEMENTO/tamPagina;
            numPaginasMatriz1 = (int) Math.ceil(aux);
            aux = (double) numElementosMatriz2*TAMANO_ELEMENTO/tamPagina;
            numPaginasMatriz2 = (int) Math.ceil(aux);
            aux = (double) numFilasMatriz1*numColumnasMatriz2*TAMANO_ELEMENTO/tamPagina;
            numPaginasMatriz3 = (int) Math.ceil(aux);
            int numPaginasTotales = numPaginasMatriz1 + numPaginasMatriz2 + numPaginasMatriz3;

            //comienza escritura del archivo:
            writer.write("TP=" + tamPagina + "\n");
            writer.write("NF=" + numFilasMatriz1 + "\n");
            writer.write("NC1=" + numColumnasMatriz1 + "\n");
            writer.write("NC2=" + numColumnasMatriz2 + "\n");
            writer.write("NR=" + numReferencias + "\n");
            writer.write("NP=" + numPaginasTotales + "\n");

            //contruye las referencias:
            generarReferencias(writer);

        } catch (IOException e) {
            System.out.println("Ocurrio un problema durante la escritura del archivo de referencias: "+"\n\n");
            e.printStackTrace();
        }
    }

    /**
     * Genera las referencias para el archivo de referencias a partir del algoritmo para multiplicar matrices dado en el enunciado. 
     * @param writer {@code BufferedWriter} para escribir sobre el archivo referencias.txt
     * @throws IOException si ocurre un problema de tipo I/O durante la escritura de las referencias en el archivo. 
     */
    public static void generarReferencias(BufferedWriter writer) throws IOException{
         int[] rta = null;
        for (int i = 0; i < numFilasMatriz1; i++) {
            for (int j = 0; j < numColumnasMatriz2; j++) {
                for (int k = 0; k < numColumnasMatriz1; k++) {
                    rta = paginaVirtual(1,i,k);
                    writer.write("[A-"+i+"-"+k+"],"+rta[0]+","+rta[1]+"\n");
                    rta = paginaVirtual(2,k,j);
                    writer.write("[B-"+k+"-"+j+"],"+rta[0]+","+rta[1]+"\n");
                }
                rta = paginaVirtual(3,i,j);
                writer.write("[C-"+i+"-"+j+"],"+rta[0]+","+rta[1]+"\n");
            }
        }
    }

    /**
     * Devuelve el numero de pagina virtual y el desplazamiento del elemento que se referencia.
     * @param numMatriz 1 si es la primera matriz, 2 si es la segunda matriz, 3 si es la tercera matriz.
     * @param fila fila del elemento .
     * @param col columna del elemento.
     * @return {@code int[]} donde [0] contiene el numero de pagina virtual y [1] contiene el desplazamiento del elemento en la pagina virtual. 
     */
    public static int[] paginaVirtual(int numMatriz, int fila, int col){
     
        int[] rta = new int[2];
        int aux;
        if(numMatriz==1){//primera en almacenarse (matriz A o 1)
            aux = fila*numColumnasMatriz1*TAMANO_ELEMENTO + TAMANO_ELEMENTO*col;
            rta[0] = aux/tamPagina; //division entera
            rta[1] = aux - rta[0]*tamPagina;
        }else if(numMatriz==2){//segunda
            aux = fila*numColumnasMatriz2*TAMANO_ELEMENTO + TAMANO_ELEMENTO*col;
            rta[0] = numPaginasMatriz1 + aux/tamPagina;
            rta[1] = numPaginasMatriz1*tamPagina + aux - rta[0]*tamPagina;
        }else{ //tercera
            aux = fila*numColumnasMatriz2*TAMANO_ELEMENTO + TAMANO_ELEMENTO*col;
            rta[0] = aux/tamPagina;
            rta[1] = aux - rta[0]*tamPagina;
            rta[0] += (numPaginasMatriz1 + numPaginasMatriz2);
        }
        return rta;
    }


}
