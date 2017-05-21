package simplex;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pedro
 */
class Celula {
    private double superior;
    private double inferior;

    Celula(Double superior, Double inferior) {
        setSuperior(superior);
        setInferior(inferior);
    }

    /**
     * @return the superior
     */
    public double getSuperior() {
        return superior;
    }

    /**
     * @return the inferior
     */
    public double getInferior() {
        return inferior;
    }

    /**
     * @param superior the superior to set
     */
    public void setSuperior(double superior) {
        this.superior = superior;
    }

    /**
     * @param inferior the inferior to set
     */
    public void setInferior(double inferior) {
        this.inferior = inferior;
    }
}
