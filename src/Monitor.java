import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Monitor {

	private ArrayList<Pagina> paginas;
	
	private ArrayList<Pagina> ram;
	
	private HashMap<Integer, Integer> tablaPg;
	
	private boolean finished;
	
	private int fallas;
	
	private int numMarcosPagina;
	
	public Monitor() {

		this.ram = new ArrayList<Pagina>();
		this.paginas = new ArrayList<Pagina>();
		this.tablaPg = new HashMap<Integer, Integer>();
		this.finished = false;
		this.fallas = 0;
	}
	
    public void initMacros(String nomArchivo, int numMarcosPagina) throws IOException {

    	//numero de marcos de pagina (tamano de la Ram)
    	this.numMarcosPagina=numMarcosPagina;
        //Inicializacion
        BufferedReader br = new BufferedReader(new FileReader(nomArchivo));
        br.readLine();
        br.readLine();
        br.readLine();
        br.readLine();
        br.readLine();
        int np = Integer.parseInt((br.readLine().split("="))[1].trim());
        br.close();
        //Inicializa la tabla de paginas
        for (int i = 0; i < np; i++) {
        	Pagina pag = new Pagina(i);
            this.paginas.add(pag);
            this.tablaPg.put(pag.getId(), -1);
        }
        //Crear instancias de AdminTablas y Tanenbaun y mandarlas a correr (.start()).
        Algoritmo adminTablas = new Algoritmo(nomArchivo, this);
        adminTablas.start();
        Tanenbaum tanenbaum = new Tanenbaum(this);
        tanenbaum.start();
    }
    
    /**
     * @return el numero de marcos de pagina.
     */
    public int getNumMarcosPagina() {
    	return this.numMarcosPagina;
    }
	
    /**
     * @return la tabla de paginas.
     */
    public HashMap<Integer, Integer> getTablaPg(){
        return this.tablaPg;
    }
    

    public synchronized boolean getFinished(){
        return this.finished;
    }
	
    public synchronized void setFinished(){
        this.finished=true;
    }
    
    public synchronized ArrayList<Pagina> getPaginas(){
    	return this.paginas;
    }
    

    /**
     * Busca la pagina mas vieja para reemplazarla por la pagina entrante
     * Se ejecuta solo cuando ocurre un fallo de pagina y la ram esta llena.
     * rta[0]= id de pagina a reemplazar y rta[1] = posicion del marco de pagina
     * en ram donde se encuentra la pagina
     * que debe ser reemplazada.
     */
    public synchronized void intercambio(int numPagina){

        int idPag = -1;
        int posPagRam = -1;
        int maximo = Integer.MAX_VALUE;
        for (int i = 0; i < ram.size(); i++) {
            if(ram.get(i).isR()){continue;}
            
            if(ram.get(i).getContador() < maximo){
                idPag = ram.get(i).getId(); //id pagina
                posPagRam = i; // posicion ram
                maximo = ram.get(i).getContador();
            }
        }
        // Saca la pagina mas vieja:
        paginas.get(idPag).setContadorCero();
        tablaPg.replace(idPag,-1);
        // Mete la pagina solicitada a ram:
        ram.set(posPagRam, paginas.get(numPagina));
        paginas.get(numPagina).setR(true);
        tablaPg.replace(numPagina, ram.size()-1);
    }
    
    
    
    public synchronized void addPaginaRam(int numPag) {
    	Pagina pagina = paginas.get(numPag);
    	pagina.setR(true);
    	ram.add(pagina);
        tablaPg.replace(numPag, ram.size()-1);
    }
    
    public ArrayList<Pagina> getRam(){
    	return ram;
    }
    
    public void incFallas() {
    	fallas ++;
    }
    
    public int getFallas() {
    	return fallas;
    }
    
    public synchronized void syncR(int idPag) {
        paginas.get(idPag).setR(true);
    }
    
    public synchronized void sincronizacion() {
    	for(int i = 0; i < ram.size(); i++) {
    		Pagina pagina = ram.get(i);
    		if(pagina.isR()){
                pagina.rejuvenecer();
                pagina.setR(false);
            }else{
                pagina.envejecer();
            }
    	}
    }


    /**
     * Determina si una pagina esta en ram o no.
     * @param idPag id de la pagina que se quiere saber si esta o no en ram.
     * @return {@code true} si la pagina esta en ram {@code false} de lo contrario.
     */
    public boolean estaEnRam(int idPag){
        return tablaPg.get(idPag)!=-1;
    }

}