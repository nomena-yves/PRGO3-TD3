package hei.group.exercicetd3;

import java.sql.SQLException;
import java.util.ArrayList;

import static hei.group.exercicetd3.CategoryEnum.*;
import static hei.group.exercicetd3.DishTypeEnum.MAIN;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection db = new DatabaseConnection();
        DataRetreiver d = new DataRetreiver(db);
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        Dish steakPorc=new Dish(10,"porc",MAIN,ingredients,7000.00);

        Ingredient viande_Hacher= new Ingredient(6,"viande_hache",200.00,ANIMAL,steakPorc);
        ingredients.add(viande_Hacher);
        try{
            //System.out.println(d.findDishById(1));
            //System.out.println(d.findByIngredient(2,3));
            //System.out.println(d.saveDish(steakPorc));
            //System.out.println(d.findDishByIngredientName("Chocolat"));
            System.out.println(d.findIngredientByCretaria("Tomate",VEGETABLE,"Salade fraiche",1,2));
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
