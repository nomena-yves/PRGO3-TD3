package hei.group.exercicetd3;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DataRetreiver {
    Dish findDishById(int id) throws SQLException{
        Dish dish = null;

       DatabaseConnection db = new DatabaseConnection();
       String sql = "select d.id,d.name,d.dish_type,d.price,D.id_ingredient from dish d inner join dishIngredient D on d.id=D.id_dish where d.id = ?";
       PreparedStatement ps = db.getConnection().prepareStatement(sql);
       ps.setInt(1, id);
       ResultSet rs = ps.executeQuery();
       ArrayList<Ingredient> ingredients = new ArrayList<>();
       while (rs.next()) {
           int idIngredient=rs.getInt("id_ingredient");
           if (dish == null) {
               dish =new Dish(
                       rs.getInt("id"),
                       rs.getString("name"),
                       DishTypeEnum.valueOf(rs.getString("dish_type")),
                       ingredients,
                       rs.getDouble("price")
               );
           }
           String sql2="select id,name,price,category from ingredient where id = ?";
           PreparedStatement ps2 = db.getConnection().prepareStatement(sql2);
           ps2.setInt(1, idIngredient);
           ResultSet rs2 = ps2.executeQuery();
           while (rs2.next()) {
               Ingredient ingredient = new Ingredient(
                       rs2.getInt("id"),
                       rs2.getString("name"),
                       rs2.getDouble("price"),
                       CategoryEnum.valueOf(rs2.getString("category")),
                       dish
               );
               ingredients.add(ingredient);
           }
       }
    return dish;
    }
}