package hei.group.exercicetd3;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;

import static hei.group.exercicetd3.CategoryEnum.*;
import static hei.group.exercicetd3.DishTypeEnum.MAIN;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection db = new DatabaseConnection();
        DataRetreiver d = new DataRetreiver(db);
        ArrayList<DishIngredient> dishIngredients = new ArrayList<>();
        ArrayList<StockMouvement> stockMouvements = new ArrayList<>();
        Dish steakPorc=new Dish(10,"porc",MAIN,dishIngredients,7000.00);

        Ingredient viandeHacheePorc = new Ingredient(
                11,
                "Viande hachée au porc",
                200.0,
                CategoryEnum.ANIMAL,
                steakPorc,
                stockMouvements
        );

        Ingredient tomate = new Ingredient(
                12,
                "carotte",
                50.0,
                CategoryEnum.VEGETABLE,
                steakPorc,
                stockMouvements

        );

        Ingredient viande_Hacher= new Ingredient(6,"viande_hache",200.00,ANIMAL,steakPorc,stockMouvements);
        DishIngredient di1 = new DishIngredient(
                11,
                steakPorc,
                viande_Hacher,
                new BigDecimal("0.5"),
                UnitType.KG
        );

        DishIngredient di2 = new DishIngredient(
                12,
                steakPorc,
                tomate,
                new BigDecimal("2"),
                UnitType.PIECE
        );
    dishIngredients.add(di1);
    dishIngredients.add(di2);
        try{
            //System.out.println(d.findDishById(1));
            //System.out.println(d.findByIngredient(2,3));
            System.out.println(d.saveDish(steakPorc));
            //System.out.println(d.findDishByIngredientName("Chocolat"));
            //System.out.println(d.findIngredientByCretaria("Tomate",VEGETABLE,"Salade fraiche",1,2));
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
