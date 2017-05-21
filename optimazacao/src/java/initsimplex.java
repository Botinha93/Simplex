
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import simplex.FuncaoObjetiva;
import simplex.Ponto;
import simplex.Restricao;
import simplex.Simplex;
import simplex.TipoFuncao;
import simplex.TipoRestricao;
import simplex.TipoResultado;
import simplex.VariavelDecisao;


public class initsimplex {
    /* Var declaration */
    private int nvar;
    private int nfunc ;
    private String orien ;
    private double respArray[][];
    private int resptype[];
    private double funcobjec[];
    private Ponto resultado;
    
    /*generic contructor, only sets the variables */
    public initsimplex(int nvar,int nfunc,String orien,double respArray[][],int resptype[],double funcobjec[]){
        this.nvar=nvar;
        this.nfunc=nfunc;
        this.orien=orien;
        this.respArray=respArray;
        this.resptype=resptype;
        this.funcobjec=funcobjec;
        calcVars();
    }
    
    /*this functions takes the descerealized values and set then acording to the simplex library*/
    private void calcVars(){
        ArrayList<Restricao> restricoes = new ArrayList();
        ArrayList<VariavelDecisao> variaveisDecisao = new ArrayList();
        FuncaoObjetiva objetiva;
        for (int f=0;f<nfunc;f++){
            ArrayList<Double> valores = new ArrayList();
            for (int v=0;v<nfunc;v++){
                valores.add(respArray[f][v]);
                variaveisDecisao.add(new VariavelDecisao(respArray[f][v]));
            }
            double lin= respArray[f][nvar];
            TipoRestricao tipo;
            switch (resptype[f]) {
            case 3:  tipo = TipoRestricao.Igual;
                     break;
            case 4:  tipo = TipoRestricao.MaiorIgual;
                     break;
            case 5:  tipo = TipoRestricao.MenorIgual;
                     break;
            default:  tipo = TipoRestricao.Igual;
                     break;
            }
            restricoes.add(new Restricao(tipo,valores,lin));
        }
        ArrayList<VariavelDecisao> variaveis=new ArrayList();
        for (int v=0;v<nvar;v++){
                variaveis.add(new VariavelDecisao(funcobjec[v]));
         }
        if(orien.contains("Max")){
            objetiva = new FuncaoObjetiva(variaveis, TipoFuncao.Maximizacao);
        }else{
            objetiva = new FuncaoObjetiva(variaveis, TipoFuncao.Minimizacao);
        }
        Logger.getLogger(Process.class.getName()).log(Level.FINE,"Simplex started");
        Simplex simplex = new Simplex();/*the simplex!*/
        resultado = simplex.CalcularSimplex(restricoes, objetiva);
        TControler.setUp(this, Parms.local);
        if(resultado.getTipoResultado()==TipoResultado.Otimo && Parms.rumBB){
            TControler.statrtQueue(Parms.threads);
            TControler.addQueue(new BeB(resultado,restricoes,objetiva,nvar));
            Logger.getLogger(Process.class.getName()).log(Level.FINE,"First BeB sended");
            TControler.useResource();
        }else{
            TControler.stop();
        }
    }
    public Ponto getResult(){/*gets the simplex response dhuu*/
        return resultado;
    }
}
