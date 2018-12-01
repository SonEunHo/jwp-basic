package next.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class MyJdbcTemplate {
    private Supplier<Connection> supplier;
    public MyJdbcTemplate(Supplier<Connection> supplier) {
        this.supplier = supplier;
    }

    public <T> List<T> executeQuery(String query,
                                    ParameterSetter paramSetter,
                                    RowMapper<T> resultSetConsumer) throws JdbcSqlException {
        ResultSet rs = null;
        List<T> result = new ArrayList<>();
        try (Connection con = supplier.get();
            PreparedStatement pstmt = con.prepareStatement(query)) {
            paramSetter.set(pstmt);
            rs = pstmt.executeQuery();
            while(rs.next()) {
                result.add(resultSetConsumer.map(rs));
            }
        } catch (SQLException e) {
            throw new JdbcSqlException(e);
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                throw new JdbcSqlException("fail to close resultSet", e);
            }
        }

        return result;
    }

    public Integer executeUpdate(String query, ParameterSetter paramSetter) throws JdbcSqlException {
        Integer result = null;

        try (Connection con = supplier.get();
            PreparedStatement pstmt = con.prepareStatement(query)) {
            paramSetter.set(pstmt);
            result = pstmt.executeUpdate();
        } catch (SQLException e){
            throw new JdbcSqlException(e);
        }
        return result;
    }

    public Integer executeUpdate(String query, Object... params) {
        Integer result = null;

        try (Connection con = supplier.get();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i+1, params[i]);
            }
            result = pstmt.executeUpdate();
        } catch (SQLException e){
            throw new JdbcSqlException(e);
        }
        return result;
    }

    public <T> T executeForObject(String query,
                         ParameterSetter paramSetter,
                         RowMapper<T> rowMapper) throws JdbcSqlException {
        List<T> temp = executeQuery(query, paramSetter, rowMapper);
        if(temp == null || temp.isEmpty()) {
            return null;
        }
        return temp.get(0);
    }

    class JdbcSqlException extends RuntimeException {
        private String msg;
        private SQLException sqlException;

        public JdbcSqlException(SQLException sqlException) {
            this(sqlException.getMessage());
            this.sqlException = sqlException;
        }

        public JdbcSqlException(String msg, SQLException sqlException) {
            super(msg);
            this.msg = msg;
            this.sqlException = sqlException;
        }

        public JdbcSqlException(String msg) {
            super(msg);
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public SQLException getSqlException() {
            return sqlException;
        }

        public void setSqlException(SQLException sqlException) {
            this.sqlException = sqlException;
        }
    }
}
