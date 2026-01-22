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

    void attachement(Connection conn, Integer dishId, List<DishIngredient> dishingredients) throws SQLException {
        if(dishingredients==null||dishingredients.isEmpty()){
            return;
        }
        String sqlattInsert="insert into dishIngredient(id,dish_id,id_ingredient,quantity_required,uniti) values(?,?,?,?,?)";
        String sqlattUpdate="Update dishIngredient set id_dish=?,id_ingredient=?,quantity_required=?,uniti=? where id=? ";
        String selectdish="select id, id_dish from dishIngredient where id_dish=?";
        PreparedStatement statemenSelect = conn.prepareStatement(selectdish);
        statemenSelect.setInt(1, dishId);
        ResultSet rs = statemenSelect.executeQuery();
        if(rs.next()) {
            for (DishIngredient dishIngredient:dishingredients){
                int idDishIngredient=rs.getInt("id");
                PreparedStatement statementUpdate = conn.prepareStatement(sqlattUpdate);
                statementUpdate.setInt(1, idDishIngredient);
                statementUpdate.setInt(2, dishId);
                statementUpdate.setObject(3,dishIngredient.getQuantity_requierd());
                statementUpdate.setObject(4,dishIngredient.getUnit());
                statemenSelect.executeUpdate();
            }
        }else{
            for (DishIngredient dishIngredient:dishingredients){
                PreparedStatement preparedStatement = conn.prepareStatement(sqlattInsert);
                preparedStatement.setInt(1,dishIngredient.getId());
                preparedStatement.setInt(2,dishId);
                preparedStatement.setInt(3,dishIngredient.getIngredient().getId());
                preparedStatement.setObject(4,dishIngredient.getQuantity_requierd());
                preparedStatement.setObject(5,dishIngredient.getUnit());
                preparedStatement.executeUpdate();
            }
        }

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
                        statement3.setDouble(3,dish.getPrice());
                        statement3.setInt(4,dish.getId());
                        statement3.executeUpdate();

                        for (Ingredient ingredient : dish.getIngredients()) {
                           sqlUpdate = "update ingredient set name=?,price=?,category=? where id = ?";
                            PreparedStatement statement4 = conn.prepareStatement(sqlUpdate);
                            statement4.setString(1, ingredient.getName());
                            statement4.setDouble(2,ingredient.getPrice());
                            statement4.setObject(3,ingredient.getCategory().toString(),java.sql.Types.OTHER);
                            statement4.setInt(4,ingredient.getId());
                            int rs4=statement4.executeUpdate();
                        }
                       System.out.println("Dish updated");
                   }else {
                       for (Ingredient ingredient : dish.getIngredients()) {
                           String sqlInsertIngredient="Insert into ingredient(id,name,price,category) values(?,?,?,?)";
                           PreparedStatement statementInsert = conn.prepareStatement(sqlInsertIngredient);
                           statementInsert.setInt(1, ingredient.getId());
                           statementInsert.setString(2, ingredient.getName());
                           statementInsert.setDouble(3, ingredient.getPrice());
                           statementInsert.setObject(4, ingredient.getCategory().toString(), java.sql.Types.OTHER);
                           statementInsert.executeUpdate();

                       }
                         String sqlInsert = "Insert into dish(id,name,dish_type,price) values(?,?,?,?)";
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
    List<Ingredient> findIngredientByCretaria(String ingredientName,CategoryEnum category,String NameDish,int page,int size) throws SQLException {
        Connection conn = db.getConnection();
        int offset = (page - 1) * size;
        String sql="select i.id,i.name,i.category,i.price from ingredient i inner join dishIngredient d on i.id=d.id_ingredient where i.name like ? and d.id_ingredient =? and category=?::ingredient_type limit ? offset ?";
        String sql2="select di.id_ingredient from dish d inner join dishIngredient di on di.id_dish=d.id where d.id=?";
        String sqlDish="select id,name,dish_type,price from dish where name=?";
       ArrayList<Ingredient> ingredients = new ArrayList<>();
       Ingredient ingredient = null;
       Dish dish=null;
        PreparedStatement ps = conn.prepareStatement(sqlDish);
        ps.setString(1, ingredientName);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
           int idDish= rs.getInt("id");
           dish=new Dish(
                   rs.getInt("id"),
                   rs.getString("name"),
                   DishTypeEnum.valueOf(rs.getString("dish_type")),
                   null,
                   rs.getDouble("price")
           );
           PreparedStatement ps2 = conn.prepareStatement(sql2);
           ps.setInt(1, idDish);
           ResultSet rs2 = ps2.executeQuery();
           while (rs2.next()) {
              int idIngredient= rs2.getInt("id_ingredient");
               PreparedStatement ps3 = conn.prepareStatement(sql);
               ps3.setInt(1, idIngredient);
               ResultSet rs3 = ps3.executeQuery();
               while (rs3.next()) {
                   ingredient =  new Ingredient(
                           rs3.getInt("id"),
                           rs3.getString("name"),
                           rs3.getDouble("price"),
                           CategoryEnum.valueOf(rs3.getString("category")),
                           dish

                   );

               }
               ingredients.add(ingredient);
           }

        }

        return ingredients;
    }
}