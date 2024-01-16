import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class DBHelper {

    private static final String filename = "KorrosionsDatenbank.db";

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
            System.out.println(e + " - SQL Error");
        }

        return out;
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
                String imgPath = resultSet.getString("Bildpfad");
                int themaID = resultSet.getInt("ThemaID");


                Question question = new Question(q, addHTML(a1), addHTML(a2), addHTML(a3), addHTML(a4), ca, score, imgPath, getThema(themaID));
                if (question.thema.name.equals(thema.name)) out.add(question);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println(e + " - SQL Error");
        }

        return out;
    }

    private static String addHTML(String s) {
        return "<html>" + s + "<html>";
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
            System.out.println(e + " - SQL Error");
        }
        return null;
    }

}
