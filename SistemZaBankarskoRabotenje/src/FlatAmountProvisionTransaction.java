import java.util.Objects;

public class FlatAmountProvisionTransaction extends Transaction{
    private final String FlatAmount;
    public FlatAmountProvisionTransaction(long from_id, long to_id, String s, String s1) {
        super(from_id,to_id,s,s1);
        this.FlatAmount=s1;
    }

    public String getFlatAmount() {
        return FlatAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FlatAmountProvisionTransaction that = (FlatAmountProvisionTransaction) o;
        return FlatAmount==that.FlatAmount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), FlatAmount);
    }
}
