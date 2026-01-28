package hei.group.exercicetd3;

import java.util.List;
import java.util.Objects;

public class Dish {
    private int id;
    private String name;
    private DishTypeEnum dishType;
    private List<DishIngredient> dishIngredients;
    private Double price;

    public Dish(int id, String name, DishTypeEnum dishType, List<DishIngredient> dishingredients, Double price) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
        this.dishIngredients = dishIngredients;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DishTypeEnum getDishType() {
        return dishType;
    }

    public void setDishType(DishTypeEnum dishType) {
        this.dishType = dishType;
    }

    public List<DishIngredient> getDishIngredients() {
        return dishIngredients;
    }

    public void setIngredients(List<DishIngredient> ingredients) {
        this.dishIngredients = dishIngredients;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dishType=" + dishType +
                ", ingredients=" + dishIngredients +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        return id == dish.id && Objects.equals(name, dish.name) && dishType == dish.dishType && Objects.equals(dishIngredients, dish.dishIngredients) && Objects.equals(price, dish.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dishType, dishIngredients, price);
    }

    public Double getDishCost() {
        Double dishCost = 0.0;
        if (dishIngredients != null) {
            for (DishIngredient dishIngredient : dishIngredients) {
                dishCost+= dishIngredient.getIngredient().getPrice()*dishIngredient.getQuantity_requierd().doubleValue();
            }
        }else{
            return null;
        }
        return dishCost;
    }
}
