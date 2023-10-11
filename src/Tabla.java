import java.util.ArrayList;
import java.util.Arrays;

public class Tabla {

    public MarcoPagina[] marcos;
    public boolean alerta;

    public Tabla(int espacio){
        marcos = new MarcoPagina[espacio];
        for (int i = 0; i < marcos.length; i++) {
            marcos[i] = new MarcoPagina(i);
        }
        alerta=true;
    }

    public boolean hayPag(int numPag){
        boolean esta = false;
        for (MarcoPagina m: marcos) {
            if(m.estaVacio()) continue;
            if(m.pagCargada == numPag) {
                esta = true;
                break;
            }
        }
        return esta;
    }

    public void cambioEstado(int pag, String instruccion){
        int index = -1;
        for (MarcoPagina m: marcos) {
            if (m.pagCargada == pag) index = m.num;
        }
        if(instruccion.equals("r")) marcos[index].seReferencia();
        else marcos[index].seModifica();
    }

    public synchronized void agregarPagina(int num){
        boolean agregada = false;
        for (int i = 0; i < marcos.length; i++) {
            if(marcos[i].estaVacio()){
                marcos[i].cargarPag(num);
                agregada = true;
                break;
            }
        }
        if(agregada) return;

        System.out.println("Agregando con el algoritmo NRU");
        int indexBorrable = -1;
        for (int i = 0; i < 4; i++) {
            ArrayList<MarcoPagina> clase = encontrarClases(i);
            if(clase.isEmpty()) continue;
            indexBorrable = clase.get(0).num;
            System.out.println("Se eliminó una página de clase " + i);
            break;
        }
        marcos[indexBorrable].cargarPag(num);
    }

    private synchronized ArrayList encontrarClases(int clase){
        ArrayList encontrados = new ArrayList();
        for (MarcoPagina m : marcos){
            if(m.darClase() == clase) encontrados.add(m);
        }
        return encontrados;
    }
    
    // Agrega este método para procesar referencias
    public void procesarReferencia(int paginaVirtual, int paginaFisica, int desplazamiento) {
        // Aquí implementa la lógica para procesar la referencia
        // Deberías actualizar el estado de la tabla de páginas y los marcos de página
        // de acuerdo con los valores proporcionados
    }
}
