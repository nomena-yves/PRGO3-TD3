package hei.group.exercicetd3;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Ingredient {
    private Integer id;
    private String name;
    private Double price;
    private CategoryEnum category;
    private List<StockMouvement> stockMouvements;
    private Dish dish;

    public Ingredient(Integer id, String name, Double price, CategoryEnum category,Dish dish,List<StockMouvement> stockMouvements) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.dish = dish;
        this.stockMouvements = stockMouvements;
    }

    public List<StockMouvement> getStockMouvements() {
        return stockMouvements;
    }

    public void setStockMouvements(List<StockMouvement> stockMouvements) {
        this.stockMouvements = stockMouvements;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public CategoryEnum getCategory() {
        return category;
    }

    public void setCategory(CategoryEnum category) {
        this.category = category;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public String getDishName() {
        if(dish == null) {
            return null;
        }else {
            return dish.getName();
        }
    };

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", category=" + category +
                ", dish=" + dish.getName() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(price, that.price) && category == that.category && Objects.equals(dish, that.dish);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, category, dish);
    }

    public StockValue getStockValueAt(Instant t) {
        Double quantityFinal=0.0;
        if (stockMouvements == null) return null;
        Map<UnitType, List<StockMouvement>> unitSet = stockMouvements.stream()
                .collect(Collectors.groupingBy(stockMovement -> stockMovement.getValue().getUniti()));
        if (unitSet.keySet().size() > 1) {
            throw new RuntimeException("Multiple unit found and not handle for conversion");
        }
        Double quantityIN = stockMouvements.stream().filter(p -> p.getType().equals(MouvementTypeEnum.IN)).mapToDouble(p->p.getValue().getQuantity()).sum();
        Double quantityOUt=stockMouvements.stream().filter(p ->  p.getType().equals(MouvementTypeEnum.OUT)).mapToDouble(p->p.getValue().getQuantity()).sum();
            quantityFinal=quantityIN-quantityOUt;

            StockValue stockValue= new StockValue();
        stockValue.setQuantity(quantityFinal);
        stockValue.setUniti(unitSet.keySet().stream().findFirst().get());
            return stockValue;
    }
    }
