package hei.group.exercicetd3;

import java.util.Objects;

public class Table {
    private int id;
    private int numero;

    public Table(int id, int numero) {
        this.id = id;
        this.numero = numero;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        return id == table.id && numero == table.numero;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numero);
    }

    @Override
    public String toString() {
        return "Table{" +
                "id=" + id +
                ", numero=" + numero +
                '}';
    }
}
