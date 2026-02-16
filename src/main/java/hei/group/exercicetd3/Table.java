package hei.group.exercicetd3;

import java.util.List;
import java.util.Objects;

public class Table {
  private int id;
  private int number;
  private List<Order> orders;

    public Table(int id, int number, List<Order> orders) {
        this.id = id;
        this.number = number;
        this.orders = orders;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        return id == table.id && number == table.number && Objects.equals(orders, table.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, orders);
    }

    @Override
    public String toString() {
        return "Table{" +
                "id=" + id +
                ", number=" + number +
                ", orders=" + orders +
                '}';
    }
}
