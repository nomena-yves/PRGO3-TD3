package hei.group.exercicetd3;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Order {
    private int id;
    private String references;
    private List<DishOrder> dishOrders;
    private Table table;
    private Instant creationDatetime;

    public Order(int id, String references, List<DishOrder> dishOrders, Table table, Instant creationDatetime) {
        this.id = id;
        this.references = references;
        this.dishOrders = dishOrders;
        this.table = table;
        this.creationDatetime = creationDatetime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }

    public List<DishOrder> getDishOrders() {
        return dishOrders;
    }

    public void setDishOrders(List<DishOrder> dishOrders) {
        this.dishOrders = dishOrders;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Instant getCreationDatetime() {
        return creationDatetime;
    }

    public void setCreationDatetime(Instant creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && Objects.equals(references, order.references) && Objects.equals(dishOrders, order.dishOrders) && Objects.equals(table, order.table) && Objects.equals(creationDatetime, order.creationDatetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, references, dishOrders, table, creationDatetime);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", references='" + references + '\'' +
                ", dishOrders=" + dishOrders +
                ", table=" + table +
                ", creationDatetime=" + creationDatetime +
                '}';
    }
}
