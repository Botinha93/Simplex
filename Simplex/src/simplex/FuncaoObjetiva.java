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
public class FuncaoObjetiva {
    private TipoFuncao tipo;
    private ArrayList<Double> valores;

    public FuncaoObjetiva(ArrayList<VariavelDecisao> variaveis, TipoFuncao tipoFuncao) {
        this.valores = new ArrayList<>();
        
        for(VariavelDecisao variavel : variaveis)
        {
            this.valores.add(variavel.getValor());
        }
        
        this.tipo = tipoFuncao;
    }

    /**
     * @return the tipo
     */
    public TipoFuncao getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(TipoFuncao tipo) {
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
}
