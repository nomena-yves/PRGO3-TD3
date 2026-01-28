package hei.group.exercicetd3;

import java.time.Instant;
import java.util.Objects;

public class StockMouvement {
    private int id;
    private StockValue value;
    private MouvementTypeEnum type;
    private Instant createDateTime;

    public StockMouvement(int id, StockValue value, MouvementTypeEnum type, Instant createDateTime) {
        this.id = id;
        this.value = value;
        this.type = type;
        this.createDateTime = createDateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StockValue getValue() {
        return value;
    }

    public void setValue(StockValue value) {
        this.value = value;
    }

    public MouvementTypeEnum getType() {
        return type;
    }

    public void setType(MouvementTypeEnum type) {
        this.type = type;
    }

    public Instant getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Instant createDateTime) {
        this.createDateTime = createDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StockMouvement that = (StockMouvement) o;
        return id == that.id && Objects.equals(value, that.value) && type == that.type && Objects.equals(createDateTime, that.createDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, type, createDateTime);
    }

    @Override
    public String toString() {
        return "StockMouvement{" +
                "id=" + id +
                ", value=" + value +
                ", type=" + type +
                ", createDateTime=" + createDateTime +
                '}';
    }
}
