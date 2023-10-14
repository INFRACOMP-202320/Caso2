import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Menú de opciones:");
            System.out.println("1. Crear archivo de referencias");
            System.out.println("2. Calcular el número de fallas de página");
            System.out.println("3. Salir");

            System.out.println("Seleccione una opción: ");
            int opcion = 0;
            try{
                opcion = scanner.nextInt();
            }catch(Exception e){
                System.out.println("Error: debe ingresar un numero");
                continue;
            }
            switch (opcion) {
                case 1: // Opción 1: Crear archivo de referencias.
                    try {
                        // parte 1: Generación de las referencias
                    	System.out.println("El documento 'referencias' se actualizara automáticamente al colocar los datos.");
                        System.out.print("Ingrese el tamaño de página (TP en bytes): ");
                        tamPagina = scanner.nextInt();
                        System.out.print("Ingrese el número de filas de la matriz 1 (NF): ");
                        numFilasMatriz1 = scanner.nextInt();
                        System.out.print("Ingrese el número de columnas de la matriz 1 (NC1, y filas de la matriz 2): ");
                        numColumnasMatriz1 = scanner.nextInt();
                        System.out.print("Ingrese el número de columnas de la matriz 2 (NC2): ");
                        numColumnasMatriz2 = scanner.nextInt();
                        //Crea o actualiza el archivo referencias.txt:
                        System.out.println("Generando archivo...");
                        generarArchivo();
                        System.out.println("Archivo referencias.txt creado exitosamente!");

                    } catch (Exception e) {
                        System.out.println("Error. Debe ingresar un numero valido. \n\n");
                        continue;
                    }
                case 2:// Opción 2: Calcular el número de fallas de página
                    try{
                        System.out.println("Ingrese el numero de marcos de pagina:");
                        numMarcosPagina = scanner.nextInt();
                        System.out.print("Ingrese el nombre del archivo de referencias (incluya la extension, e.g: referencias.txt): ");
                        String archivo = scanner.next();
                        System.out.println("\n            SIMULANDO...");
                        simular(archivo);
                    }catch(Exception e){
                        System.out.println("Error ingresando los datos para la simulacion.\n\n");
                        continue;
                    }
                case 3: // Opción 3: Salir
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, seleccione una opción válida.");
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
            double aux = numElementosMatriz1*TAMANO_ELEMENTO/tamPagina;
            numPaginasMatriz1 = (int) Math.ceil(aux);
            aux = numElementosMatriz2*TAMANO_ELEMENTO/tamPagina;
            numPaginasMatriz2 = (int) Math.ceil(aux);
            aux = numFilasMatriz1*numColumnasMatriz2*TAMANO_ELEMENTO/tamPagina;
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
        double aux = 0.0;
        if(numMatriz==1){//primera en almacenarse (matriz A o 1)
            aux = (fila*numColumnasMatriz1*TAMANO_ELEMENTO + TAMANO_ELEMENTO*col)/tamPagina;
            rta[0] = (int) Math.ceil(aux);
            rta[1] = fila*numColumnasMatriz1*TAMANO_ELEMENTO + TAMANO_ELEMENTO*col - rta[0]*tamPagina;
        }else if(numMatriz==2){//segunda 
            aux = (fila*numColumnasMatriz2*TAMANO_ELEMENTO + TAMANO_ELEMENTO*col)/tamPagina;
            rta[0] = numPaginasMatriz1 + (int) Math.ceil(aux);
            rta[1] = numPaginasMatriz1*tamPagina + fila*numColumnasMatriz2*TAMANO_ELEMENTO + TAMANO_ELEMENTO*col - rta[0]*tamPagina;
        }else{ //tercera
            aux = (fila*numColumnasMatriz2*TAMANO_ELEMENTO + TAMANO_ELEMENTO*col)/tamPagina;
            rta[0] = (int) Math.ceil(aux);
            rta[1] = fila*numColumnasMatriz2*TAMANO_ELEMENTO + TAMANO_ELEMENTO*col - rta[0]*tamPagina;
            rta[0] += (numPaginasMatriz1 + numPaginasMatriz2);
        }
        return rta;
    }


    public static void simular(String nomArchivo) {
        Pagina[] tablaPg = new Pagina[numPaginasMatriz1+numPaginasMatriz2+numPaginasMatriz3];
        Pagina[] ram = new Pagina[numMarcosPagina];
        for (int i = 0; i < tablaPg.length; i++) {
            tablaPg[i] = new Pagina(i);
        }

        //Crear instancias de AdminTablas y Tanenbaun y mandarlas a correr (.start()).
    }

}
