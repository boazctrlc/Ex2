package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import bricker.gameobjects.ExtraPaddle;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * A collision strategy that spawns an additional paddle at the center of the screen
 * if no extra paddle currently exists.
 *
 * @author Boaz Chijner Ankier, Elkana Daum
 * @see BasicCollisionStrategy
 */
public class ExtraPaddleStrategy extends BasicCollisionStrategy {

    private static final int NO_EXTRA_PADDLE = 0;
    private static final float SCREEN_CENTER_FACTOR = 0.5f;

    private final Renderable paddleImage;
    private final UserInputListener inputListener;
    private final Vector2 paddleDimensions;
    private final Vector2 windowDimensions;
    private final Counter extraPaddleCounter;
    private final float minDistanceFromEdge;

    /**
     * Constructs a new ExtraPaddleStrategy.
     *
     * @param gameObjects The collection of game objects in the game.
     * @param bricksCounter A counter tracking the remaining bricks.
     * @param paddleImage The visual renderable for the extra paddle.
     * @param inputListener Listener for player keyboard input.
     * @param paddleDimensions The dimensions of the extra paddle.
     * @param windowDimensions The dimensions of the game window.
     * @param extraPaddleCounter A counter tracking active extra paddles.
     * @param minDistanceFromEdge The minimum distance the paddle must keep from screen edges.
     */
    public ExtraPaddleStrategy(GameObjectCollection gameObjects, Counter bricksCounter,
                               Renderable paddleImage, UserInputListener inputListener,
                               Vector2 paddleDimensions, Vector2 windowDimensions,
                               Counter extraPaddleCounter, float minDistanceFromEdge) {
        super(gameObjects, bricksCounter);
        this.paddleImage = paddleImage;
        this.inputListener = inputListener;
        this.paddleDimensions = paddleDimensions;
        this.windowDimensions = windowDimensions;
        this.extraPaddleCounter = extraPaddleCounter;
        this.minDistanceFromEdge = minDistanceFromEdge;
    }

    /**
     * Removes the brick and creates an extra paddle in the center of the screen if none exists.
     *
     * @param brick The brick that was hit.
     * @param otherObject The game object that collided with the brick.
     */
    @Override
    public void onCollision(Brick brick, GameObject otherObject) {
        if (!removeBrick(brick)) {
            return;
        }
        if (extraPaddleCounter.value() == NO_EXTRA_PADDLE) {
            GameObject extraPaddle = new ExtraPaddle(
                    Vector2.ZERO,
                    paddleDimensions,
                    paddleImage,
                    inputListener,
                    extraPaddleCounter,
                    collection,
                    windowDimensions,
                    minDistanceFromEdge);
            extraPaddle.setCenter(windowDimensions.mult(SCREEN_CENTER_FACTOR));
            collection.addGameObject(extraPaddle);
            extraPaddleCounter.increment();
        }
    }
}