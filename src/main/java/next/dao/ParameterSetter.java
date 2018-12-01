package next.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ParameterSetter {
    void paramSet(PreparedStatement pstmt) throws SQLException;
}
