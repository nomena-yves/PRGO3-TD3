package hei.group.exercicetd3;

import java.sql.SQLException;

import static hei.group.exercicetd3.DishTypeEnum.MAIN;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection db = new DatabaseConnection();
        DataRetreiver d = new DataRetreiver(db);
        Dish steakBoeuf=new Dish(8,"steak",MAIN,null,2000.00);
        try{
            //System.out.println(d.findDishById(1));
            System.out.println(d.findByIngredient(2,3));
            System.out.println(d.saveDish(steakBoeuf));
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
