import java.util.Objects;

public class FlatPercentProvisionTransaction extends Transaction{
    private final int percent;

    public FlatPercentProvisionTransaction(long from_id, long to_id, String s, int s1) {
        super(from_id,to_id,s,String.valueOf(s1));
        percent=5;
    }

    public int getPercent() {
        return percent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FlatPercentProvisionTransaction that = (FlatPercentProvisionTransaction) o;
        return percent == that.percent;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), percent);
    }
}
