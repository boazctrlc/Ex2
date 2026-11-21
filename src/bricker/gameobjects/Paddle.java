package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * Represents a player-controlled paddle that moves horizontally within the screen borders.
 *
 * @author Boaz Chijner Ankier, Elkana Daum
 */
public class Paddle extends GameObject {

    /**
     * Tag identifying the main player paddle.
     */
    public static final String MAIN_PADDLE_TAG = "Main Paddle";

    private static final float MOVEMENT_SPEED = 400;

    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;
    private final float minDistanceFromEdge;

    /**
     * Constructs a new Paddle instance.
     *
     * @param topLeftCorner Position of the paddle, in window coordinates.
     * @param dimensions Width and height of the paddle.
     * @param renderable The visual renderable representing the paddle.
     * @param inputListener Listener for player keyboard input.
     * @param windowDimensions The dimensions of the game window.
     * @param minDistanceFromEdge The minimum distance the paddle must keep from screen edges.
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  UserInputListener inputListener, Vector2 windowDimensions,
                  float minDistanceFromEdge) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.minDistanceFromEdge = minDistanceFromEdge;
    }

    /**
     * Updates the paddle's velocity based on keyboard input and keeps it within screen bounds.
     *
     * @param deltaTime The time elapsed since the last frame, in seconds.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementDirection = Vector2.ZERO;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            movementDirection = movementDirection.add(Vector2.LEFT);
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            movementDirection = movementDirection.add(Vector2.RIGHT);
        }
        setVelocity(movementDirection.mult(MOVEMENT_SPEED));
        clampPositionToBorders();
    }

    /*
     * Keeps the paddle inside the horizontal boundaries defined by the screen borders.
     */
    private void clampPositionToBorders() {
        float currentX = getTopLeftCorner().x();
        float maxRightX = windowDimensions.x() - minDistanceFromEdge - getDimensions().x();

        if (currentX < minDistanceFromEdge) {
            setTopLeftCorner(new Vector2(minDistanceFromEdge, getTopLeftCorner().y()));
        } else if (currentX > maxRightX) {
            setTopLeftCorner(new Vector2(maxRightX, getTopLeftCorner().y()));
        }
    }
}