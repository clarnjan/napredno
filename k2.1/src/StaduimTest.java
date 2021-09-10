import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class StaduimTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] sectorNames = new String[n];
        int[] sectorSizes = new int[n];
        String name = scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            sectorNames[i] = parts[0];
            sectorSizes[i] = Integer.parseInt(parts[1]);
        }
        Stadium stadium = new Stadium(name);
        stadium.createSectors(sectorNames, sectorSizes);
        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            try {
                stadium.buyTicket(parts[0], Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));
            } catch (SeatNotAllowedException e) {
                System.out.println("SeatNotAllowedException");
            } catch (SeatTakenException e) {
                System.out.println("SeatTakenException");
            }
        }
        stadium.showSectors();
    }
}

class Sector{
    String name;
    int numberOfSeats;
    Map<Integer,Integer> seats;

    public Sector(String name, int numberOfSeats) {
        this.name = name;
        this.numberOfSeats = numberOfSeats;
        seats = new HashMap<>();
    }
    public int getFreeSeats(){
        return numberOfSeats-seats.size();
    }

    public String getName() {
        return name;
    }
}

class Stadium {
    String name;
    Map<String,Sector> sectorMap;

    public Stadium(String name) {
        this.name = name;
        sectorMap = new HashMap<>();
    }

    public void createSectors(String[] sectorNames, int[] sectorSizes) {
        for(int i=0;i<sectorNames.length;i++){
            sectorMap.put(sectorNames[i],new Sector(sectorNames[i],sectorSizes[i]));
        }
    }

    public void buyTicket(String name, int seat, int type) throws SeatTakenException, SeatNotAllowedException {
        Sector s = sectorMap.get(name);
        if(s.seats.containsKey(seat))
            throw new SeatTakenException();
        if(type==1 && s.seats.containsValue(2) || type==2 && s.seats.containsValue(1))
            throw new SeatNotAllowedException();
        s.seats.put(seat,type);

    }

    public void showSectors() {
        sectorMap.values().stream().sorted(Comparator.comparing(Sector::getFreeSeats).reversed().thenComparing(Sector::getName))
                .forEach(s-> System.out.printf("%s\t%d/%d\t%.1f%%%n",s.name,s.getFreeSeats(),s.numberOfSeats,((double)s.seats.size()/s.numberOfSeats*100)));
    }
}

class SeatTakenException extends Exception{
    SeatTakenException(){
        super("seat already taken");
    }
}

class SeatNotAllowedException extends Exception{
    SeatNotAllowedException(){
        super("seat not allowed");
    }
}