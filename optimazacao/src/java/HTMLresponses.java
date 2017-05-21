
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;


/*
this class sets the 2 response pages
 */
public class HTMLresponses {
    /*using html code stored on a string, it sends the response to the user over http
    style sheets, html code and a javascript function that from time to time
    checks if the result webpage exists, if it does, redirect to it
    */
    static void w8tingPage( HttpServletResponse response, String connId){
        String Loader ="<!DOCTYPE html>\n" +
"<html>\n" +
"<head>\n<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\"></script>\n" +
"<style>\n" +
"body{\n" +
"	background:#f1f1f1;\n" +
"}\n" +
".loader {\n" +
"  border-radius: 50%;\n" +
"  border: 10px solid #343434;\n" +
"  border-top: 10px solid #f1f1f1;border-bottom: 10px solid #f1f1f1;\n" +
"  width: 260px;\n" +
"  height: 260px;\n" +
"  animation: spin 2s linear infinite;\n" +
"}\n" +
"\n" +
".center{\n" +
"  position: absolute;\n" +
"  width: 260px;\n" +
"  height: 300px;\n" +
"  left:50%;\n" +
"  top:50%;\n" +
"  margin-top:-137px;\n" +
"  margin-left:-137px;\n" +
"  text-align: center;\n" +
"	font-family:Verdana,sans-serif;\n" +
"	font-size:15px;\n" +
"	color:#343434;\n" +
"}\n" +
"@keyframes spin {\n" +
"  0% { transform: rotate(0deg);}\n" +
"  100% { transform: rotate(360deg);}\n" +
"}\n" +
"</style>\n" +
"</head>\n" +
"<body>\n" +
"<img src=\"Artboard2.png\" style=\"height:223px; width:223px; left:50%; margin-left:-111px; position:absolute; top:50%; margin-top:-111px;\">\n" +
"<div class=\"center\">\n" +
"<div class=\"loader\"></div>\n" +
"<strong><br>Carregando<br>...</strong>\n" +
"</div>\n<script>"
                + "setInterval(testpage, 3000)\n"
                + "function testpage(){\n"
                + "$.ajax({\n" +
"    type: 'HEAD',\n" +
"    url: 'http://localhost:8080/optimazacao/"+connId+"',\n" +
"success: function() {\n" +
"        window.location.replace(\"http://localhost:8080/optimazacao/"+connId+"\");\n" +
"},\n" +
"error: function() {\n" +
"        // page does not exist\n" +
"}\n" +
"});}" +
"</script></body>\n" +
"</html>";
        sendresponse(Loader,response);
    }
    /*
    since we can only use one http response and the fancy loadding page is a must
    we creat a html page using the session id as name for it
    this second page has the simplex and B&B final values aka result
    */
    static void resultPage(String[] resultText, File outputFile){
        String result="<html>\n" +
"<style>\n" +
"body{\n" +
"	background:#f1f1f1;\n" +
"}\n" +
".loader {\n" +
"  border: 0px;\n" +
"}\n" +
"\n" +
".center{\n" +
"  position: absolute;\n" +
"  width: 0px;\n" +
"  height: 0px;\n" +
"}\n" +
".container{\n" +
"	color:#343434;\n" +
"	width:540px;\n" +
"	float: left;\n" +
"	min-height:400px;\n" +
"	position: absolute;\n" +
"	background:#fbfafa	;\n" +
"	left:50%;\n" +
"	padding: 30px;\n" +
"	top: 200px;\n" +
"	margin-bottom:120px;\n" +
"	margin-left:-300px;\n" +
"	border: 0px;\n" +
"	border-radius:10px;\n" +
"	box-shadow: 3px 3px 5px #CCC;\n" +
"	font-family:Verdana,sans-serif;\n" +
"	font-size:15px;\n" +
"}\n" +
"</style>\n" +
"<html>\n" +
"<head>\n" +
"</head>\n" +
"<body>\n" +
"<img src=\"Artboard 1.png\" style=\"height:auto; width:150px; left:50%; margin-left:-75px; position:absolute; top:25px;\">\n" +
"<div class=\"container\" id=\"container\">\n";
        for (int i=0;i<resultText.length;i++){
            result+=resultText[i]+"<br>";
        }
        result+="<form action=\"http://localhost:8080/optimazacao/index.html\" style=\"margin-bottom:330px;margin-left:190px;\" method=\"get\">\n" +
"    <input type=\"submit\" value=\"Retornar a pagina inicial\" \n" +
"         name=\"Submit\" id=\"frm1_submit\" />\n" +
"</form></div>\n" +
"</body>\n" +
"\n" +
"</html>";
        /*
        writing the html file, nothing fancy
        */
        FileWriter fout;
        try {
            fout = new FileWriter(outputFile);
            fout.write(result);
            fout.close();
        } catch (IOException ex) {
            Logger.getLogger(HTMLresponses.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    /*this is the function that sends the proper http response to the client
    */
    private static void sendresponse(String resp, HttpServletResponse response){
         try (PrintWriter out = response.getWriter()) {    
            out.println(resp);
        } catch (IOException ex) {
         }
    }
}
