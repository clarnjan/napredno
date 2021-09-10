import java.util.Arrays;

public class Bank {
    private String name;
    private Account[] accounts;

    public Bank(String name, Account[] accounts) {
        this.name = name;
        this.accounts = accounts;
    }

    public Boolean makeTransaction(Transaction t) {
        Account from=null;
        Account to=null;

        for (Account acc : accounts){
            if(acc.getId()==t.getFromId())
                from=acc;
            if(acc.getId()==t.getToId())
                to=acc;
        }
        if(from==null || to==null)
            return false;
        Double amount= Double.valueOf(t.getAmount().split("\\$")[0]);
        Double a=0.0;
        if(t.getClass()==FlatAmountProvisionTransaction.class){
            a= Double.valueOf(t.getDescription().split("\\$")[0]);
        }
        else if(t.getClass()==FlatPercentProvisionTransaction.class){
            a= Double.valueOf(t.getDescription().split("%")[0]);
        }
        Double fromAmount= Double.valueOf(from.getBalance().split("\\$")[0]);
        if(fromAmount<amount+a)
            return false;
        Double toAmount= Double.valueOf(to.getBalance().split("\\$")[0]);
        from.setBalance(fromAmount-amount-a+"0$");
        to.setBalance(fromAmount+amount+"0$");
        return true;
    }
    public String totalTransfers(){
        return null;
    }

    public String totalProvision() {
        return null;
    }

    public Account[] getAccounts() {
        return accounts;
    }

    @Override
    public String toString() {
        String result="Name: "+name+"\n\n";
        for (Account a : accounts)
            result+=a.toString();
        return result;
    }
}
