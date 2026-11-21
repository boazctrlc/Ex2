package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Represents a falling heart spawned by an extra-life brick.
 * Can only be caught by the main paddle to restore a lost life.
 *
 * @author Boaz Chijner Ankier, Elkana Daum
 */
public class FallingHeart extends GameObject {

    private static final float HORIZONTAL_VELOCITY = 0;
    private static final float FALL_SPEED = 100;

    private final int maxLives;
    private final Counter livesCounter;
    private final GameObjectCollection collection;
    private final float windowHeight;

    /**
     * Constructs a new FallingHeart instance.
     *
     * @param topLeftCorner Position of the heart, in window coordinates.
     * @param dimensions Width and height of the heart.
     * @param renderable The visual renderable representing the heart.
     * @param maxLives The maximum number of lives the player can have.
     * @param livesCounter A counter tracking the player's remaining lives.
     * @param collection The collection of game objects in the game.
     * @param windowHeight The height of the game window.
     */
    public FallingHeart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                        int maxLives, Counter livesCounter, GameObjectCollection collection,
                        float windowHeight) {
        super(topLeftCorner, dimensions, renderable);
        this.maxLives = maxLives;
        this.livesCounter = livesCounter;
        this.collection = collection;
        this.windowHeight = windowHeight;
        setVelocity(new Vector2(HORIZONTAL_VELOCITY, FALL_SPEED));
    }

    /**
     * Allows collisions only with the main player paddle.
     *
     * @param other The other GameObject to check collision eligibility against.
     * @return true if the other object is the main paddle, false otherwise.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return Paddle.MAIN_PADDLE_TAG.equals(other.getTag());
    }

    /**
     * Removes the falling heart and increments the lives counter if below the maximum.
     *
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (collection.removeGameObject(this) && livesCounter.value() < maxLives) {
            livesCounter.increment();
        }
    }

    /**
     * Removes the falling heart once it moves below the bottom of the screen.
     *
     * @param deltaTime The time elapsed since the last frame, in seconds.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (getTopLeftCorner().y() > windowHeight) {
            collection.removeGameObject(this);
        }
    }
}