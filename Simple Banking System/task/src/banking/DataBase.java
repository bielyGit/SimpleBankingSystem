package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase {
    static String fileName = "default";

    public DataBase (String name){
        this.fileName = name;
    }

    public void executeUpdateStatement(String sqlStatement) {
        Connection conn = null;
        String url = "jdbc:sqlite:" + fileName;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        try {
            conn = dataSource.getConnection();

            try (Statement statement = conn.createStatement()) {
                // Statement execution
                statement.executeUpdate(sqlStatement);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void connect() {
        String createTable = ("CREATE TABLE IF NOT EXISTS card(" +
            "id INTEGER PRIMARY KEY," +
            "number TEXT NOT NULL," +
            "pin VARCHAR (4) NOT NULL," +
            "balance INTEGER DEFAULT 0)");
        executeUpdateStatement(createTable);
}

    public void addEntity(Account account) {
        System.out.println("pin nr: " + account.getPin());

        String sql = "INSERT INTO card (number, pin) VALUES (" +
                account.getCardNumber() + ", " +
                account.getPin() + ");";
        executeUpdateStatement(sql);
    }

    public int getBalance (Account account) {
        Connection conn = null;
        String url = "jdbc:sqlite:" + fileName;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        try {
            conn = dataSource.getConnection();
            try (Statement statement = conn.createStatement()) {
                // Statement execution

                ResultSet rs = statement.executeQuery("SELECT balance FROM card WHERE number = " +
                        account.getCardNumber() + ";");
                return rs.getInt("balance");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public void addIncome (Account account, int addValue) {
        String updateBalance = "UPDATE card SET balance = " +
                (int)(getBalance(account) + addValue) +
                " WHERE number = " +
                account.getCardNumber() + ";";
        executeUpdateStatement(updateBalance);
    }

    public int ifCardNumberExists(String cardNumber, Account account) {
        Connection conn = null;
        String url = "jdbc:sqlite:" + fileName;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        try {
            conn = dataSource.getConnection();
            try (Statement statement = conn.createStatement()) {

                // Statement execution
                ResultSet rs = statement.executeQuery("SELECT number FROM card WHERE number = " +
                        "'" + cardNumber + "'" + ";");
                String result = rs.getString("number");
                if (cardNumber.equals(result)){
                    if (result.equals(account.getCardNumber())) {
                        return 2;
                    } else return 1;
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public void closeAccount(Account account) {
        String sql = "DELETE from card WHERE number = '" +
                account.getCardNumber() +"';";
        executeUpdateStatement(sql);
    }

}
