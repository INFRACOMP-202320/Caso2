/**
 * Representa una pagina.
 * Se asume que, como en la practica, el tamano de un marco de pagina = tamano de una pagina virtual
 * Por lo tanto, la ubicacion en memoria virtual o en memoria real se representa en este Objeto a traves
 * de un valor booleano.
 */
public class Pagina {


    /***********************
     *                          ATRIBUTOS
     ***********************/

    /**
     * Indica si la pagina fue referenciada en algun punto en el ciclo de reloj actual.
     */
    private boolean R;

    /**
     * Indicador de envejecimiento de la pagina.
     * Se aplica el algoritmo de envejecimiento de Tanenbaum, es decir que las operaciones sobre este entero son sobre los bits.
     */
    private Integer contador;

    /**
     * Id de la pagina.
     * Va desde 0 hasta el numero de paginas ingresadas por el usuario -1.
     */
    private int id;


    /**
     * Construye una pagina y le asigna el id dado por parametro
     * Inicializa los demas parametros. 
     * @param id de la pagina.
     */
    public Pagina(int id){

        this.R = false;
        this.contador = 0;
        this.id = id;
    }

    /***********************
     *                           METODOS
     ***********************/

    /**
     * @return {@code true} si la pagina ha sido referenciada en algun punto durante el presente ciclo de reloj,
     * {@code false} de lo contrario.
     */
    public boolean isR() {
        return R;
    }

    /**
     * Asigna a R {@code true} si la pagina ha sido referenciada en algun punto durante el presente ciclo de reloj,
     * {@code false} de lo contrario.
     * @param r booleano que indica si la pagina se ha referenciado en el presente ciclo de reloj o no.
     */
    public void setR(boolean r) {
        R = r;
    }

    /**
     * @return el valor del indicador de envejecimiento de esta pagina.
     */
    public Integer getContador() {
        return contador;
    }
    
    /**
     * @return el valor del indicador de envejecimiento de esta pagina.
     */
    public void setContadorCero() {
        contador = 0;
    }

    /**
     * Envejece este contador aplicando un right shift logico (>>>) el cual inserta
     * un 0 en el extremo izquierdo del numero binario y realiza un desplazamiento de
     * todos los bits 1 unidad a la derecha, desechando el valor del bit menos significativo
     * (el de mas a la derecha). 
     */
    public void envejecer() {
        this.contador = contador >> 1;
    }

    /**
     * Rejuvenece este contador, indicando que la pagina fue referenciada durante el ciclo que acaba de terminar.
     * Siguiendo la logica de Tanenbaun, si la pagina fue referenciada durante el ciclo actual, entonces se debe
     * realizar un corrimiento de los bits a la derecha e insertar en la posicion del bit mas significativo un 1,
     * en vez de un 0.
     * Para esto, primero se ejecuta un right shift logico a la derecha (>>>) y luego se le suma el valor del bit
     * mas significativo, que en este caso es 2^31 porque el contador es un entero.
     */
    public void rejuvenecer(){
        this.contador = contador >> 1;
        this.contador += 2^30;
    }

    /**
     * Devuelve el numero de pagina virtual al que correponde esta pagina (para referenciacion)
     * @return el id de la pagina virtual. 
     */
    public int getId() {
        return id;
    }
     
    
}