package backend.academy.gallows.model;

import lombok.Getter;

@Getter
public enum Levels {
    EASY(9, 1),
    MIDDLE(5, 2),
    HARD(3, 4),

    RANDOM(0, 0); // не использовать нигде кроме при выборе меню

    private final int hitpoint;
    private final int step;

    private Levels(int hitpoint, int step) {
        this.hitpoint = hitpoint;
        this.step = step;
    }
}
