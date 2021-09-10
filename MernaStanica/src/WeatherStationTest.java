import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.lang.RuntimeException;

public class WeatherStationTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            Date date = df.parse(line);
            ws.addMeasurment(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        Date from = df.parse(line);
        line = scanner.nextLine();
        Date to = df.parse(line);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}

class Measurment implements Comparable<Measurment> {
    private float temperature;
    private float wind;
    private float humidity;
    private float visibility;
    private Date date;

    public Measurment(float temperature, float wind, float humidity, float visibility, Date date) {
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
        this.visibility = visibility;
        this.date = date;
    }

    @Override
    public int compareTo(Measurment o) {
        if(date.before(o.date))
            return -1;
        if(date.after(o.date))
            return 1;
        return 0;
    }

    public Date getDate() {
        return date;
    }

    public float getTemperature() {
        return temperature;
    }

    @Override
    public String toString() {
        return String.format("%.1f %.1f km/h %.1f%% %.1f km ",temperature ,wind, humidity ,visibility )+date;
    }
}

class WeatherStation{
    int days;
    List<Measurment> measurmentList;

    public WeatherStation(int n) {
        days=n;
        measurmentList=new ArrayList<>();
    }

    public void addMeasurment(float temp, float wind, float hum, float vis, Date date) {
        for(Measurment m : measurmentList){
            if(Math.abs(m.getDate().getTime()-date.getTime())<150000)
                return;
        }
        removeOld(date);
        Measurment measurment=new Measurment(temp,wind,hum,vis,date);
        measurmentList.add(measurment);
    }

    private void removeOld(Date date) {
        List<Measurment> nova=new ArrayList<>();
        for(Measurment m : measurmentList){
            long x=m.getDate().getTime()+days*1000*60*60*24;
            Date d=new Date(x);
            if(!d.before(date))
                nova.add(m);
        }
        measurmentList=nova;
    }

    public int total() {
        return measurmentList.size();
    }

    public void status(Date from, Date to) {
        List<Measurment> lista= measurmentList.stream().filter(s->!s.getDate().after(to) && !s.getDate().before(from))
                .sorted().collect(Collectors.toList());
        if(lista.isEmpty())
            throw new RuntimeException();
        lista.stream().forEach(System.out::println);
        double avg= lista.stream().mapToDouble(Measurment::getTemperature).average().getAsDouble();
        System.out.println(String.format("Average temperature: %.2f",avg));
    }
}