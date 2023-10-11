public class MarcoPagina {
    public boolean R;
    public int pagCargada;
    public boolean M;
    public int num;

    public MarcoPagina(int num) {
        R = false;
        M = false;
        this.num = num;
        pagCargada = -1; //-1 representa una página vacía
    }

    public void cargarPag(int pagCargada){
        this.pagCargada = pagCargada;
        R = false;
        M = false;
    }

    public synchronized boolean haSidoModificada() { return  M; }

    public synchronized void seModifica() { M = true; }

    public synchronized boolean haSidoReferenciada() { return R; }

    public synchronized void seReferencia() { R = true; }

    public synchronized void cambiarReferenciada(){
        R = false;
    }

    public int darClase(){ //Retorna la clase de este marco de página
        if (R==false && M==false) return 0;
        if (!R && M) return 1;
        if (R && !M) return 2;
        return 3;
    }

    public boolean estaVacio(){
        return pagCargada == -1;
    }

    public String toString(){
        return "Marco #" + num + ", página #" + pagCargada + ", R es " + R + ", M es " + M;
    }
}
