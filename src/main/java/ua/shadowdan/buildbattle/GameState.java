package ua.shadowdan.buildbattle;

/**
 * Created by SHADOWDAN on 30.06.2019.
 */
public enum GameState {
    WAITING("Ожидание игроков"),
    GAME("Идёт игра"),
    VOTE("Голосование"),
    END("Завершение");

    private String description;

    GameState(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
