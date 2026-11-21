package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.Color;

/**
 * Displays the player's remaining lives numerically with color-coding based on remaining count.
 *
 * @author Boaz Chijner Ankier, Elkana Daum
 */
public class HeartsNumber extends GameObject {

    private static final int GREEN_LIVES_THRESHOLD = 3;
    private static final int YELLOW_LIVES_THRESHOLD = 2;

    private final Counter livesCounter;
    private final TextRenderable textRenderable;
    private int currentLives;

    /**
     * Constructs a new HeartsNumber display.
     *
     * @param topLeftCorner Position of the numeric counter, in window coordinates.
     * @param dimensions Width and height of the numeric counter.
     * @param livesCounter A counter tracking the player's remaining lives.
     */
    public HeartsNumber(Vector2 topLeftCorner, Vector2 dimensions, Counter livesCounter) {
        this(topLeftCorner, dimensions, livesCounter,
                new TextRenderable(Integer.toString(livesCounter.value())));
    }

    /*
     * Internal constructor accepting the initialized TextRenderable instance.
     */
    private HeartsNumber(Vector2 topLeftCorner, Vector2 dimensions,
                         Counter livesCounter, TextRenderable textRenderable) {
        super(topLeftCorner, dimensions, textRenderable);
        this.livesCounter = livesCounter;
        this.textRenderable = textRenderable;
        this.currentLives = livesCounter.value();
        this.textRenderable.setColor(decideColor(currentLives));
    }

    /**
     * Updates the displayed number and text color whenever the lives counter changes.
     *
     * @param deltaTime The time elapsed since the last frame, in seconds.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        int lives = livesCounter.value();
        if (lives != currentLives) {
            textRenderable.setString(Integer.toString(lives));
            textRenderable.setColor(decideColor(lives));
            currentLives = lives;
        }
    }

    /*
     * Selects the display color according to the current number of lives.
     */
    private Color decideColor(int lives) {
        if (lives >= GREEN_LIVES_THRESHOLD) {
            return Color.GREEN;
        }
        if (lives == YELLOW_LIVES_THRESHOLD) {
            return Color.YELLOW;
        }
        return Color.RED;
    }
}