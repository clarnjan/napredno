import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class TimesTest {

    public static void main(String[] args) {
        TimeTable timeTable = new TimeTable();
        try {
            timeTable.readTimes(System.in);
        } catch (UnsupportedFormatException e) {
            System.out.println("UnsupportedFormatException: " + e.getMessage());
        } catch (InvalidTimeException e) {
            System.out.println("InvalidTimeException: " + e.getMessage());
        }
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
    }

}

class UnsupportedFormatException extends RuntimeException{
    public UnsupportedFormatException(String time) {
        super(time);
    }
}
class InvalidTimeException extends RuntimeException{
    public InvalidTimeException(String time) {
        super(time);
    }
}

class Time{
    private int hours;
    private int minutes;

    public Time(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }
}

class TimeTable{
    List<Time> timeList;

    public TimeTable() {
        timeList=new ArrayList<>();
    }

    public void readTimes(InputStream in) {
        Scanner scanner=new Scanner(in);
        while(scanner.hasNextLine()){
            String line=scanner.nextLine();
            String []spl=line.split("\\s+");
            for(String x : spl){
                String []hm=x.split(":|\\.");
                if(hm.length==1){
                    throw new UnsupportedFormatException(x);
                }
                else{
                    int h= Integer.parseInt(hm[0]);
                    int m= Integer.parseInt(hm[1]);
                    if(h<0 || h>23 || m<0 || m>59)
                        throw new InvalidTimeException(x);
                    else
                        timeList.add(new Time(h,m));
                }
            }
        }
    }

    public void writeTimes(PrintStream out, TimeFormat format24) {
        List<Time> sorted = timeList.stream().sorted(Comparator.comparing(Time::getHours).thenComparing(Time::getMinutes))
                .collect(Collectors.toList());
        if(format24==TimeFormat.FORMAT_24){
            for(Time time : sorted){
                String min= String.valueOf(time.getMinutes());
                if(time.getMinutes()<10){
                    min=0+min;
                }
                String hou= String.valueOf(time.getHours());
                if(time.getHours()<10)
                    hou=" "+hou;
                System.out.println(hou+":"+min);
            }
        }
        else{
            for(Time time : sorted){
                String min= String.valueOf(time.getMinutes());
                if(time.getMinutes()<10){
                    min=0+min;
                }
                int h =time.getHours();
                int hou=h;
                if(h==0)
                    hou=h+12;
                if(h<12)
                    min+=" AM";
                if(h>11)
                    min+=" PM";
                if(h>12)
                    hou=h-12;
                String a= String.valueOf(hou);
                if(hou<10)
                    a=" "+a;
                System.out.println(a+":"+min);
            }
        }
    }
}

enum TimeFormat {
    FORMAT_24, FORMAT_AMPM
}