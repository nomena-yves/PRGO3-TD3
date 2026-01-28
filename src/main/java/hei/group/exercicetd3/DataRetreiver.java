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
       List<DishIngredient> dishIngredients = new ArrayList<>();
        List<StockMouvement> stockMouvements = new ArrayList<>();
        Ingredient ingredient = null;
        dishIngredients=new ArrayList<>();
        DishIngredient dishIngredient = null;
        Connection conn = db.getConnection();
        String sql = "select d.id,d.name,d.dish_type,d.price,di.id_ingredient,di.id as id_dishIngredient,di.quantity_required,di.uniti from dish d inner join dishIngredient di on d.id=di.id_dish where d.id = ?";
        String sql2="select i.id,i.name,i.category,i.price from ingredient where i.id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            int id_ingredient=rs.getInt("id_ingredient");
            dish = new Dish(
                    rs.getInt("id"),
                    rs.getString("name"),
                    DishTypeEnum.valueOf(rs.getString("dish_type")),
                    dishIngredients,
                    rs.getDouble("price")
            );
            dishIngredient = new DishIngredient(
                    rs.getInt("id_dishIngredient"),
                    dish,
                    ingredient,
                    rs.getBigDecimal("quantity_required"),
                    UnitType.valueOf(rs.getString("unit"))
            );
                PreparedStatement ps2=conn.prepareStatement(sql2);
                ps2.setInt(1,id_ingredient);
                ResultSet rs2 = ps2.executeQuery();
                if (rs2.next()) {
                    ingredient=new Ingredient(
                            rs2.getInt("id"),
                            rs2.getString("name"),
                            rs2.getDouble("price"),
                            CategoryEnum.valueOf(rs2.getString("category")),
                            dish,
                            stockMouvements
                    );
                }

            dishIngredients.add(dishIngredient);
        }
        dishIngredients.add(dishIngredient);

        return dish;
    }

    List<Ingredient> findByIngredient(int page, int size) throws SQLException {
        List<StockMouvement> stockMouvements = new ArrayList<>();
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
                       dish,
                  stockMouvements
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

                        for (DishIngredient dishingredient : dish.getDishIngredients()) {
                           sqlUpdate = "update ingredient set name=?,price=?,category=? where id = ?";
                            PreparedStatement statement4 = conn.prepareStatement(sqlUpdate);
                            statement4.setString(1, dishingredient.getIngredient().getName());
                            statement4.setDouble(2,dishingredient.getIngredient().getPrice());
                            statement4.setObject(3,dishingredient.getIngredient().getCategory().toString(),java.sql.Types.OTHER);
                            statement4.setInt(4,dishingredient.getIngredient().getId());
                            int rs4=statement4.executeUpdate();
                        }
                       System.out.println("Dish updated");
                   }else {
                       for (DishIngredient dishingredient : dish.getDishIngredients()) {
                           String sqlInsertIngredient="Insert into ingredient(id,name,price,category) values(?,?,?,?)";
                           PreparedStatement statementInsert = conn.prepareStatement(sqlInsertIngredient);
                           statementInsert.setInt(1, dishingredient.getIngredient().getId());
                           statementInsert.setString(2, dishingredient.getIngredient().getName());
                           statementInsert.setDouble(3, dishingredient.getIngredient().getPrice());
                           statementInsert.setObject(4, dishingredient.getIngredient().getCategory().toString(), java.sql.Types.OTHER);
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
    List<DishIngredient> findIngredientByCretaria(String ingredientName,CategoryEnum category,String NameDish,int page,int size) throws SQLException {
        Connection conn = db.getConnection();
        int offset = (page - 1) * size;
        String sql="select i.id,i.name,i.category,i.price from ingredient i inner join dishIngredient d on i.id = d.id_ingredient where i.name = ? and i.category=? and d.id_dish=? limit ? offset ? ";
        String sqlDish="select id,name,dish_type,price from dish where name=?";

       return ;
    }

    public Ingredient saveIngredient(Ingredient ingredient) throws SQLException {
        Connection conn = db.getConnection();
        String sqlSelect="select id from ingredient where name = ?";
        PreparedStatement ps=conn.prepareStatement(sqlSelect);
        ResultSet rs=ps.executeQuery();
        if(rs.next()){
           String sqlUpdate="update ingredient set name = ?,category=?,price=? where id=? ";
           PreparedStatement psUpdate=conn.prepareStatement(sqlUpdate);
           psUpdate.setString(1, ingredient.getName());
           psUpdate.setObject(2,ingredient.getCategory(),java.sql.Types.OTHER);
           psUpdate.setDouble(3,ingredient.getPrice());
           psUpdate.setInt(4,ingredient.getId());
           psUpdate.executeUpdate();
            for (StockMouvement stockMouvement:ingredient.getStockMouvements()) {
                insertStockMouvement(stockMouvement);
            }
        }else {
            String sqlInsert="insert into ingredient (id,name,category,price values(?,?,?,?)";
            PreparedStatement psInsert=conn.prepareStatement(sqlInsert);
            psInsert.setInt(1, ingredient.getId());
            psInsert.setString(2, ingredient.getName());
            psInsert.setObject(3, ingredient.getCategory(), java.sql.Types.OTHER);
            psInsert.setDouble(4, ingredient.getPrice());
            psInsert.executeUpdate();
            for (StockMouvement stockMouvement:ingredient.getStockMouvements()) {
               insertStockMouvement(stockMouvement);
            }
        }

        return ingredient;
    }

    public StockMouvement insertStockMouvement(StockMouvement stockMouvement) throws SQLException {
        Connection conn = db.getConnection();
        String sql="insert into stockmouvement(id,quantity,type,unity,creation_datetime) values(?,?,?,?,?,?) ON CONFLICT (id) DO NOTHING";
        PreparedStatement ps=conn.prepareStatement(sql);
        ps.setInt(1, stockMouvement.getId());
        ps.setObject(2,stockMouvement.getType(),java.sql.Types.OTHER);
        ps.setObject(3,stockMouvement.getValue().getUniti(),java.sql.Types.OTHER);
        ps.setTimestamp(4, java.sql.Timestamp.from(stockMouvement.getCreateDateTime()));
                ps.executeUpdate();
                return stockMouvement;
    }

    public Order saveOrder(Order orderSave) throws SQLException {
        Connection conn = db.getConnection();

    }
}