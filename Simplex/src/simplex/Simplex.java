package simplex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author pedro
 */
public class Simplex {

    public Ponto CalcularSimplex(ArrayList<Restricao> restricoes, FuncaoObjetiva objetiva) {
        Ponto resultado = new Ponto();

        objetiva = CalcularMembrosLivres(objetiva);

        restricoes = InserirVariaveisFolga(restricoes);

        TabelaSimplex tabela = new TabelaSimplex(restricoes, objetiva);

        resultado = FaseUm(tabela);

        if (resultado.getTipoResultado() == TipoResultado.Otimo || resultado.getTipoResultado() == TipoResultado.MultiplasSolucoes) {
            resultado.setValorResultado(tabela.getCelulas()[0][0].getSuperior());
            if (objetiva.getTipo() == TipoFuncao.Maximizacao) {
                resultado.setValorResultado(resultado.getValorResultado() * (-1));
            }
        } else {
            resultado.setValorResultado(Double.NaN);
        }

        return resultado;
    }

    private Ponto FaseUm(TabelaSimplex tabela) {

        Ponto retorno = null;

        int posicaoNegativo = -1;

        do {
            //Operação 1
            posicaoNegativo = GetPosicaoLinhaNegativo(tabela, -1);

            if (posicaoNegativo > -1) {
                //Operação 2

                int colunaPermissivel = -1;

                for (int j = 1; j < tabela.getCelulas()[0].length; j++) {
                    if (tabela.getCelulas()[posicaoNegativo][j].getSuperior() < 0) {
                        colunaPermissivel = j;
                        break;
                    }
                }

                if (colunaPermissivel == -1) {
                    return new Ponto(TipoResultado.Impossivel);
                } else {
                    //Operação 3

                    int linhaPermissivel = -1;
                    double quociente = Double.MAX_VALUE;

                    for (int i = 1; i < tabela.getCelulas().length; i++) {
                        if (tabela.getCelulas()[i][colunaPermissivel].getSuperior() == 0) {
                            continue;
                        }

                        double temp = tabela.getCelulas()[i][0].getSuperior() / tabela.getCelulas()[i][colunaPermissivel].getSuperior();

                        if (temp > 0 && temp < quociente) {
                            linhaPermissivel = i;
                            quociente = temp;
                        }

                    }

                    tabela = AlgoritmoTroca(tabela, linhaPermissivel, colunaPermissivel, tabela.getCelulas().length, tabela.getCelulas()[0].length);

                }
            }

            posicaoNegativo = GetPosicaoLinhaNegativo(tabela, -1);

            Validacoes(tabela, tabela.getCelulas().length, tabela.getCelulas()[0].length);

        } while (posicaoNegativo != -1);
        // só pra compilar, não faz parte da logica seu animal

        retorno = FaseDois(tabela);

        return retorno;
    }

    private int GetPosicaoLinhaNegativo(TabelaSimplex tabela, int posicaoNegativo) {
        for (int i = 1; i < tabela.getCelulas().length; i++) {
            if (tabela.getCelulas()[i][0].getSuperior() < 0) {
                posicaoNegativo = i;
                break;
            }
        }
        return posicaoNegativo;
    }

    private int GetPosicaoColunaPositivo(TabelaSimplex tabela, int posicaoPositivo) {
        for (int j = 1; j < tabela.getCelulas()[0].length; j++) {
            if (tabela.getCelulas()[0][j].getSuperior() > 0) {
                posicaoPositivo = j;
                break;
            }
        }
        return posicaoPositivo;
    }

    private Ponto FaseDois(TabelaSimplex tabela) {

        //Operação 1
        int posicaoPositivo = -1;

        posicaoPositivo = GetPosicaoColunaPositivo(tabela, posicaoPositivo);

        while (posicaoPositivo != -1) {

            int linhaPermissivel = -1;
            //Operação 2
            for (int i = 1; i < tabela.getCelulas().length; i++) {
                if (tabela.getCelulas()[i][posicaoPositivo].getSuperior() > 0) {
                    linhaPermissivel = i;
                    break;
                }
            }

            if (linhaPermissivel != -1) {
                //Operação 3

                double quociente = Double.MAX_VALUE;

                for (int i = 1; i < tabela.getCelulas().length; i++) {
                    Celula elemento = tabela.getCelulas()[i][posicaoPositivo];
                    if (elemento.getSuperior() == 0) {
                        continue;
                    } else {
                        double temp = tabela.getCelulas()[i][0].getSuperior() / tabela.getCelulas()[i][posicaoPositivo].getSuperior();

                        if (temp > 0 && temp < quociente) {
                            linhaPermissivel = i;
                            quociente = temp;
                        }
                    }

                }

                tabela = AlgoritmoTroca(tabela, linhaPermissivel, posicaoPositivo, tabela.getCelulas().length, tabela.getCelulas()[0].length);

                

            } else {

                return new Ponto(TipoResultado.Ilimitado);
            }

            posicaoPositivo = GetPosicaoColunaPositivo(tabela, -1);

        }

        Ponto retorno = GetPontoOtimo(tabela);

        return retorno;

    }

