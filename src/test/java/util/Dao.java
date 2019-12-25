package util;

import java.sql.*;
import java.util.ArrayList;

public class Dao {
    public static String getId(String login) throws SQLException {
        try (Connection cn = ConnectionFactory.getConnection();
             PreparedStatement st = cn.prepareStatement("SELECT id FROM users WHERE login = ?;");
        ) {
            st.setString(1, login);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    return rs.getString("id");
                }
                return null;
            }
        }
    }

    public static String getAuthCode(String id) throws SQLException {
        try (Connection cn = ConnectionFactory.getConnection();
             PreparedStatement st = cn.prepareStatement("SELECT code FROM auth_codes WHERE user_id = ? ORDER BY created;")
        ) {
            st.setString(1, id);
            try (ResultSet rs = st.executeQuery()) {
                ArrayList<String> codes = new ArrayList<>();
                while (rs.next()) {
                    codes.add(rs.getString("code"));
                }
                return codes.get(codes.size() - 1);
            }
        }
    }

    public static void clearAuthCodes() throws SQLException {
        try (Connection cn = ConnectionFactory.getConnection();
             Statement st = cn.createStatement()
        ) {
            st.execute("TRUNCATE TABLE auth_codes;");
        }
    }

    public static void clearAllTables() throws SQLException {
        try (Connection cn = ConnectionFactory.getConnection();
             Statement st1 = cn.createStatement();
             Statement st2 = cn.createStatement();
             Statement st3 = cn.createStatement()) {
            st1.execute("TRUNCATE TABLE auth_codes;");
            st2.execute("TRUNCATE TABLE cards;");
            st3.execute("DELETE FROM users WHERE id IS NOT NULL;");
        }
    }

    public static void setBalanceTo10000() throws SQLException {
        try (Connection cn = ConnectionFactory.getConnection();
             Statement st1 = cn.createStatement();
             Statement st2 = cn.createStatement()) {
            st1.execute("UPDATE cards SET balance_in_kopecks='1000000' WHERE number='5559 0000 0000 0002';");
            st1.execute("UPDATE cards SET balance_in_kopecks='1000000' WHERE number='5559 0000 0000 0001';");
        }
    }

    public static String getCardBalance(String cardNumber) throws SQLException {
        try (Connection cn = ConnectionFactory.getConnection();
             PreparedStatement st = cn.prepareStatement("SELECT balance_in_kopecks FROM cards WHERE number = ?;")
        ) {
            st.setString(1, cardNumber);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    return rs.getString("balance_in_kopecks");
                }
                return null;
            }
        }
    }
}
