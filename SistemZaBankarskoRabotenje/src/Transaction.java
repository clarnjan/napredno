import java.util.Objects;

public abstract class Transaction {
    private final long fromId;
    private final long toId;
    private final String amount;
    private final String description;

    public Transaction(long fromId, long toId, String amount, String description) {
        this.fromId = fromId;
        this.toId = toId;
        this.amount = amount;
        this.description = description;
    }

    public long getFromId() {
        return fromId;
    }

    public long getToId() {
        return toId;
    }

    public String getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return fromId == that.fromId &&
                toId == that.toId &&
                amount.equals(that.amount) &&
                description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromId, toId, amount, description);
    }

    public String getDescription(){
        return description;
    }
}
