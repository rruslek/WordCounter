import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class DatabaseConnector {
    String dbName = "jdbc:postgresql://localhost/wordcounter";
    String dbDriver = "org.postgresql.Driver";
    String userName = "postgres";
    String password = "870125";

    private static Statement statement;

    private static DatabaseConnector instance;

    private DatabaseConnector() {
        try {
            Class.forName(dbDriver);
            Connection con = DriverManager.getConnection(dbName, userName, password);
            statement = con.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS Dictionary " +
                    "(word VARCHAR (255) NOT NULL, count INTEGER NOT NULL, filepath VARCHAR (255) NOT NULL," +
                    "PRIMARY KEY(word, filepath))");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static synchronized DatabaseConnector getInstance() {
        if (instance == null) {
            instance = new DatabaseConnector();
        }

        return instance;
    }

    public void insertWord(String word, Long count, String path) {
        String query = String.format("INSERT INTO Dictionary (word, count, filepath) VALUES ('%s', %s, '%s' ) ON CONFLICT (word, filepath) DO NOTHING",
                word, count, path);
        try {
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Integer> getData(String word) {
        String query = String.format("SELECT count, filepath FROM Dictionary WHERE word = '%s' ORDER BY count DESC", word);
        Map<String, Integer> result = new LinkedHashMap<>();
        try {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                result.put(resultSet.getString("filepath"), resultSet.getInt("count"));
            }
            System.out.println(result);
            return result;
        } catch (SQLException ignored) {
            return result;
        }
    }
}