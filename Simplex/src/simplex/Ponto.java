package simplex;


import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pedro
 */
public class Ponto {
    private ArrayList<Double> valores;
    
    private TipoResultado tipoResultado;
    
    private Double valorResultado;
    
    Ponto(){
        this.valores =  new ArrayList<>();
    }

    Ponto(TipoResultado tipoResultado) {
        this();
        this.tipoResultado = tipoResultado;
        
    }

    /**
     * @return the valores
     */
    public ArrayList<Double> getValores() {
        return valores;
    }

    /**
     * @param valores the valores to set
     */
    public void setValores(ArrayList<Double> valores) {
        this.valores = valores;
    }

    /**
     * @return the tipoResultado
     */
    public TipoResultado getTipoResultado() {
        return tipoResultado;
    }

    /**
     * @return the valorResultado
     */
    public Double getValorResultado() {
        return valorResultado;
    }

    /**
     * @param tipoResultado the tipoResultado to set
     */
    public void setTipoResultado(TipoResultado tipoResultado) {
        this.tipoResultado = tipoResultado;
    }

    /**
     * @param valorResultado the valorResultado to set
     */
    public void setValorResultado(Double valorResultado) {
        this.valorResultado = valorResultado;
    }

 
}
