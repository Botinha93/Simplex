
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import simplex.Ponto;
import simplex.Simplex;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Tulio Botinha
 */
public class TControler {
    static ArrayList <BeB> queue ;
    static int Resources=0;
    static int maxP;
    static int next=0;
    static initsimplex simples;
    static File local;
    static boolean ForceStop= false;
    static long time;
    static Ponto pontoOtimo;/*static variables can survive evem the worst runnables
    just like cockroaches 
    */
    static public void setUp(initsimplex simples,File Local){
        TControler.simples=simples;
        TControler.local=Local;
        Resources=0;
        next=0;
        ForceStop= false;  
    }
    static public void statrtQueue(int maxP){
        queue=new ArrayList();
        Resources=0;
        TControler.maxP=maxP;
        Logger.getLogger(Process.class.getName()).log(Level.FINE,"BeB started");
    }
    public static void useResource(){
        if (Resources<maxP && queue.size()>0 && queue.get(queue.size()-1)!=null && !ForceStop){
            Thread t = new Thread(queue.get(queue.size()-1));
            t.start();
            queue.remove(queue.size()-1);
            Resources++;
            next++;
        }
        Logger.getLogger(Process.class.getName()).log(Level.SEVERE,"Resources in use: "+Resources+"\nMax number of threads: "+maxP+"\nQueue size: "+queue.size()+"\nNext Element in list: "+next);
    }
    private static void MaxNosReached(){
        if (Parms.MaxNos>0 && next>Parms.MaxNos){
            ForceStop=true;
        }
    }
    public static void freeResource(){
        Resources--;
        useResource();
        isFinished();
    }
    public static void addQueue(BeB toadd){
        queue.add(toadd);
         MaxNosReached();
    }
    private static String[] concat(String[] a, String[] b) {
       int aLen = a.length;
        int bLen = b.length;
       String[] c= new String[aLen+bLen];
       System.arraycopy(a, 0, c, 0, aLen);
       System.arraycopy(b, 0, c, aLen, bLen);
       return c;
    }
    public static void isFinished()
    {
        if (Resources<1){
            stop();
        }else if(Resources<1 && !TControler.ForceStop){
            queue=new ArrayList();
            stop();
        }
    }
    public static void stop(){
        String[] textResult = new String[simples.getResult().getValores().size()+1];/*now we do some somple processing on the simplex response, so we can show it to the user in the response html*/
        for (int i=0;i<simples.getResult().getValores().size();i++)
            textResult[i]="X "+i+": "+simples.getResult().getValores().get(i);
        textResult[textResult.length-1]="Z: "+simples.getResult().getValorResultado()+"     O resultado é: "+simples.getResult().getTipoResultado().name();
        if(Parms.rumBB){
            String[] textResult2 = new String[TControler.pontoOtimo.getValores().size()+2];
            for (int i=0;i<TControler.pontoOtimo.getValores().size();i++)
                textResult2[i]="BeB X"+i+": "+TControler.pontoOtimo.getValores().get(i);
            textResult2[textResult2.length-2]="BeB Z: "+TControler.pontoOtimo.getValorResultado();
            textResult2[textResult2.length-1]=next+" Nós percorridos<br>"+ForceStop+" Parada Forçada<br>"+(System.currentTimeMillis()-time)+" Ms de execução";
            HTMLresponses.resultPage(concat(textResult,textResult2) ,local);/*the proper response to the simplex as the user will see it*/

        }else{
            HTMLresponses.resultPage(textResult ,local);/*the proper response to the simplex as the user will see it*/

        }
    }
    
}
