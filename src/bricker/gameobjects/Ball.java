package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents the game ball that bounces off objects and plays a sound upon collision.
 *
 * @author Boaz Chijner Ankier, Elkana Daum
 */
public class Ball extends GameObject {

    /**
     * Tag identifying ball objects (including pucks) in collision checks.
     */
    public static final String BALL_TAG = "Ball";

    private final Sound collisionSound;

    /**
     * Constructs a new Ball instance.
     *
     * @param topLeftCorner Position of the ball, in window coordinates.
     * @param dimensions Width and height of the ball.
     * @param renderable The visual renderable representing the ball.
     * @param collisionSound The sound played when the ball collides with another object.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
        setTag(BALL_TAG);
    }

    /**
     * Reflects the ball's velocity across the collision normal and plays the collision sound.
     *
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        Vector2 newVelocity = getVelocity().flipped(collision.getNormal());
        setVelocity(newVelocity);
        collisionSound.play();
    }
}