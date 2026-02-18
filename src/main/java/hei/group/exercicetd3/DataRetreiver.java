package hei.group.exercicetd3;

import org.springframework.data.relational.core.sql.In;

import java.math.BigDecimal;
import java.sql.*;
import java.time.Instant;
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
        String sql2="select i.id,i.name,i.category,i.price from ingredient i where i.id = ?";
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
                    UnitType.valueOf(rs.getString("uniti"))
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

        conn.close();
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
        conn.close();
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
                       String sqlInsert = "Insert into dish(id,name,dish_type,price) values(?,?,?,?)";
                       PreparedStatement statementInsert = conn.prepareStatement(sqlInsert);
                       statementInsert.setInt(1, dish.getId());
                       statementInsert.setString(2, dish.getName());
                       statementInsert.setObject(3, dish.getDishType().toString(), java.sql.Types.OTHER);
                       statementInsert.setDouble(4, dish.getPrice());
                       statementInsert.executeUpdate();

                       System.out.println("Dish inserted");
                       for (DishIngredient dishingredient : dish.getDishIngredients()) {
                           String sqlInsertIngredient="Insert into ingredient(id,name,price,category) values(?,?,?,?)";
                           PreparedStatement statementInsertIngredient = conn.prepareStatement(sqlInsertIngredient);
                           statementInsertIngredient.setInt(1, dishingredient.getIngredient().getId());
                           statementInsertIngredient.setString(2, dishingredient.getIngredient().getName());
                           statementInsertIngredient.setDouble(3, dishingredient.getIngredient().getPrice());
                           statementInsertIngredient.setObject(4, dishingredient.getIngredient().getCategory().toString(), java.sql.Types.OTHER);
                           statementInsertIngredient.executeUpdate();

                       }
                   }
                   conn.commit();
        }catch(SQLException e){
            conn.rollback();
            System.out.println(e.getMessage());
        }
        conn.close();
        return dish;
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
        conn.close();
        return dishes;
    }
    List<Ingredient> findIngredientByCretaria(String ingredientName,CategoryEnum category,String NameDish,int page,int size) throws SQLException {
        Connection conn = db.getConnection();
        Dish dish=null;
        Ingredient ingredient=null;
        List<Ingredient> ingredients=new ArrayList<>();
        List<DishIngredient> dishIngredients=new ArrayList<>();
        List<StockMouvement> stockMouvements=new ArrayList<>();
        int offset = (page - 1) * size;
        String sql="select i.id,i.name,i.category,i.price from ingredient i inner join dishIngredient d on i.id = d.id_ingredient where i.name = ? and i.category=? and d.id_dish=? limit ? offset ? ";
        String sqlDish="select id,name,dish_type,price from dish where name=?";
        PreparedStatement psDish=conn.prepareStatement(sqlDish);
        psDish.setString(1, NameDish);
        ResultSet rs=psDish.executeQuery();
        while (rs.next()){
            int idDish=rs.getInt("id");
            dish =new Dish(
                    rs.getInt("id"),
                    rs.getString("name"),
                    DishTypeEnum.valueOf(rs.getString("dish_type")),
                    dishIngredients,
                    rs.getDouble("price")
            );
            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setString(1, ingredientName);
            ps.setObject(2, category.toString(),java.sql.Types.OTHER);
            ps.setInt(3, idDish);
            ps.setInt(4, size);
            ps.setInt(5,offset);
            ResultSet rs2=ps.executeQuery();
            while (rs2.next()){
                ingredient=new Ingredient(
                        rs2.getInt("id"),
                        rs2.getString("name"),
                        rs2.getDouble("price"),
                        CategoryEnum.valueOf(rs2.getString("category")),
                        dish,
                        stockMouvements
                );
            }
            ingredients.add(ingredient);
        }
        conn.close();
       return ingredients ;
    }

    public Ingredient saveIngredient(Ingredient ingredient) throws SQLException {
        Connection conn = db.getConnection();
        String sqlSelect="select id from ingredient where name = ?";
        PreparedStatement ps=conn.prepareStatement(sqlSelect);
        ps.setString(1, ingredient.getName());
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
            String sqlInsert="insert into ingredient (id,name,category,price) values(?,?,?,?)";
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
        conn.close();
        return ingredient;
    }

    public StockMouvement insertStockMouvement(StockMouvement stockMouvement) throws SQLException {
        Connection conn = db.getConnection();
        String sql="insert into stockmouvement(id,quantitiy,type,unity,creation_datetime) values(?,?,?,?,?) ON CONFLICT (id) DO NOTHING";
        PreparedStatement ps=conn.prepareStatement(sql);
        ps.setInt(1, stockMouvement.getId());
        ps.setBigDecimal(2, BigDecimal.valueOf(stockMouvement.getValue().getQuantity()));
        ps.setObject(3,stockMouvement.getType(),java.sql.Types.OTHER);
        ps.setObject(4,stockMouvement.getValue().getUniti(),java.sql.Types.OTHER);
        ps.setTimestamp(5, java.sql.Timestamp.from(stockMouvement.getCreateDateTime()));
                ps.executeUpdate();
                return stockMouvement;
    }

    public Order findOrderByReference(String reference)throws SQLException{
       Connection conn = db.getConnection();
       Order order=null;
       DishOrder dishOrder=null;
       Dish dish=null;
       Table table=null;
        List<DishIngredient> dishIngredients=new ArrayList<>();
       List<DishOrder> orders=new ArrayList<>();
       String sql="select d.id_dish,d.quantity from ordres o inner join dishorder d on o.id=d.id_order where reference=? ";
       String sqlDish="select id,name,dish_type,price from dish where id=?";
       PreparedStatement psDishorder=conn.prepareStatement(sql);
       psDishorder.setString(1,reference);
return order;
    }

    public Ingredient findIngredientById(Integer id)throws SQLException{
        String sql="select id,name,category, price from ingredient where id=?";
        Connection conn=db.getConnection();
        Ingredient ingredient=null;
        Dish dish=null;
        List<StockMouvement> stockMouvements=new ArrayList<>();
        PreparedStatement ps=conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            ingredient=new Ingredient(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    CategoryEnum.valueOf(rs.getString("category")),
                    dish,
                    stockMouvements
            );
        }
        conn.close();
    return ingredient;
    }
    public Order saveOrder(Order orderSave) throws SQLException {
        Connection conn = db.getConnection();
    String sql="insert into orders (id,reference,create_datetime)values(?,?,?)";
    String sqlDishOrder="insert into dishorder (id,id_order,id_dish,quantity) values(?,?,?,?)";
    PreparedStatement ps=conn.prepareStatement(sql);
    ps.setInt(1, orderSave.getId());
    ps.setString(2,orderSave.getReferences());
    ps.setTimestamp(3,Timestamp.from(orderSave.getCreationDatetime()));
    ps.executeUpdate();
    PreparedStatement ps2=conn.prepareStatement(sqlDishOrder);
    for (DishOrder d : orderSave.getDishOrders()) {
        ps2.setInt(1, d.getId());
        ps2.setInt(2, orderSave.getId());
        ps2.setInt(3, d.getDish().getId());
        ps2.setDouble(4, d.getQuantity());
    }
        System.out.println("order insert");
        conn.close();
        return orderSave;
    }

    public StockValue getStockValueAt(Instant t,Integer ingredientIdentifier) throws SQLException{
        Connection conn = db.getConnection();
       StockValue stockValue=null;
     String sql= """
             SELECT unity,id_ingredient,SUM(CASE
       
              WHEN type = 'OUT' THEN quantitiy * -1
              ELSE quantitiy
              END) AS quantity_total
              FROM stockmouvement
              WHERE creation_datetime >=?
              GROUP BY unity, id_ingredient=?;""";
     PreparedStatement ps=conn.prepareStatement(sql);
     ps.setTimestamp(1, Timestamp.from(t));
     ps.setInt(2, ingredientIdentifier);
     ResultSet rs=ps.executeQuery();
     if(rs.next()){
        stockValue=new StockValue(
                rs.getDouble("quantity_total"),
                UnitType.valueOf(rs.getString("unity"))
        );
     }
        conn.close();
     return stockValue;
    }

    public Double getDishCost(Integer dishId) throws SQLException{
        Connection conn = db.getConnection();
        Double dishCost=null;
        Double ingredientQuantity=1.0;
        int idIngredient;
        String sql= """
                select id_ingredient,quantity_required from dishIngredient where id_dish=?
                """;
        String sqlIngredient="select price from ingredient where id=?";
        PreparedStatement ps=conn.prepareStatement(sql);
        ps.setInt(1, dishId);
        ResultSet rs=ps.executeQuery();
        while(rs.next()){
            idIngredient=rs.getInt("id_ingredient");
            ingredientQuantity= ingredientQuantity*rs.getDouble("quantity_required");

            PreparedStatement psIngredient=conn.prepareStatement(sqlIngredient);
            psIngredient.setInt(1,idIngredient);
            ResultSet rs2=psIngredient.executeQuery();
            if(rs2.next()){
                dishCost+=rs2.getDouble("price");
            }
        }
        conn.close();
        return dishCost*ingredientQuantity;
    }

    public Double getGrossMargint(Integer  dishId)throws SQLException{
        Double marge=0.0;
        Connection conn = db.getConnection();
    String sql= """
           select d.price - sum(di.quantity_required * i.price) as marge from dishIngredient di inner join dish d on di.id_dish=d.id inner join ingredient i on i.id=di.id_ingredient where d.id=? group by d.price;""";
    PreparedStatement ps=conn.prepareStatement(sql);
    ps.setInt(1, dishId);
    ResultSet rs=ps.executeQuery();
    if(rs.next()){
        marge=rs.getDouble("marge");
    }
    conn.close();
    return marge;
    }

    public  IngredientStockEvolution getIngredientStockEvolution(Timestamp starDate,Timestamp endDate) throws SQLException{
        List<IngredientStockEvolution> ingredientStockEvolutions=new ArrayList<>();
        IngredientStockEvolution ingredientStockEvolution=null;
        Connection conn= db.getConnection();
        String sql=" select i.id as idIngredient,i.name as nameIngredient,s.quantitiy,s.creation_datetime from ingredient i  inner join stockmouvement s on s.id_ingredient=i.id where s.creation_datetime between ? and ? group by i.id,s.creation_datetime,s.quantitiy,i.name;";
         PreparedStatement ps=conn.prepareStatement(sql);
            ps.setTimestamp(1,starDate);
            ps.setTimestamp(2,endDate);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                ingredientStockEvolution= new IngredientStockEvolution(
                        rs.getInt("idIngredient"),
                        rs.getString("nameIngredient"),
                        rs.getTimestamp("creation_datetime"),
                        rs.getDouble("quantitiy")
                );
                ingredientStockEvolutions.add(ingredientStockEvolution);
            }
            conn.close();
            return ingredientStockEvolution;
    }
}