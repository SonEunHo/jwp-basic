package next.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;

import org.springframework.jdbc.support.xml.SqlXmlFeatureNotImplementedException;

public class MyJdbcTemplate {
    private Supplier<Connection> supplier;
    public MyJdbcTemplate(Supplier<Connection> supplier) {
        this.supplier = supplier;
    }

    public void executeQuery(String query,
                             ParameterSetter paramSetter,
                             ResultSetConsumer resultSetConsumer) {
        ResultSet rs = null;
        try (Connection con = supplier.get();
            PreparedStatement pstmt = con.prepareStatement(query)) {
            paramSetter.paramSet(pstmt);
            rs = pstmt.executeQuery();
            resultSetConsumer.accept(rs);
        } catch (SQLException e) {
            throw new JdbcSqlException(e);
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                throw new JdbcSqlException("fail to close resultSet", e);
            }
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

        ResultSet rs = null;
        try (Connection con = supplier.get();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            paramSetter.paramSet(pstmt);
            rs = pstmt.executeQuery();
            result = extractor.extract(rs);
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
