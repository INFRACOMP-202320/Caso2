import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Menú de opciones:");
            System.out.println("1. Generar referencias y crear archivo");
            System.out.println("2. Calcular el número de fallas de página");
            System.out.println("3. Salir");

            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    // Opción 1: Generar referencias y crear archivo
                    try {
                    	System.out.println("El documento 'referencias' se actualizara al colocar los datos.");
                        System.out.print("Ingrese el tamaño de página (TP en bytes): ");
                        int tamPagina = scanner.nextInt();
                        System.out.print("Ingrese el número de filas de la matriz 1 (NF): ");
                        int numFilasMatriz1 = scanner.nextInt();
                        System.out.print("Ingrese el número de columnas de la matriz 1 (NC1, y filas de la matriz 2): ");
                        int numColumnasMatriz1 = scanner.nextInt();
                        System.out.print("Ingrese el número de columnas de la matriz 2 (NC2): ");
                        int numColumnasMatriz2 = scanner.nextInt();

                        
                        generarReferenciasArchivo(tamPagina, numFilasMatriz1, numColumnasMatriz1, numColumnasMatriz2);
                        System.out.println("Referencias generadas y archivo creado exitosamente.");
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Error al generar referencias y archivo.");
                    }
                case 2:
                    // Opción 2: Calcular el número de fallas de página
                case 3:
                    // Opción 3: Salir
                    System.exit(0);
                    break;

                default:
                    System.out.println("Opción no válida. Por favor, seleccione una opción válida.");
                    break;
 
                
            }
        }
    }

    
    private static void generarReferenciasArchivo(int tamPagina, int numFilasMatriz1, int numColumnasMatriz1, int numColumnasMatriz2) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("referencias.txt"))) {
            int numElementosMatriz1 = numFilasMatriz1 * numColumnasMatriz1;
            int numElementosMatriz2 = numColumnasMatriz1 * numColumnasMatriz2;
            int numReferencias = numElementosMatriz1 + numElementosMatriz2;
            int tamElemento = 4; 
            int numPaginasMatriz1 = (int) Math.ceil((double) numElementosMatriz1 * tamElemento / tamPagina);
            int numPaginasMatriz2 = (int) Math.ceil((double) numElementosMatriz2 * tamElemento / tamPagina);
            int numPaginasTotales = numPaginasMatriz1 + numPaginasMatriz2;

            writer.write("TP=" + tamPagina + "\n");
            writer.write("NF=" + numFilasMatriz1 + "\n");
            writer.write("NC1=" + numColumnasMatriz1 + "\n");
            writer.write("NC2=" + numColumnasMatriz2 + "\n");
            writer.write("NR=" + numReferencias + "\n");
            writer.write("NP=" + numPaginasTotales + "\n");

     
            for (int i = 0; i < numFilasMatriz1; i++) {
                for (int j = 0; j < numColumnasMatriz1; j++) {
                    int referencia = (i * numColumnasMatriz1 + j) * tamElemento;
                    String linea = "[A-" + i + "-" + j + "]," + (referencia / tamPagina) + "," + (referencia % tamPagina) + "\n";
                    writer.write(linea);
                }
            }

            for (int i = 0; i < numColumnasMatriz1; i++) {
                for (int j = 0; j < numColumnasMatriz2; j++) {
                    int referencia = (i * numColumnasMatriz2 + j) * tamElemento + numElementosMatriz1 * tamElemento;
                    String linea = "[B-" + i + "-" + j + "]," + (referencia / tamPagina) + "," + (referencia % tamPagina) + "\n";
                    writer.write(linea);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
