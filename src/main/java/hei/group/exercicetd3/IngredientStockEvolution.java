package hei.group.exercicetd3;

import java.sql.Timestamp;
import java.util.Objects;

public class IngredientStockEvolution {
    int id;
    String name_ingredient;
    Timestamp date;
    Double periodicity;

    public IngredientStockEvolution(int id, String name_ingredient, Timestamp date, Double periodicity) {
        this.id = id;
        this.name_ingredient = name_ingredient;
        this.date = date;
        this.periodicity = periodicity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName_ingredient() {
        return name_ingredient;
    }

    public void setName_ingredient(String name_ingredient) {
        this.name_ingredient = name_ingredient;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Double getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(Double periodicity) {
        this.periodicity = periodicity;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        IngredientStockEvolution that = (IngredientStockEvolution) o;
        return id == that.id && Objects.equals(name_ingredient, that.name_ingredient) && Objects.equals(date, that.date) && Objects.equals(periodicity, that.periodicity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name_ingredient, date, periodicity);
    }

    @Override
    public String toString() {
        return "IngredientStockEvolution{" +
                "id=" + id +
                ", name_ingredient='" + name_ingredient + '\'' +
                ", date=" + date +
                ", periodicity=" + periodicity +
                '}';
    }
}
