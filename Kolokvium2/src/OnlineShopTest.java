import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

enum COMPARATOR_TYPE {
    NEWEST_FIRST,
    OLDEST_FIRST,
    LOWEST_PRICE_FIRST,
    HIGHEST_PRICE_FIRST,
    MOST_SOLD_FIRST,
    LEAST_SOLD_FIRST
}

class ProductNotFoundException extends Exception {
    ProductNotFoundException(String id) {
        super(String.format("Product with id %s does not exist in the online shop!",id));
    }
}


class Product {
    String category;
    String id;
    String name;
    LocalDateTime createdAt;
    double price;
    int quantitySold;

    public Product(String category, String id, String name, LocalDateTime createdAt, double price, int quantitySold) {
        this.category = category;
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.price = price;
        this.quantitySold = quantitySold;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", price=" + price +
                ", quantitySold=" + quantitySold +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantitySold() {
        return quantitySold;
    }
}


class OnlineShop {

    Map<String,Product> products;
    OnlineShop() {
        products = new HashMap<>();
    }

    void addProduct(String category, String id, String name, LocalDateTime createdAt, double price){
        products.put(id,new Product(category, id, name, createdAt, price, 0));
    }

    double buyProduct(String id, int quantity) throws ProductNotFoundException{
        if(!products.containsKey(id))
            throw new ProductNotFoundException(id);
        Product product = products.get(id);
        product.quantitySold+=quantity;
        return product.price*quantity;
    }

    List<List<Product>> listProducts(String category, COMPARATOR_TYPE comparatorType, int pageSize) {
        List<List<Product>> result = new ArrayList<>();
        HashMap<String, List<Product>> x = (HashMap<String, List<Product>>) products.values().stream().collect(Collectors.groupingBy(Product::getCategory, Collectors.toList()));

        List<Product> list = new ArrayList<>();
        if(x.containsKey(category)){
            list = x.get(category);
        }
        else{
            for(List<Product> lp : x.values()){
                for(Product p : lp){
                    list.add(p);
                }
            }
        }
        if(comparatorType==COMPARATOR_TYPE.NEWEST_FIRST)
            list = list.stream().sorted(Comparator.comparing(Product::getCreatedAt).reversed()).collect(Collectors.toList());
            else if(comparatorType==COMPARATOR_TYPE.OLDEST_FIRST)
            list = list.stream().sorted(Comparator.comparing(Product::getCreatedAt)).collect(Collectors.toList());
        else if(comparatorType==COMPARATOR_TYPE.HIGHEST_PRICE_FIRST)
            list = list.stream().sorted(Comparator.comparing(Product::getPrice).reversed()).collect(Collectors.toList());
        else if(comparatorType==COMPARATOR_TYPE.LOWEST_PRICE_FIRST)
            list = list.stream().sorted(Comparator.comparing(Product::getPrice)).collect(Collectors.toList());
        else if(comparatorType==COMPARATOR_TYPE.MOST_SOLD_FIRST)
            list = list.stream().sorted(Comparator.comparing(Product::getQuantitySold).reversed()).collect(Collectors.toList());
        else if(comparatorType==COMPARATOR_TYPE.LEAST_SOLD_FIRST)
            list = list.stream().sorted(Comparator.comparing(Product::getQuantitySold)).collect(Collectors.toList());


        for(int i=0;i<=list.size()/pageSize;i++){
            List<Product> temp = new ArrayList<>();
            for(int j=0;j<pageSize;j++){
                if(j<list.size() && (i*pageSize+j<list.size()))
                    temp.add(list.get(i*pageSize+j));
            }
            result.add(temp);
        }
        return result;
    }

}

public class OnlineShopTest {

    public static void main(String[] args) {
        OnlineShop onlineShop = new OnlineShop();
        double totalAmount = 0.0;
        Scanner sc = new Scanner(System.in);
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equalsIgnoreCase("addproduct")) {
                String category = parts[1];
                String id = parts[2];
                String name = parts[3];
                LocalDateTime createdAt = LocalDateTime.parse(parts[4]);
                double price = Double.parseDouble(parts[5]);
                onlineShop.addProduct(category, id, name, createdAt, price);
            } else if (parts[0].equalsIgnoreCase("buyproduct")) {
                String id = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                try {
                    totalAmount += onlineShop.buyProduct(id, quantity);
                } catch (ProductNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                String category = parts[1];
                if (category.equalsIgnoreCase("null"))
                    category=null;
                String comparatorString = parts[2];
                int pageSize = Integer.parseInt(parts[3]);
                COMPARATOR_TYPE comparatorType = COMPARATOR_TYPE.valueOf(comparatorString);
                printPages(onlineShop.listProducts(category, comparatorType, pageSize));
            }
        }
        System.out.println("Total revenue of the online shop is: " + totalAmount);

    }

    private static void printPages(List<List<Product>> listProducts) {
        for (int i = 0; i < listProducts.size(); i++) {
            System.out.println("PAGE " + (i + 1));
            listProducts.get(i).forEach(System.out::println);
        }
    }
}

