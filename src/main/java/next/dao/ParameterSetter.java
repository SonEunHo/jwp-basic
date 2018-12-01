package next.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface ParameterSetter {
    void set(PreparedStatement pstmt) throws SQLException;
}
