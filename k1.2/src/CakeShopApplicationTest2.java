import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CakeShopApplicationTest2 {

    public static void main(String[] args) {
        CakeShopApplication cakeShopApplication = new CakeShopApplication(4);

        System.out.println("--- READING FROM INPUT STREAM ---");
        cakeShopApplication.readCakeOrders(System.in);

        System.out.println("--- PRINTING TO OUTPUT STREAM ---");
        cakeShopApplication.printAllOrders(System.out);
    }
}

abstract class Food {
    String name;
    int price;

    public Food(String name, int price) {
        this.name = name;
        this.price = price;
    }
}

class Cake extends Food {

    public Cake(String name, int price) {
        super(name, price);
    }
}

class Pie extends Food {

    public Pie(String name, int price) {
        super(name, price);
        this.price += 50;
    }
}

class Order {
    int id;
    List<Food> foodList;

    public Order(int id) {
        this.id = id;
        foodList = new ArrayList<>();
    }

    @Override
    public String toString() {
        //orderId totalOrderItems totalPies totalCakes totalAmount
        return String.format("%d %d %d %d %d", id, foodList.size(), totalPies(), totalCakes(), totalAmount());
    }

    private int totalPies() {
        int i = 0;
        for (Food f : foodList) {
            if (f.getClass() == Pie.class)
                i++;
        }
        return i;
    }

    private int totalCakes() {
        int i = 0;
        for (Food f : foodList) {
            if (f.getClass() == Cake.class)
                i++;
        }
        return i;
    }

    public int totalAmount() {
        int amount = 0;
        for (Food f : foodList) {
            amount += f.price;
        }
        return amount;
    }
}

class CakeShopApplication {
    List<Order> orderList;
    int minimum;

    public CakeShopApplication(int min) {
        minimum = min;
        orderList = new ArrayList<>();
    }

    public int readCakeOrders(InputStream in) {
        Scanner scanner = new Scanner(in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] spl = line.split("\\s+");
            Order o = new Order(Integer.parseInt(spl[0]));

            for (int i = 1; i < spl.length; i += 2) {
                String name = spl[i];
                Food f = null;
                if (name.startsWith("C")) {
                    f = new Cake(spl[i], Integer.parseInt(spl[i + 1]));
                } else
                    f = new Pie(spl[i], Integer.parseInt(spl[i + 1]));
                o.foodList.add(f);
            }
            orderList.add(o);
        }
        int x=0;
        while(x!=-1) {
            x = removeMin();
            try {
                if (x != -1)
                    throw new InvalidOrderException(x);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return numItems();
    }

    private int removeMin() {
        for (Order o : orderList) {
            if (o.foodList.size() < minimum) {
                orderList.remove(o);
                return o.id;
            }
        }
        return -1;
    }

    private int numItems() {
        int x = 0;
        for (int i = 0; i < orderList.size(); i++) {
            x += orderList.get(i).foodList.size();
        }
        return x;
    }

    public void printLongestOrder(PrintStream out) {
        PrintWriter printWriter = new PrintWriter(out);
        Order longestOrder = longestOrder();
        printWriter.println(longestOrder.id + " " + longestOrder.foodList.size());
        printWriter.flush();
    }

    private Order longestOrder() {
        int max = 0;
        int maxindex = 0;
        for (int i = 0; i < orderList.size(); i++) {
            int x = orderList.get(i).foodList.size();
            if (max < x) {
                max = x;
                maxindex = i;
            }

        }
        return orderList.get(maxindex);
    }

    public void printAllOrders(PrintStream out) {
        PrintWriter printWriter = new PrintWriter(out);
        List<Order> sorted = orderList.stream().sorted(Comparator.comparing(Order::totalAmount)).collect(Collectors.toList());
        for (int i = sorted.size() - 1; i >= 0; i--) {
            printWriter.println(sorted.get(i));
        }
        printWriter.flush();
    }
}

class InvalidOrderException extends RuntimeException {
    public InvalidOrderException(int x) {
        super(String.format("The order with id %d has less items than the minimum allowed.", x));
    }
}