    private Ponto GetPontoOtimo(TabelaSimplex tabela) throws NumberFormatException {

        HashMap<Integer, Double> colunas = new HashMap<Integer, Double>();
        for (int j = 1; j < tabela.getCelulas()[0].length; j++) {
            String nString = tabela.getDescSuperior().get(j);
            int n = Integer.parseInt(nString.substring(1));

            colunas.put(n, 0d);

        }
        for (int i = 1; i < tabela.getCelulas().length; i++) {
            String nString = tabela.getDescLateral().get(i);
            int n = Integer.parseInt(nString.substring(1));

            colunas.put(n, tabela.getCelulas()[i][0].getSuperior());

        }
        TreeMap<Integer, Double> orderedColunas = new TreeMap<>(colunas);
        Ponto retorno = new Ponto(TipoResultado.Otimo);

        ArrayList<Double> valores = new ArrayList<>();
        for (int x = 0; x < orderedColunas.values().size(); x++) {
            valores.add((Double) orderedColunas.values().toArray()[x]);
        }
        retorno.setValores(valores);
        //retorno.setValores((ArrayList<Double>) orderedColunas.values());
        return retorno;
    }

    private ArrayList<Restricao> InserirVariaveisFolga(ArrayList<Restricao> restricoes) {

        for (Restricao rest : restricoes) {
            switch (rest.getTipo()) {
                case MaiorIgual:
                    rest.setFolga(-1d);
                    break;
                case MenorIgual:
                    rest.setFolga(1d);
                    break;
                default:
                    rest.setTipo(TipoRestricao.MenorIgual);
                    rest.setFolga(1d);
                    break;
            }

            for (int x = 0; x < rest.getValores().size(); x++) {
                rest.getValores().set(x, rest.getValores().get(x) * rest.getFolga());
            }
            rest.setLimite(rest.getFolga() * rest.getLimite());

        }

        return restricoes;
    }

    private FuncaoObjetiva CalcularMembrosLivres(FuncaoObjetiva objetiva) {

        for (int i = 0; i < objetiva.getValores().size(); i++) {
            if (objetiva.getTipo() == TipoFuncao.Minimizacao) {
                objetiva.getValores().set(i, objetiva.getValores().get(i) * (-1));
            }
        }

        //objetiva.setTipo(objetiva.getTipo() == TipoFuncao.Maximizacao ? TipoFuncao.Minimizacao : TipoFuncao.Maximizacao);
        return objetiva;
    }

    private TabelaSimplex AlgoritmoTroca(TabelaSimplex tabela, int linhaPermissivel, int colunaPermissivel, int maxLinha, int maxColuna) {

        Celula elementoPermissivel = tabela.getCelulas()[linhaPermissivel][colunaPermissivel];

        double inverso = Math.pow(elementoPermissivel.getSuperior(), -1);

        elementoPermissivel.setInferior(inverso);

        //Calcula a sub celula inferior das celulas que estão da coluna e linha permissivel -- Faz a cruz
        for (int j = 0; j < maxColuna; j++) {
            if (j != colunaPermissivel) {
                double temp = tabela.getCelulas()[linhaPermissivel][j].getSuperior() * elementoPermissivel.getInferior();
                tabela.getCelulas()[linhaPermissivel][j].setInferior(temp);
            }
        }

        for (int i = 0; i < maxLinha; i++) {
            if (i != linhaPermissivel) {
                double temp = tabela.getCelulas()[i][colunaPermissivel].getSuperior() * ((-1) * elementoPermissivel.getInferior());
                tabela.getCelulas()[i][colunaPermissivel].setInferior(temp);
            }
        }

        for (int i = 0; i < maxLinha; i++) {

            if (i != linhaPermissivel) {

                for (int j = 0; j < maxColuna; j++) {

                    if (j != colunaPermissivel) {

                        Celula elemento = tabela.getCelulas()[i][j];

                        double temp = tabela.getCelulas()[i][colunaPermissivel].getInferior() * tabela.getCelulas()[linhaPermissivel][j].getSuperior();

                        elemento.setInferior(temp);
                    }
                }
            }
        }

        String linhaDesc = tabela.getDescLateral().get(linhaPermissivel);
        String colunaDesc = tabela.getDescSuperior().get(colunaPermissivel);

        tabela.getDescLateral().set(linhaPermissivel, colunaDesc);
        tabela.getDescSuperior().set(colunaPermissivel, linhaDesc);

        for (int i = 0; i < maxLinha; i++) {

            for (int j = 0; j < maxColuna; j++) {

                if (i == linhaPermissivel || j == colunaPermissivel) {

                    tabela.getCelulas()[i][j].setSuperior(tabela.getCelulas()[i][j].getInferior());

                } else {

                    tabela.getCelulas()[i][j].setSuperior(tabela.getCelulas()[i][j].getInferior() + tabela.getCelulas()[i][j].getSuperior());

                }

                tabela.getCelulas()[i][j].setInferior(Double.NaN);
            }
        }

        return tabela;
    }

    private void Validacoes(TabelaSimplex tabela, int maxLinha, int maxColuna) {

    }

}
