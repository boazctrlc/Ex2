package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.Random;

/**
 * Factory class responsible for creating and randomly selecting brick collision strategies
 * according to the game's probability distribution.
 *
 * @author Boaz Chijner Ankier, Elkana Daum
 * @see CollisionStrategy
 */
public class BrickStrategyFactory {

    private static final int NUMBER_OF_SPECIAL_STRATEGIES = 4;
    private static final int PUCK_STRATEGY = 0;
    private static final int EXTRA_PADDLE_STRATEGY = 1;
    private static final int EXPLODING_BRICK_STRATEGY = 2;
    private static final int EXTRA_LIFE_STRATEGY = 3;
    private static final int INITIAL_EXTRA_PADDLES = 0;

    private static final String PUCK_IMAGE_PATH = "assets/mockBall.png";
    private static final String BALL_SOUND_PATH = "assets/blop.wav";
    private static final String PADDLE_IMAGE_PATH = "assets/paddle.png";
    private static final String EXPLOSION_SOUND_PATH = "assets/explosion.wav";
    private static final String HEART_IMAGE_PATH = "assets/heart.png";

    private final Random random;
    private final CollisionStrategy basicStrategy;
    private final CollisionStrategy puckStrategy;
    private final CollisionStrategy extraPaddleStrategy;
    private final CollisionStrategy explodingBrickStrategy;
    private final CollisionStrategy extraLifeStrategy;
    private final Counter extraPaddleCounter;

    /**
     * Constructs a new BrickStrategyFactory and initializes all available collision strategies.
     *
     * @param gameObjects The collection of game objects in the game.
     * @param bricksCounter A counter tracking the remaining bricks.
     * @param livesCounter A counter tracking the player's remaining lives.
     * @param imageReader Reader used to load image assets.
     * @param soundReader Reader used to load sound assets.
     * @param inputListener Listener for player keyboard input.
     * @param windowDimensions The dimensions of the game window.
     * @param bricksGrid A two-dimensional array representing the grid of bricks.
     * @param ballSize The diameter of the main ball.
     * @param ballSpeed The speed of the main ball.
     * @param paddleDimensions The dimensions of the paddle.
     * @param heartDimensions The dimensions of the falling heart.
     * @param maxLives The maximum number of lives the player can have.
     * @param minDistanceFromEdge The minimum distance paddles must keep from screen edges.
     */
    public BrickStrategyFactory(GameObjectCollection gameObjects,
                                Counter bricksCounter,
                                Counter livesCounter,
                                ImageReader imageReader,
                                SoundReader soundReader,
                                UserInputListener inputListener,
                                Vector2 windowDimensions,
                                Brick[][] bricksGrid,
                                float ballSize,
                                float ballSpeed,
                                Vector2 paddleDimensions,
                                Vector2 heartDimensions,
                                int maxLives,
                                float minDistanceFromEdge) {
        this.random = new Random();
        this.extraPaddleCounter = new Counter(INITIAL_EXTRA_PADDLES);

        Renderable puckImage = imageReader.readImage(PUCK_IMAGE_PATH, true);
        Sound ballSound = soundReader.readSound(BALL_SOUND_PATH);
        Renderable paddleImage = imageReader.readImage(PADDLE_IMAGE_PATH, false);
        Sound explosionSound = soundReader.readSound(EXPLOSION_SOUND_PATH);
        Renderable heartImage = imageReader.readImage(HEART_IMAGE_PATH, true);

        this.basicStrategy = new BasicCollisionStrategy(gameObjects, bricksCounter);
        this.puckStrategy = new PuckCollisionStrategy(
                gameObjects, bricksCounter, puckImage, ballSound,
                ballSize, ballSpeed, windowDimensions.y());
        this.extraPaddleStrategy = new ExtraPaddleStrategy(
                gameObjects, bricksCounter, paddleImage, inputListener,
                paddleDimensions, windowDimensions, extraPaddleCounter, minDistanceFromEdge);
        this.explodingBrickStrategy = new ExplodingBrickStrategy(
                gameObjects, bricksCounter, bricksGrid, explosionSound);
        this.extraLifeStrategy = new ExtraLifeStrategy(
                gameObjects, bricksCounter, livesCounter, heartImage,
                heartDimensions, windowDimensions.y(), maxLives);
    }

    /**
     * Randomly selects and returns a collision strategy.
     * Returns the basic strategy with probability 1/2, or one of the four special strategies
     * with probability 1/8 each.
     *
     * @return A randomly selected CollisionStrategy instance.
     */
    public CollisionStrategy getStrategy() {
        if (random.nextBoolean()) {
            return basicStrategy;
        }
        return switch (random.nextInt(NUMBER_OF_SPECIAL_STRATEGIES)) {
            case PUCK_STRATEGY -> puckStrategy;
            case EXTRA_PADDLE_STRATEGY -> extraPaddleStrategy;
            case EXPLODING_BRICK_STRATEGY -> explodingBrickStrategy;
            case EXTRA_LIFE_STRATEGY -> extraLifeStrategy;
            default -> basicStrategy;
        };
    }
}