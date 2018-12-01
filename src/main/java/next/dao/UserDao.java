package next.dao;

import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import core.jdbc.ConnectionManager;
import next.model.User;

public class UserDao {
    private static MyJdbcTemplate template =
            new MyJdbcTemplate(ConnectionManager::getConnection);

    public void insert(final User user) throws SQLException {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        template.executeUpdate(sql, pstmt -> {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
        });
    }

    public void update(final User user) throws SQLException {
        final String sql = "Update USERS SET password = ?, name = ?, email = ? WHERE userid = ?";
        template.executeUpdate(sql, pstmt -> {
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getUserId());
        });
    }

    public List<User> findAll() throws SQLException {
        final String sql = "SELECT userId, password, name, email FROM USERS";
        return template.executeQuery(sql, pstmt -> {}, resultSet -> {
            return new User(resultSet.getString("userId"),
                            resultSet.getString("password"),
                            resultSet.getString("name"),
                            resultSet.getString("email"));
        });
    }

    public User findByUserId(final String userId) throws SQLException {
        final String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

        User user = template.executeForObject(sql, pstmt -> {
            pstmt.setString(1, userId);
        }, resultSet -> {
            return new User(resultSet.getString("userId"), resultSet.getString("password"),
                            resultSet.getString("name"),
                            resultSet.getString("email"));
        });

        return user;
    }
}
