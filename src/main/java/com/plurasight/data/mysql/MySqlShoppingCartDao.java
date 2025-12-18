package com.plurasight.data.mysql;

import com.plurasight.data.ShoppingCartDao;
import com.plurasight.models.Product;
import com.plurasight.models.ShoppingCart;
import com.plurasight.models.ShoppingCartItem;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao
{
    public MySqlShoppingCartDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId)
    {
        String sql = """
            SELECT
                p.product_id,
                p.name,
                p.price,
                p.category_id,
                p.description,
                p.subcategory,
                p.stock,
                p.image_url,
                p.featured,
                sc.quantity
            FROM shopping_cart sc
            JOIN products p ON p.product_id = sc.product_id
            WHERE sc.user_id = ?
        """;

        ShoppingCart cart = new ShoppingCart();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1, userId);

            try (ResultSet results = statement.executeQuery())
            {
                while (results.next())
                {
                    Product product = MySqlProductDao.mapRow(results);
                    int quantity = results.getInt("quantity");

                    ShoppingCartItem item = new ShoppingCartItem();
                    item.setProduct(product);
                    item.setQuantity(quantity);

                    // Use ShoppingCart.add to handle duplicates
                    cart.add(item);
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error loading shopping cart", e);
        }

        return cart;
    }

    @Override
    public boolean exists(int userId, int productId)
    {
        String sql = "SELECT COUNT(*) FROM shopping_cart WHERE user_id = ? AND product_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1, userId);
            statement.setInt(2, productId);

            try (ResultSet rs = statement.executeQuery())
            {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error checking cart item existence", e);
        }
    }

    @Override
    public void add(int userId, int productId, int quantity)
    {
        if (exists(userId, productId)) {
            incrementQuantityBy(userId, productId, quantity);
            return;
        }

        String sql = "INSERT INTO shopping_cart (user_id, product_id, quantity) VALUES (?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1, userId);
            statement.setInt(2, productId);
            statement.setInt(3, quantity);
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error adding item to cart", e);
        }
    }

    @Override
    public void incrementQuantity(int userId, int productId)
    {
        incrementQuantityBy(userId, productId, 1);
    }

    private void incrementQuantityBy(int userId, int productId, int amount)
    {
        String sql = "UPDATE shopping_cart SET quantity = quantity + ? WHERE user_id = ? AND product_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1, amount);
            statement.setInt(2, userId);
            statement.setInt(3, productId);
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error incrementing cart quantity", e);
        }
    }

    @Override
    public void update(int userId, int productId, int quantity)
    {
        String sql = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1, quantity);
            statement.setInt(2, userId);
            statement.setInt(3, productId);
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error updating cart item quantity", e);
        }
    }

    @Override
    public void delete(int userId, int productId)
    {
        String sql = "DELETE FROM shopping_cart WHERE user_id = ? AND product_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1, userId);
            statement.setInt(2, productId);
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error deleting cart item", e);
        }
    }

    @Override
    public void clear(int userId)
    {
        String sql = "DELETE FROM shopping_cart WHERE user_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1, userId);
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error clearing cart", e);
        }
    }
}