package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Displays the player's remaining lives graphically as a row of heart icons in the UI layer.
 *
 * @author Boaz Chijner Ankier, Elkana Daum
 */
public class HeartsBar extends GameObject {

    private static final int NO_HEARTS = 0;
    private static final int PREVIOUS_INDEX_OFFSET = 1;
    private static final float HEART_SPACING = 3;

    private final int maxLives;
    private final GameObject[] hearts;
    private final Vector2 heartDimensions;
    private final Renderable heartImage;
    private final GameObjectCollection collection;
    private final Counter livesCounter;
    private int heartsOnScreen;

    /**
     * Constructs a new HeartsBar instance.
     *
     * @param topLeftCorner The top-left starting position of the hearts bar.
     * @param heartDimensions The dimensions of each displayed heart icon.
     * @param heartImage The visual renderable for each heart icon.
     * @param maxLives The maximum number of hearts that can be displayed.
     * @param collection The collection of game objects in the game.
     * @param livesCounter A counter tracking the player's remaining lives.
     */
    public HeartsBar(Vector2 topLeftCorner, Vector2 heartDimensions, Renderable heartImage,
                     int maxLives, GameObjectCollection collection, Counter livesCounter) {
        super(topLeftCorner, Vector2.ZERO, null);
        this.heartDimensions = heartDimensions;
        this.heartImage = heartImage;
        this.maxLives = maxLives;
        this.collection = collection;
        this.livesCounter = livesCounter;
        this.hearts = new GameObject[maxLives];
        this.heartsOnScreen = NO_HEARTS;
        syncHeartsWithCounter();
    }

    /**
     * Synchronizes the displayed heart icons with the current value of the lives counter.
     *
     * @param deltaTime The time elapsed since the last frame, in seconds.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        syncHeartsWithCounter();
    }

    /*
     * Adds or removes heart icons until the screen matches the current lives count.
     */
    private void syncHeartsWithCounter() {
        int lives = livesCounter.value();
        while (heartsOnScreen > lives && heartsOnScreen > NO_HEARTS) {
            removeLastHeart();
        }
        while (heartsOnScreen < lives && heartsOnScreen < maxLives) {
            addNextHeart();
        }
    }

    /*
     * Removes the rightmost displayed heart icon from the UI layer.
     */
    private void removeLastHeart() {
        int lastIndex = heartsOnScreen - PREVIOUS_INDEX_OFFSET;
        collection.removeGameObject(hearts[lastIndex], Layer.UI);
        hearts[lastIndex] = null;
        heartsOnScreen--;
    }

    /*
     * Creates and adds the next heart icon to the UI layer at its computed horizontal offset.
     */
    private void addNextHeart() {
        float heartX = getTopLeftCorner().x()
                + heartsOnScreen * (heartDimensions.x() + HEART_SPACING);
        Vector2 heartPosition = new Vector2(heartX, getTopLeftCorner().y());
        GameObject heart = new GameObject(heartPosition, heartDimensions, heartImage);
        hearts[heartsOnScreen] = heart;
        collection.addGameObject(heart, Layer.UI);
        heartsOnScreen++;
    }
}