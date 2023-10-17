import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Monitor {

	private ArrayList<Pagina> paginas;
	
	private ArrayList<Pagina> ram;
	
	private HashMap<Pagina, Integer> tablaPg;
	
	private boolean finished;
	
	private int fallas;
	
	private int numMarcosPagina;
	
	public Monitor() {
		
		this.ram = new ArrayList<Pagina>();
		this.paginas = new ArrayList<Pagina>();
		this.tablaPg = new HashMap<Pagina, Integer>();
		this.finished = false;
		this.fallas = 0;
		
	}
	
    public void initMacros(String nomArchivo, int numMarcosPagina) throws IOException {
    	
    	//numPag
    	//numero de paginas
    	this.numMarcosPagina=numMarcosPagina;
        
        //Inicializacion
        FileReader fr = new FileReader(nomArchivo);
        BufferedReader br = new BufferedReader(fr);
        br.readLine();
        br.readLine();
        br.readLine();
        br.readLine();
        int nr = Integer.parseInt((br.readLine().split("="))[1].trim());
        int np = Integer.parseInt((br.readLine().split("="))[1].trim());
        
        for (int i = 0; i < np; i++) {
        	Pagina pag = new Pagina(i);
            this.paginas.add(pag);
            this.tablaPg.put(pag, -1);
        }
        
        /*for(int i = 0; i < numMarcosPagina; i++) {
    		int numPag =Integer.parseInt(br.readLine().split(",")[1]);
    		Pagina pagina = paginas.get(numPag);
    		this.ram.add(pagina);
    		this.tablaPg.put(pagina, i);
    		pagina.setR(true);
        }*/


        //Crear instancias de AdminTablas y Tanenbaun y mandarlas a correr (.start()).
        
        AdminTablas adminTablas = new AdminTablas(nomArchivo, this);
        adminTablas.start();
        
        Tanenbaum tanenbaum = new Tanenbaum(this);
        tanenbaum.start();
        
        br.close();
        fr.close();
    }
    
    public int getNumMarcosPagina() {
    	return this.numMarcosPagina;
    }
	
    public HashMap<Pagina, Integer> getTablaPg(){
        return this.tablaPg;
    }
    
    public void setTabla(HashMap<Pagina, Integer> tabla){
        this.tablaPg=tabla;
    }
    
    public boolean getFinished(){
        return this.finished;
    }
	
    public void setFinished(){
        this.finished=true;
    }
    
    public synchronized ArrayList<Pagina> getPaginas(){
    	return this.paginas;
    }
    
    public synchronized int[] intercambio(int numPagina){

        int[] rta = new int[2];
        int maximo = Integer.MAX_VALUE;
        for (int i = 0; i < ram.size(); i++) {
            if(ram.get(i).getContador() <= maximo){
                 rta[0] = ram.get(i).getId(); //id pagina
                 rta[1] = i; // posicion ram
                maximo = ram.get(i).getContador();
            }
        }
        
        setRam(rta[1], numPagina);
        paginas.get(numPagina).setR(true);
        paginas.get(rta[0]).setContadorCero();
        
        return rta;
    }
    
    
    
    public synchronized int addPaginaRam(int numPag) {
    	Pagina pagina = paginas.get(numPag);
    	pagina.setR(true);
    	ram.add(pagina);
    	return ram.size()-1;
    }
    
    public void setRam(int info, int numPagina) {
    	ram.set(info, paginas.get(numPagina));
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
    
    public synchronized void syncR(int numPagina) {
    	Pagina pagina = ram.get(numPagina);
    	pagina.setR(true);
    	ram.set(numPagina, pagina);
    }
    
    public synchronized void sincronizacion() {
    	
    	for(int i = 0; i < ram.size(); i++) {
    		
    		Pagina pagina = ram.get(i);
    		boolean ref = pagina.isR();
    		if(ref) {
    			pagina.rejuvenecer();
    			ram.set(i, pagina);
    		}
    		
    		else {
    			pagina.envejecer();
    			ram.set(i, pagina);
    		}
    	}
    	
    	enCero();
    }
    
    public void enCero() {
    	for(int i = 0; i < ram.size(); i++) {
    		Pagina pagina = ram.get(i);
    		pagina.setR(false);
    		ram.set(i, pagina);
    	}
    }
	
}