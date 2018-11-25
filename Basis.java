import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedWriter;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


//fetcher class
class Fetcher {

    public List<String> getFetchedLinks(String s) throws IOException {
        //Document doc = Jsoup.connect("https://www.jugantor.com/archive/"+dateUrl).get();
        //https://basis.org.bd/index.php/site/memberList/
        Document doc = Jsoup.connect(s).get();
        Elements links = doc.select("a[href]");

        List<String> linkList = new ArrayList<>();
        for (Element link : links) {
            if(linkList.contains(link.attr("abs:href"))==false) {
                linkList.add(link.attr("abs:href"));
            }
            
           // System.out.println(link.attr("abs:href"));
        }

        return linkList;
    }

    public String getTextData(String link) throws IOException {
        Document doc = Jsoup.connect(link).get();
        //Elements texts = doc.select("p");
        Elements texts = doc.select("td");

        StringBuilder sb = new StringBuilder();
        for (Element text : texts){
            sb.append(text.text()+"\n");
            //System.out.println(text.text()+"\n");
        }

        return sb.toString();
    }
}

class FileWriting{
    //jugantor_data.txt
    String fileName;
    BufferedWriter bw;
    FileWriter fw;

    public FileWriting(String s){
        fileName=s;
        try{
            this.fw = new FileWriter(s,true); //to append
            this.bw = new BufferedWriter(fw);
        }
        catch(Exception e){
            System.out.println("Writer Error");
        }
        
    }

    String extractData(String s){
        String d="";
        String[] arrOfStr=s.split("\\r?\\n");
        
        for(int i=0;i<arrOfStr.length;i++){
            //System.out.println(arrOfStr[i]);
            if(arrOfStr[i].compareTo("Company Name") == 0){
                String line = arrOfStr[i]+":"+arrOfStr[i+1]; 
                d=d+line+"\n";
                i++;
            }
            else if(arrOfStr[i].compareTo("Address") == 0){
                String line = arrOfStr[i]+":"+arrOfStr[i+1]; 
                d=d+line+"\n";
                i++;
            }
            else if(arrOfStr[i].compareTo("Contact No.") == 0){
                String line = arrOfStr[i]+":"+arrOfStr[i+1]; 
                d=d+line+"\n";
                i++;
            }
            else if(arrOfStr[i].compareTo("E-mail") == 0){
                String line = arrOfStr[i]+":"+arrOfStr[i+1]; 
                d=d+line+"\n";
                i++;
            }
            else if(arrOfStr[i].compareTo("Company website") == 0){
                String line = arrOfStr[i]+":"+arrOfStr[i+1]; 
                d=d+line+"\n";
                i++;
            }
            else if(arrOfStr[i].compareTo("Contact Person") == 0){
                String line = arrOfStr[i]; 
                d=d+line+"\n";
            }
            else if(arrOfStr[i].compareTo("Name") == 0){
                String line = arrOfStr[i]+":"+arrOfStr[i+1]; 
                d=d+line+"\n";
                i++;
            }
            else if(arrOfStr[i].compareTo("Designation") == 0){
                String line = arrOfStr[i]+":"+arrOfStr[i+1]; 
                d=d+line+"\n";
                i++;
            }
            else if(arrOfStr[i].compareTo("Mobile") == 0){
                String line = arrOfStr[i]+":"+arrOfStr[i+1]; 
                d=d+line+"\n";
                i++;
            }
        }
        return d;
    }
    void closeFile(){
        try{
            if(this.fw != null){
                fw.close();
            }
            if(this.bw != null){
                bw.close();
            }
        }
        catch(Exception e){
            System.out.println("file connection didn't close properly");
        }
    }

}


//main class 
public class Basis {

    //int monthDays[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    boolean validityCheck(String url){
        String[] arrOfStr = url.split("/", 100); 
        int length = arrOfStr.length;
        if(length>1){
            String s = arrOfStr[length-2];
            if(s.compareTo("memberProfile")==0){
                return true;
            }
            return false;
        }
        else{
            return false;
        }
    }

    public static void main(String gs[]) throws IOException {


        Basis bs=new Basis();
        FileWriting  fileWriting = new FileWriting("company details.txt");
        Fetcher obj = new Fetcher();

        

        for(int i=0;i<1161;i+=20){
            String base="https://basis.org.bd/index.php/site/memberList/";
            String temp;
            if(i!=0){
                base = base+Integer.toString(i);
            }
            List<String> urls = obj.getFetchedLinks(base);
            for(String url : urls){
                try{
                    boolean ok=bs.validityCheck(url);
                    if(ok==true){
                       String s=obj.getTextData(url);
                       s=fileWriting.extractData(s);
                       System.out.println(s);
                       fileWriting.bw.write(s);
                       fileWriting.bw.flush();
                       fileWriting.bw.write("\n");
                       fileWriting.bw.flush();
                    }
                }
                catch(Exception e){
                    System.out.println("no");
                }
            }
        }
            
        }
}