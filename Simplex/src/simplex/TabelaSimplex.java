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
public class TabelaSimplex {

    private Celula[][] celulas;

    private ArrayList<String> DescSuperior;
    private ArrayList<String> DescLateral;

    TabelaSimplex(ArrayList<Restricao> restricoes,FuncaoObjetiva objetiva) {

        int contador = 1;
        
        DescSuperior = new ArrayList<>();
        DescLateral = new ArrayList<>();
        
        DescSuperior.add("ML");
        DescLateral.add("f(x)");
        
        for(int j = 0; j < objetiva.getValores().size(); j++)
        {
            DescSuperior.add("x"+contador);
            contador++;
        }
        
        for(int i = 0; i < restricoes.size(); i++)
        {
            DescLateral.add("x"+contador);
            contador++;
        }
        
        
        celulas = new Celula[restricoes.size() + 1][objetiva.getValores().size() + 1];

        celulas[0][0] = new Celula(0d, Double.NaN);

        for (int i = 0; i < objetiva.getValores().size() ; i++) {
            celulas[0][i + 1] = new Celula(objetiva.getValores().get(i), Double.NaN);
        }

        for (int i = 1; i < restricoes.size()+1; i++) {
            for (int j = 0; j < objetiva.getValores().size()+1; j++) {
                if (j == 0) {
                    celulas[i][j] = new Celula(restricoes.get(i-1).getMembroLivre(), Double.NaN);
                } else {
                    celulas[i][j] = new Celula(restricoes.get(i-1).getValores().get(j - 1), Double.NaN);
                }
            }
        }
    }

   

    /**
     * @return the celulas
     */
    public Celula[][] getCelulas() {
        return celulas;
    }

    /**
     * @return the DescSuperior
     */
    public ArrayList<String> getDescSuperior() {
        return DescSuperior;
    }

    /**
     * @return the DescLateral
     */
    public ArrayList<String> getDescLateral() {
        return DescLateral;
    }

    /**
     * @param celulas the celulas to set
     */
    public void setCelulas(Celula[][] celulas) {
        this.celulas = celulas;
    }

    /**
     * @param DescSuperior the DescSuperior to set
     */
    public void setDescSuperior(ArrayList<String> DescSuperior) {
        this.DescSuperior = DescSuperior;
    }

    /**
     * @param DescLateral the DescLateral to set
     */
    public void setDescLateral(ArrayList<String> DescLateral) {
        this.DescLateral = DescLateral;
    }

}
