package ua.shadowdan.buildbattle.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ua.shadowdan.buildbattle.GameState;

import javax.annotation.Nonnull;

/**
 * Created by SHADOWDAN on 03.07.2019.
 */
public class GameStateChangeEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();
    @Getter @Setter
    private GameState gameState;

    public GameStateChangeEvent(GameState gameState) {
        this.gameState = gameState;
    }

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    @Nonnull
    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
