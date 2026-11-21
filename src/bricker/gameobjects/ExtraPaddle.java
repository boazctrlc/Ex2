package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Represents a temporary extra paddle that disappears after absorbing a fixed number of ball hits.
 *
 * @author Boaz Chijner Ankier, Elkana Daum
 * @see Paddle
 */
public class ExtraPaddle extends Paddle {

    private static final int MAX_HITS = 4;
    private static final int INITIAL_HITS = 0;

    private final Counter hitsCounter;
    private final Counter extraPaddleCounter;
    private final GameObjectCollection collection;

    /**
     * Constructs a new ExtraPaddle instance.
     *
     * @param topLeftCorner Position of the extra paddle, in window coordinates.
     * @param dimensions Width and height of the extra paddle.
     * @param renderable The visual renderable representing the extra paddle.
     * @param inputListener Listener for player keyboard input.
     * @param extraPaddleCounter A counter tracking active extra paddles in the game.
     * @param collection The collection of game objects in the game.
     * @param windowDimensions The dimensions of the game window.
     * @param minDistanceFromEdge The minimum distance the paddle must keep from screen edges.
     */
    public ExtraPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                       UserInputListener inputListener, Counter extraPaddleCounter,
                       GameObjectCollection collection, Vector2 windowDimensions,
                       float minDistanceFromEdge) {
        super(topLeftCorner, dimensions, renderable, inputListener,
                windowDimensions, minDistanceFromEdge);
        this.hitsCounter = new Counter(INITIAL_HITS);
        this.extraPaddleCounter = extraPaddleCounter;
        this.collection = collection;
    }

    /**
     * Increments the hit counter on ball collisions and removes this paddle once the hit limit
     * is reached.
     *
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (Ball.BALL_TAG.equals(other.getTag())) {
            hitsCounter.increment();
            if (hitsCounter.value() >= MAX_HITS && collection.removeGameObject(this)) {
                extraPaddleCounter.decrement();
            }
        }
    }
}