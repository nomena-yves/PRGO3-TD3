package hei.group.exercicetd3;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection db = new DatabaseConnection();
        DataRetreiver d = new DataRetreiver(db);
        try{
            //System.out.println(d.findDishById(1));
            System.out.println(d.findByIngredient(2,3));
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
