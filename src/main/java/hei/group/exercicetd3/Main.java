package hei.group.exercicetd3;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        DataRetreiver d = new DataRetreiver();
        try{
            System.out.println(d.findDishById(1));
        }catch(SQLException e){
            System.out.println(e.getErrorCode());
        }
    }
}
