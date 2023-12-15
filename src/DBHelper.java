import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class DBHelper {

    private static final String filename = "KorrosionsDatenbank.db";

    public static void SQL_command(String command) {
        try{
            Connection connection = getConnection(filename);
            PreparedStatement statement = connection.prepareStatement(command);
            ResultSet resultSet = statement.executeQuery();

            try {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                String header = "\n| ";
                for (int i = 1; i <= columnCount; i++) {
                    header += metaData.getColumnName(i);
                    if (i + 1 <= columnCount) header += " | ";
                }
                System.out.println(header + " |");
                while (resultSet.next()) {
                    String line = " - | ";
                    for (int i = 1; i <= columnCount; i++) {
                        line += resultSet.getObject(i);
                        if (i + 1 <= columnCount) line += " | ";
                    }
                    System.out.println(line + " |");
                }
                System.out.println();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static Connection getConnection(String filename){
        Connection connection = null;
        try{
            //Verbindung zur SQLite-Datenbank herstellen
            connection = DriverManager.getConnection("jdbc:sqlite:"+filename);
            //System.out.println("Verbindung zur SQLite-Datenbank hergestellt.");
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return connection;
    }

    public static ArrayList<Thema> getThemen() {
        ArrayList<Thema> out = new ArrayList<>();
        String command = "SELECT * FROM Thema;";

        try {
            Connection connection = getConnection(filename);
            PreparedStatement statement = connection.prepareStatement(command);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("Name");

                Thema thema = new Thema(name);
                out.add(thema);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return out;
    }

    public static void addThema(String text) {
        SQL_command("INSERT INTO Thema (Name) VALUES ('" + text + "');\n");
    }

    public static ArrayList<Question> getQuestions(Thema thema) {
        ArrayList<Question> out = new ArrayList<>();
        String command = "SELECT * FROM Fragen;";

        try {
            Connection connection = getConnection(filename);
            PreparedStatement statement = connection.prepareStatement(command);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String q = resultSet.getString("FrageText");
                String a1 = resultSet.getString("AntwortA");
                String a2 = resultSet.getString("AntwortB");
                String a3 = resultSet.getString("AntwortC");
                String a4 = resultSet.getString("AntwortD");
                int ca = resultSet.getInt("RichtigeAntwort");
                int score = resultSet.getInt("Score");
                String imgPath = resultSet.getString("Bildpfad"); // TODO add this
                int themaID = resultSet.getInt("ThemaID");


                Question question = new Question(q, a1, a2, a3, a4, ca, score, null, getThema(themaID));
                if (question.thema.name.equals(thema.name)) out.add(question);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return out;
    }

    public static Thema getThema(int themaID) {
        String command = "SELECT * FROM Thema;";

        try {
            Connection connection = getConnection(filename);
            PreparedStatement statement = connection.prepareStatement(command);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("Name");
                int tid = resultSet.getInt("ThemaID");

                if (themaID == tid) return new Thema(name);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
