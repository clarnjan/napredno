import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class F1Test {

    public static void main(String[] args) {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}
class Driver{
    private String driverName;
    private Date lap1;
    private Date lap2;
    private Date lap3;
    private Date bestTime;

    public Driver(String driverName, String lap1, String lap2, String lap3) {
        this.driverName = driverName;
        this.lap1=strToTime(lap1);
        this.lap2=strToTime(lap2);
        this.lap3=strToTime(lap3);
        this.bestTime=calculateBestTime(this.lap1,this.lap2,this.lap3);
    }

    private Date calculateBestTime(Date lap1, Date lap2, Date lap3) {
        if(lap1.before(lap2) && lap1.before(lap3))
            return lap1;
        if(lap2.before(lap1) && lap2.before(lap3))
            return lap2;
        return lap3;
    }

    private Date strToTime(String lap) {
        String []spl=lap.split(":");
        long min=Long.parseLong(spl[0]);
        long s=min*60+Long.parseLong(spl[1]);
        long ms=s*1000+Long.parseLong(spl[2]);
        return new Date(ms);
    }

    public String getDriverName() {
        return driverName;
    }

    public Date getBestTime() {
        return bestTime;
    }
}

class F1Race {
    private List<Driver> driverList;

    public F1Race() {
        this.driverList = new ArrayList<>();
    }

    public void readResults(InputStream in) {
        Scanner scanner=new Scanner(in);

        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String []spl=line.split("\\s+");
            Driver driver=new Driver(spl[0],spl[1],spl[2],spl[3]);
            this.driverList.add(driver);
        }
    }

    public void printSorted(PrintStream out) {
        PrintWriter pw=new PrintWriter(out);
        SimpleDateFormat ft =new SimpleDateFormat ("m:ss:SSS");
        List<Driver>sortedList=driverList.stream().sorted(Comparator.comparing(Driver::getBestTime)).collect(Collectors.toList());
        int i=1;
        for(Driver d: sortedList){
            System.out.println(String.format("%d. %-10s%10s",i,d.getDriverName(),ft.format(d.getBestTime())));
            i++;
        }
        pw.flush();
    }

}