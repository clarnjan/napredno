import java.util.Scanner;
import java.util.stream.IntStream;
import java.lang.Math;

public class RomanConverterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        IntStream.range(0, n)
                .forEach(x -> System.out.println(RomanConverter.toRoman(scanner.nextInt())));
        scanner.close();
    }
}


class RomanConverter {
    public static String toRoman(int n) {
        int i = getCount(n);
        String result="";
        while(n!=0){
            i-=1;
            if(i==0)
                result+=getNumber(n/(int)Math.pow(10,i),"I","V","X");
            else if(i==1)
                result+=getNumber(n/(int)Math.pow(10,i),"X","L","C");
            else if(i==2)
                result+=getNumber(n/(int)Math.pow(10,i),"C","D","M");
            else if(i==3)
                result+=getThousands(n/(int)Math.pow(10,i));
            n%=(int)Math.pow(10,i);
        }
        return result;
    }

    private static String getThousands(int n) {
        String result="";
        while(n!=0){
            result+="M";
            n--;
        }
        return result;
    }

    private static String getNumber(int n, String first, String second, String third) {
        switch (n){
            case 0:
                return "";
            case 1:
                return first;
            case 2:
                return first+first;
            case 3:
                return first+first+first;
            case 4:
                return first+second;
            case 5:
                return second;
            case 6:
                return second+first;
            case 7:
                return second+first+first;
            case 8:
                return second+first+first+first;
            case 9:
                return first+third;
        }
        return "";
    }

    private static int getCount(int n) {
        int i = 0;
        while(n !=0){
            i++;
            n /=10;
        }
        return i;
    }

}
