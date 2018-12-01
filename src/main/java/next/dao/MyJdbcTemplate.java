package next.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MyJdbcTemplate {
    private Supplier<Connection> supplier;
    public MyJdbcTemplate(Supplier<Connection> supplier) {
        this.supplier = supplier;
    }

    public void executeQuery(String query,
                             ParameterSetter paramSetter,
                             ResultSetConsumer resultSetConsumer) {
        try(ResultSet rs = executeForResultSet(query, paramSetter)) {
            resultSetConsumer.accept(rs);
        } catch (SQLException e) {
            throw new JdbcSqlException(e);
        }
    }

    public Integer executeUpdate(String query, ParameterSetter paramSetter) {
        Integer result = null;

        try (Connection con = supplier.get();
            PreparedStatement pstmt = con.prepareStatement(query)) {
            paramSetter.paramSet(pstmt);
            result = pstmt.executeUpdate();
        } catch (SQLException e){
            throw new JdbcSqlException(e);
        }
        return result;
    }

    public <T> T executeForObject(String query,
                         ParameterSetter paramSetter,
                         ResultSetExtractor<T> extractor) {
        T result = null;

        try (ResultSet rs = executeForResultSet(query, paramSetter)) {
            result = extractor.extract(rs);
        } catch (SQLException e) {
            throw new JdbcSqlException(e);
        }

        return result;
    }

    public ResultSet executeForResultSet(String query,
                                         ParameterSetter paramSetter) {
        ResultSet rs = null;
        try(Connection con = supplier.get();
            PreparedStatement pstmt = con.prepareStatement(query)) {
            paramSetter.paramSet(pstmt);
            rs = pstmt.executeQuery();
        } catch (SQLException e) {
            throw new JdbcSqlException(e);
        }
        return rs;
    }

    class JdbcSqlException extends RuntimeException {
        private String msg;
        private SQLException sqlException;

        public JdbcSqlException(SQLException sqlException) {
            this(sqlException.getMessage());
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
