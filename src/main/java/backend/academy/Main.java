package backend.academy;

import backend.academy.gallows.GameSession;
import java.util.Scanner;
import lombok.experimental.UtilityClass;

@SuppressWarnings("RegexpSinglelineJava")
@UtilityClass
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Приветствуем вас. Представтесь пожалуйста: ");
        var game = new GameSession(sc.next());
        game.session();
    }
}
