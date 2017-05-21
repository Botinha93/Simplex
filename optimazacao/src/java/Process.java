
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Process {
    /*this variable sets the id for the result html, since http can only recieve one response per request,
    we need a way to send 2 pages if we want the fancy loadding screen
    */
    private static String connId;
    /*this function descerializes the filds that we got from the html main form
    request.getParameter returns a string from the respective post paran
    that is converted to the proper data type and sets in some wath organized
    but not final format
    */
    void processRequest(HttpServletRequest request, HttpServletResponse response){
        connId=request.getSession().getId()+".html";
        int nvar = Integer.parseInt(request.getParameter("nxsend"));
        int nfunc = Integer.parseInt(request.getParameter("nfsend"));
        String orien = request.getParameter("selectsend");
        double respArray[][]= new double [nfunc][nvar+1];
        int resptype[] = new int [nfunc];
        double funcobjec[] = new double [nvar];
        for (int nf=0;nf<nfunc;nf++){
               for (int nv=0;nv<nvar;nv++){
                   if (request.getParameter("f"+(nf+1)+"x"+(nv+1)).isEmpty())
                        respArray[nf][nv]= 0.0;
                   else
                       respArray[nf][nv]= Double.parseDouble(request.getParameter("f"+(nf+1)+"x"+(nv+1)));
                   
            }
               if (request.getParameter("f"+(nf+1)+"r").isEmpty())
                    respArray[nf][nvar]= 0.0;
               else
                   respArray[nf][nvar]= Double.parseDouble(request.getParameter("f"+(nf+1)+"r"));
               resptype[nf]=Integer.parseInt(request.getParameter("resultadoorient"+(nf+1)));
        }
        
        for (int nv=0;nv<nvar;nv++){
            if (request.getParameter("mainx"+(nv+1)).isEmpty())
                  funcobjec[nv]= 0.0;
            else
                funcobjec[nv]= Double.parseDouble(request.getParameter("mainx"+(nv+1)));
        }
        Parms.local=new File(request.getSession().getServletContext().getRealPath("/") + connId); /*sets the file name and path to the html that cotains the final result*/
        if (request.getParameter("rbb").contains("N"))
                  Parms.rumBB=false;
        if (!request.getParameter("nthreads").isEmpty())
                  Parms.threads=Integer.parseInt("0"+request.getParameter("nthreads"));
        if (!request.getParameter("maxnos").isEmpty())
                    Parms.MaxNos=Integer.parseInt("0"+request.getParameter("maxnos"));
        HTMLresponses.w8tingPage(response, connId);/*fancy stuff*/
        TControler.time=System.currentTimeMillis();
        initsimplex simples = new initsimplex(nvar,nfunc,orien,respArray,resptype,funcobjec);/*starts the simples from the initsimplex class*/
        
    }
}
