import java.util.ArrayList;
import java.util.List;

public class AdminTabla extends Thread {
    private Tabla tabla;
    private List<String> referencias;

    public AdminTabla(Tabla t, List<String> referencias) {
        tabla = t;
        this.referencias = referencias;
    }

    private void ejecutarReferencia(String ref) {
        // Implementa la lógica para procesar las referencias y actualizar la tabla de páginas
        // Recuerda considerar el número de marcos de página y las acciones de lectura/escritura

        String[] partes = ref.split(",");
        if (partes.length == 3) {
            int paginaVirtual = Integer.parseInt(partes[0].substring(1));
            int paginaFisica = Integer.parseInt(partes[1]);
            int desplazamiento = Integer.parseInt(partes[2].substring(0, partes[2].length() - 1));

            // Realiza las acciones correspondientes en la tabla de páginas
            tabla.procesarReferencia(paginaVirtual, paginaFisica, desplazamiento);
        }
    }

    public void run() {
        for (String ref : referencias) {
            ejecutarReferencia(ref);

            try {
                sleep(1); // Simula una nueva referencia cada milisegundo
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        tabla.alerta = false;
    }
}

