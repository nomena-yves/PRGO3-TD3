package hei.group.exercicetd3;

import java.time.Instant;
import java.util.Objects;

public class TableOrder {
    private Table table;
    private Instant arriveDatetime;
    private Instant departureDatetime;

    public TableOrder(Table table, Instant arriveDatetime, Instant departureDatetime) {
        this.table = table;
        this.arriveDatetime = arriveDatetime;
        this.departureDatetime = departureDatetime;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Instant getArriveDatetime() {
        return arriveDatetime;
    }

    public void setArriveDatetime(Instant arriveDatetime) {
        this.arriveDatetime = arriveDatetime;
    }

    public Instant getDepartureDatetime() {
        return departureDatetime;
    }

    public void setDepartureDatetime(Instant departureDatetime) {
        this.departureDatetime = departureDatetime;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TableOrder that = (TableOrder) o;
        return Objects.equals(table, that.table) && Objects.equals(arriveDatetime, that.arriveDatetime) && Objects.equals(departureDatetime, that.departureDatetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(table, arriveDatetime, departureDatetime);
    }

    @Override
    public String toString() {
        return "TableOrder{" +
                "table=" + table +
                ", arriveDatetime=" + arriveDatetime +
                ", departureDatetime=" + departureDatetime +
                '}';
    }
}
