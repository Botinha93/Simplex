
import java.util.ArrayList;
import simplex.FuncaoObjetiva;
import simplex.Ponto;
import simplex.Restricao;
import simplex.Simplex;
import simplex.TipoRestricao;
import simplex.TipoResultado;
import simplex.VariavelDecisao;

/* Using B&B can be a extensive process se we use runnables to make it a lil bit faster
not controlling the amount of threads created can be a resource hog, and in extreme cases
slow down your processing, but this app wont get large amounts of data since it is not
suposed to see a real production enviroment.
*/
public class BeB implements Runnable {
    
    Ponto Result; ArrayList<Restricao> restricoes; FuncaoObjetiva objetiva; int vnum;
    public BeB(Ponto Result, ArrayList<Restricao> restricoes, FuncaoObjetiva objetiva, int vnum){
        this.Result=Result;
        this.restricoes=restricoes;
        this.objetiva=objetiva;
        this.vnum=vnum;
    }
    private boolean testVars(Ponto temp){
        for(int i=0;i<vnum;i++)
            if(temp.getValores().get(i)!=Math.floor(temp.getValores().get(i)) )
                return true;
        return false;
    }
    /*i like to call this "recursive threads" aka the cpu frier and memory smasher
    this is the proper B&B we test to see if a valeu != int if it is we do the simplex
    and run it all again on the results, if it is not on any of its variables then we
    see if it is a betther point than the last one and store it
    */
    @Override
    public void run() {
      if(TControler.pontoOtimo==null){
                TControler.pontoOtimo=Result;
                TControler.pontoOtimo.setValorResultado(0.0);
      }
        for (int i=0;i<vnum;i++){
            if (Result.getValores().get(i)!= Math.floor(Result.getValores().get(i)) && Double.isFinite(Result.getValores().get(i))){
                Simplex simplex = new Simplex();
                ArrayList <Double> valores=new ArrayList();
                for(int time=0;time<vnum;time++)
                    valores.add(0.0);
                valores.set(i, 1.0);
                restricoes.add(new Restricao(TipoRestricao.MenorIgual,valores, Math.floor(Result.getValores().get(i))));
                Ponto temp=simplex.CalcularSimplex(CopyRes(restricoes),CopyObj(objetiva));
                if(temp.getTipoResultado()==TipoResultado.Otimo && !TControler.ForceStop){
                    if (testVars(temp))
                        TControler.addQueue(new BeB(temp,CopyRes(restricoes),CopyObj(objetiva),vnum));
                    else if(TControler.pontoOtimo.getValorResultado()<temp.getValorResultado())
                        TControler.pontoOtimo=temp;
                }
                restricoes.remove(restricoes.size()-1);
                restricoes.add(new Restricao(TipoRestricao.MaiorIgual,valores, Math.ceil(Result.getValores().get(i))));
                Ponto temp2=simplex.CalcularSimplex(CopyRes(restricoes),CopyObj(objetiva));
                if(temp2.getTipoResultado()==TipoResultado.Otimo && !TControler.ForceStop){
                    if (testVars(temp2))
                        TControler.addQueue(new BeB(temp2,CopyRes(restricoes),CopyObj(objetiva),vnum));
                    else if(TControler.pontoOtimo.getValorResultado()<temp2.getValorResultado())
                        TControler.pontoOtimo=temp2;
                }
                
        }
        }
        
         TControler.freeResource();
    }
    static private ArrayList<Restricao> CopyRes(ArrayList<Restricao> restricoes) {
        ArrayList<Restricao> restricoescopy = new ArrayList();
        for (int i=0;i<restricoes.size();i++){
            restricoescopy.add(new Restricao(restricoes.get(i).getTipo(),CopyList(restricoes.get(i).getValores()),restricoes.get(i).getLimite()));
        }
        return restricoescopy;
    }
    static private FuncaoObjetiva CopyObj(FuncaoObjetiva objetiva) {
        FuncaoObjetiva objcopy;
        ArrayList <VariavelDecisao> copy=new ArrayList();
        for (int i=0;i<objetiva.getValores().size();i++){
            copy.add(new VariavelDecisao(objetiva.getValores().get(i)));
        }
        objcopy = new FuncaoObjetiva(copy,objetiva.getTipo());
        
        return objcopy;
    }
    static private ArrayList <Double> CopyList(ArrayList <Double> values){
        ArrayList<Double> copy = new ArrayList();
        for (int i=0;i<values.size();i++){
            copy.add(values.get(i));
        }
        return copy;
    }  
}
