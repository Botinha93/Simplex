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
public class Restricao {
    private TipoRestricao tipo;
    private ArrayList<Double> valores;
    private Double limite;
    private Double folga;

    public Restricao(TipoRestricao tipoRestricao, ArrayList<Double> valores, Double limite) {
        this.tipo = tipoRestricao;
        this.valores = valores;
        this.limite = limite;
    }
    
    /**
     * @return the tipo
     */
    public TipoRestricao getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(TipoRestricao tipo) {
        this.tipo = tipo;
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
     * @return the limite
     */
    public Double getLimite() {
        return limite;
    }

    /**
     * @return the folga
     */
    public Double getFolga() {
        return folga;
    }

    /**
     * @param limite the limite to set
     */
    public void setLimite(Double limite) {
        this.limite = limite;
    }

    /**
     * @param folga the folga to set
     */
    public void setFolga(Double folga) {
        this.folga = folga;
    }

    /**
     * @return the membroLivre
     */
    public Double getMembroLivre() {
        return getLimite();
    }

  
}
