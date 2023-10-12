public class Actualizador extends Thread {
    
    private Tabla tabla;

    public Actualizador(Tabla t) {
        tabla = t;
    }

    public void actualizar() {
        int tamaño = tabla.marcos.length;
        int i = 0;
        while (i < tamaño) {
            MarcoPagina marco = tabla.marcos[i];
            int clase = marco.darClase();
            if (clase == 2 || clase == 3) {
                marco.R = false;
            }
            i++;
        }
    }

    public void run() {
        while (tabla.alerta) {
            try {
                sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            actualizar();
        }
    }
}
