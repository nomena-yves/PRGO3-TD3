package hei.group.exercicetd3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataRetreiver {
    private final DatabaseConnection db ;

    public DataRetreiver(DatabaseConnection db) {
        this.db = db;
    }

    Dish findDishById(int id) throws SQLException{
        Dish dish = null;

        Connection conn = db.getConnection();
        String sql = "select d.id,d.name,d.dish_type,d.price,di.id_ingredient from dish d inner join dishIngredient di on d.id=di.id_dish where d.id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
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
            PreparedStatement ps2 = conn.prepareStatement(sql2);
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

    List<Ingredient> findByIngredient(int page, int size) throws SQLException {
        int offset = (page - 1) * size;
        Connection conn = db.getConnection();
        String sql="select i.id,i.name,i.price,i.category,di.id_dish from ingredient i inner join dishIngredient di on i.id=di.id_ingredient limit ? offset ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, offset);
        ps.setInt(2, size);
        ResultSet rs = ps.executeQuery();
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        Dish dish = null;
        while (rs.next()) {
            int idIngredient=rs.getInt("id_dish");
             String sql2="select id,name,dish_type,price from dish where id = ?";
             PreparedStatement ps2 = conn.prepareStatement(sql2);
             ps2.setInt(1, idIngredient);
             ResultSet rs2 = ps2.executeQuery();
          while (rs2.next()) {
              dish =new Dish(
                      rs2.getInt("id"),
                        rs2.getString("name"),
                        DishTypeEnum.valueOf(rs2.getString("dish_type")),
                        new ArrayList<>(),
                        rs2.getDouble("price")
              );
            }
          Ingredient ingredient = new Ingredient(
                  rs.getInt("id"),
                  rs.getString("name"),
                  rs.getDouble("price"),
                   CategoryEnum.valueOf(rs.getString("category")),
                       dish
          );
          ingredients.add(ingredient);


        }
        return ingredients;
    }

    public Dish saveDish(Dish dish) throws SQLException {
        Connection conn = db.getConnection();
        conn.setAutoCommit(false);
        try{
               String sqlSelect = "Select id from dish where name = ?";
               PreparedStatement statementSelect = conn.prepareStatement(sqlSelect);
                   statementSelect.setString(1, dish.getName());
                   ResultSet rs = statementSelect.executeQuery();
                   if (rs.next()) {
                       dish.setId(rs.getInt("id"));
                        String sqlUpdate = "Update dish set name=?,dish_type=?,price=? WHERE id = ?";
                        PreparedStatement statement3 = conn.prepareStatement(sqlUpdate);
                        statement3.setString(1, dish.getName());
                        statement3.setObject(2, dish.getDishType().toString(), java.sql.Types.OTHER);
                        statement3.setInt(3, dish.getId());
                        statement3.setObject(4,dish.getPrice());
                        statement3.executeUpdate();
                       System.out.println("Dish updated");
                   }else {
                        int ingredientId= rs.getInt("id");
                         String sqlInsert = "Insert into dish(id,name,dish_type,price=?) values(?,?,?)";
                         PreparedStatement statementInsert = conn.prepareStatement(sqlInsert);
                         statementInsert.setInt(1, dish.getId());
                         statementInsert.setString(2, dish.getName());
                           statementInsert.setObject(3, dish.getDishType().toString(), java.sql.Types.OTHER);
                           statementInsert.setDouble(4, dish.getPrice());
                           statementInsert.executeUpdate();
                       System.out.println("Dish inserted");
                   }
                   conn.commit();
        }catch(SQLException e){
            conn.rollback();
            System.out.println(e.getMessage());
        }
        return null;
    }
    List<Dish> findDishByIngredientName(String IngredientName) throws SQLException {
        List<Dish> dishes = new ArrayList<>();
            String sqlIngredient="select id from ingredient where name = ?";
            Connection conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(sqlIngredient);
            ps.setString(1, IngredientName);
            ResultSet rs = ps.executeQuery();
            Dish dish = null;

            if (rs.next()) {
                int idIngredient=rs.getInt("id");
                String sql2="select di.id_dish,d.id,d.name,d.price,d.dish_type from dish d inner join dishIngredient di on d.id = di.id_ingredient where d.id = ?";
                PreparedStatement ps2 = conn.prepareStatement(sql2);
                ps2.setInt(1, idIngredient);
                ResultSet rs2 = ps2.executeQuery();
                while (rs2.next()) {
                    dish =new Dish(
                            rs2.getInt("id"),
                            rs2.getString("name"),
                            DishTypeEnum.valueOf(rs2.getString("dish_type")),
                            new ArrayList<>(),
                            rs2.getDouble("price")
                    );
                    dishes.add(dish);
                }
                return dishes;
            }
        return dishes;
    }
}