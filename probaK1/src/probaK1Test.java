import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class probaK1Test {
    public static void main(String[] args) {
        List<Ocena> oceni=new ArrayList<>();
        oceni.add(new Ocena("10"));
        oceni.add(new Ocena("5"));
        oceni.add(new Ocena("8"));
        oceni.add(new Ocena("9"));
        oceni.add(new Ocena("6"));
        oceni.add(new Ocena(10));
        oceni.add(new Ocena(6));
        oceni.add(new Ocena(7));
        oceni.add(new Ocena(6));
        oceni.add(new Ocena(9));
        oceni.stream().sorted(Comparator.naturalOrder()).forEach(System.out::println);
//        oceni.stream().sorted(Comparator.comparing(Ocena::getDate)).forEach(System.out::println);
    }
}

class InvalidGradeException extends RuntimeException{
    public InvalidGradeException(int x) {
        super(String.format("%d is not a valid grade",x));
    }
}

class Ocena<T> implements Comparable<Ocena> {
    T t;
    Date date;
    public static DateFormat dateFormat;


    public Date getDate() {
        return date;
    }

    public Ocena(T t) {
        int x=Integer.parseInt(String.valueOf(t));
        if(x<5 || x>10)
            throw new InvalidGradeException(x);
        this.t = t;
        dateFormat=new SimpleDateFormat("E dd.MM.yyyy hh:mm:ss:SS z");
        dateFormat.setTimeZone(TimeZone.getTimeZone("CET"));
        this.date=new Date();

    }

    @Override
    public int compareTo(Ocena o) {
        int x= Integer.parseInt(String.valueOf(t));
        int y= Integer.parseInt(String.valueOf(o.t));
        return Integer.compare(x,y);
    }

    @Override
    public String toString() {
        return String.format("grade %s on %s",String.valueOf(t),dateFormat.format(date));
    }
}