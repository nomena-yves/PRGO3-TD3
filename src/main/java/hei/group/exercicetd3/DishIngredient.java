package hei.group.exercicetd3;

import java.math.BigDecimal;
import java.util.Objects;

public class DishIngredient {
    private Integer id;
    private  int IdDish;
    private int IdIngredient;
    private BigDecimal quantity_requierd;

    public DishIngredient(Integer id, int idDish, int idIngredient, BigDecimal quantity_requierd) {
        this.id = id;
        IdDish = idDish;
        IdIngredient = idIngredient;
        this.quantity_requierd = quantity_requierd;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getIdDish() {
        return IdDish;
    }

    public void setIdDish(int idDish) {
        IdDish = idDish;
    }

    public int getIdIngredient() {
        return IdIngredient;
    }

    public void setIdIngredient(int idIngredient) {
        IdIngredient = idIngredient;
    }

    public BigDecimal getQuantity_requierd() {
        return quantity_requierd;
    }

    public void setQuantity_requierd(BigDecimal quantity_requierd) {
        this.quantity_requierd = quantity_requierd;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DishIngredient that = (DishIngredient) o;
        return IdDish == that.IdDish && IdIngredient == that.IdIngredient && Objects.equals(id, that.id) && Objects.equals(quantity_requierd, that.quantity_requierd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, IdDish, IdIngredient, quantity_requierd);
    }

    @Override
    public String toString() {
        return "DishIngredient{" +
                "id=" + id +
                ", IdDish=" + IdDish +
                ", IdIngredient=" + IdIngredient +
                ", quantity_requierd=" + quantity_requierd +
                '}';
    }
}
