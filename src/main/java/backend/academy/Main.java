package backend.academy;

import backend.academy.gallows.GameUIImpl;
import java.util.Scanner;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
//        System.out.print("Приветствуем вас. Представтесь пожалуйста: ");
//        var game = new GameSession(sc.next());
//        game.session();
        var ui = new GameUIImpl();
        ui.printEmoji("sdf");
    }
}
