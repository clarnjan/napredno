import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CakeShopApplicationTest1 {

    public static void main(String[] args) {
        CakeShopApplication cakeShopApplication = new CakeShopApplication();

        System.out.println("--- READING FROM INPUT STREAM ---");
        System.out.println(cakeShopApplication.readCakeOrders(System.in));

        System.out.println("--- PRINTING LARGEST ORDER TO OUTPUT STREAM ---");
        cakeShopApplication.printLongestOrder(System.out);
    }
}

class Food{
    String name;
    int price;

    public Food(String name, int price) {
        this.name = name;
        this.price = price;
    }
}
//class Cake extends Food{
//
//    public Cake(String name, int price) {
//        super(name, price);
//    }
//}
//class Pie extends Food{
//
//    public Pie(String name, int price) {
//        super(name, price);
//    }
//}

class Order{
    int id;
    List<Food> foodList;

    public Order(int id) {
        this.id=id;
        foodList=new ArrayList<>();
    }
}

class CakeShopApplication{
    List<Order> orderList;

    public CakeShopApplication() {
        orderList=new ArrayList<>();
    }

    public int readCakeOrders(InputStream in) {
        Scanner scanner=new Scanner(in);
        while(scanner.hasNextLine()){
            String line=scanner.nextLine();
            String []spl=line.split("\\s+");
            Order o=new Order(Integer.parseInt(spl[0]));

            for(int i=1;i<spl.length;i+=2){
                Food f=new Food(spl[i], Integer.parseInt(spl[i+1]));
                o.foodList.add(f);
            }
            orderList.add(o);
        }
        return numItems();
    }

    private int numItems() {
        int x=0;
        for(int i=0;i<orderList.size();i++){
            x+=orderList.get(i).foodList.size();
        }
        return x;
    }

    public void printLongestOrder(PrintStream out) {
        PrintWriter printWriter=new PrintWriter(out);
        Order longestOrder=longestOrder();
        printWriter.println(longestOrder.id+" "+longestOrder.foodList.size());
        printWriter.flush();
    }

    private Order longestOrder() {
        int max=0;
        int maxindex=0;
        for(int i=0;i<orderList.size();i++){
            int x=orderList.get(i).foodList.size();
            if(max<x){
                max=x;
                maxindex=i;
            }

        }
        return orderList.get(maxindex);
    }
}
