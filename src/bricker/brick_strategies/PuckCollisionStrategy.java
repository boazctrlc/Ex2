package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import bricker.gameobjects.Puck;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.Random;

/**
 * A collision strategy that spawns puck balls from the center of the broken brick.
 *
 * @author Boaz Chijner Ankier, Elkana Daum
 * @see BasicCollisionStrategy
 */
public class PuckCollisionStrategy extends BasicCollisionStrategy {

    private static final float BALL_TO_PUCK_RATIO = 0.75f;
    private static final int NUMBER_OF_PUCKS = 2;

    private final Renderable puckImage;
    private final Sound collisionSound;
    private final Vector2 puckDimensions;
    private final float puckSpeed;
    private final float windowHeight;
    private final Random random;

    /**
     * Constructs a new PuckCollisionStrategy.
     *
     * @param collection The collection of game objects in the game.
     * @param bricksCounter A counter tracking the remaining bricks.
     * @param puckImage The visual renderable for the pucks.
     * @param collisionSound The sound played when a puck collides.
     * @param ballSize The diameter of the main ball.
     * @param ballSpeed The speed of the main ball.
     * @param windowHeight The height of the game window.
     */
    public PuckCollisionStrategy(GameObjectCollection collection, Counter bricksCounter,
                                 Renderable puckImage, Sound collisionSound,
                                 float ballSize, float ballSpeed, float windowHeight) {
        super(collection, bricksCounter);
        this.puckImage = puckImage;
        this.collisionSound = collisionSound;
        this.puckSpeed = ballSpeed;
        this.puckDimensions = new Vector2(ballSize, ballSize).mult(BALL_TO_PUCK_RATIO);
        this.windowHeight = windowHeight;
        this.random = new Random();
    }

    /**
     * Removes the brick and spawns two pucks at the brick's center with random velocities.
     *
     * @param brick The brick that was hit.
     * @param otherObject The game object that collided with the brick.
     */
    @Override
    public void onCollision(Brick brick, GameObject otherObject) {
        if (!removeBrick(brick)) {
            return;
        }
        Vector2 brickCenter = brick.getCenter();
        for (int i = 0; i < NUMBER_OF_PUCKS; i++) {
            Puck puck = new Puck(
                    Vector2.ZERO,
                    puckDimensions,
                    puckImage,
                    collisionSound,
                    windowHeight,
                    collection);
            puck.setCenter(brickCenter);
            setVelocity(puck);
            collection.addGameObject(puck);
        }
    }

    /*
     * Assigns a random initial velocity to the given puck along the upper half of the unit circle.
     */
    private void setVelocity(Puck puck) {
        double angle = random.nextDouble() * Math.PI;
        float velocityX = (float) Math.cos(angle) * puckSpeed;
        float velocityY = (float) Math.sin(angle) * puckSpeed;
        puck.setVelocity(new Vector2(velocityX, velocityY));
    }
}