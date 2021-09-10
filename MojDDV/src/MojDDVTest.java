import java.io.BufferedReader;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

    }
}

class Artikl{
    int suma;
    String tip;
    float povratok;

    public Artikl(int suma, String tip) {
        this.suma = suma;
        this.tip = tip;
        if(tip.equals("A"))
            povratok= (float) (suma*0.18*0.15);
        else if(tip.equals("B"))
            povratok= (float) (suma*0.05*0.15);
        else
            povratok=0.0f;
    }
}

class Fiskalna{
    int id;
    List<Artikl> artiklList;
    int suma;
    float povratok;

    public Fiskalna(int id) {
        this.id=id;
        artiklList=new ArrayList<>();
        suma=0;
        povratok=0;
    }

    public void addArtikl(Artikl a){
        artiklList.add(a);
        suma+=a.suma;
        povratok+=a.povratok;
    }

    @Override
    public String toString() {
        return String.format("%d %d %.3f",id,suma,povratok);
    }
}

class AmountNotAllowedException extends RuntimeException{
    public AmountNotAllowedException(int suma) {
        super (String.format("Receipt with amount %s is not allowed to be scanned",suma));
    }
}

class MojDDV{
    List<Fiskalna> fiskalnaList;
    boolean isclean=false;

    public MojDDV() {
        fiskalnaList=new ArrayList<>();
    }

    public void readRecords(InputStream in){
        Scanner sc=new Scanner(in);
        while(sc.hasNextLine()){
            String line = sc.nextLine();
            String []spl=line.split("\\s+");
            int id= Integer.parseInt(spl[0]);
            Fiskalna fiskalna=new Fiskalna(id);
            for(int i=1;i< spl.length;i+=2){
                Artikl artikl=new Artikl(Integer.parseInt(spl[i]),spl[i+1]);
                fiskalna.addArtikl(artikl);
            }
            fiskalnaList.add(fiskalna);
        }
        while(!isclean){
            try {
                check();
            }
            catch (RuntimeException e){
                System.out.println(e.getMessage());
            }
        }
    }

    private void check() throws AmountNotAllowedException {
        for(Fiskalna f : fiskalnaList){
            if (f.suma>30000){
                fiskalnaList.remove(f);
                throw new AmountNotAllowedException(f.suma);
            }
        }
        isclean=true;
    }

    public void printTaxReturns(PrintStream out) {
        PrintWriter pw =new PrintWriter(out);
        for(Fiskalna f : fiskalnaList){
            pw.println(f);
        }
        pw.flush();
    }
}