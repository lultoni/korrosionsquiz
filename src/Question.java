import java.awt.*;

public class Question {

    String q;
    String a1;
    String a2;
    String a3;
    String a4;
    int ca;
    int score;
    Image image;
    Thema thema;

    public Question(String q, String a1, String a2, String a3, String a4, int ca, int score, Image image, Thema thema) {
        this.q = q;
        this.a1 = a1;
        this.a2 = a2;
        this.a3 = a3;
        this.a4 = a4;
        this.ca = ca;
        this.score = score;
        this.image = image;
        this.thema = thema;
    }

}
