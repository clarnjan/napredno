import java.util.Objects;
import java.util.Random;

public class Account {
    private static Random rand=new Random();
    private long id;
    private String name;
    private String balance;

    public Account(String name, String balance) {
        this.name=name;
        this.balance=balance;
        this.id= rand.nextLong();

    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Name:" + name + '\n' +
                "Balance:" + balance + '\n';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id &&
                Objects.equals(name, account.name) &&
                Objects.equals(balance, account.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, balance);
    }
}
