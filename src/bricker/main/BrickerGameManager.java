package bricker.main;

import bricker.brick_strategies.BrickStrategyFactory;
import bricker.brick_strategies.CollisionStrategy;
import bricker.gameobjects.Ball;
import bricker.gameobjects.Brick;
import bricker.gameobjects.HeartsBar;
import bricker.gameobjects.HeartsNumber;
import bricker.gameobjects.Paddle;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Main game manager for the Bricker game.
 * Responsible for initializing game objects, managing game state, and handling win/loss conditions.
 *
 * @author Boaz Chijner Ankier, Elkana Daum
 */
public class BrickerGameManager extends GameManager {

    private static final String WINDOW_TITLE = "Bricker";
    private static final int WINDOW_WIDTH = 700;
    private static final int WINDOW_HEIGHT = 500;
    private static final int EXPECTED_ARGS_COUNT = 2;
    private static final int BRICKS_PER_ROW_ARG_INDEX = 0;
    private static final int BRICK_ROWS_ARG_INDEX = 1;

    private static final int BORDER_WIDTH = 10;
    private static final int VERTICAL_BORDERS_COUNT = 2;
    private static final int PADDLE_HEIGHT = 20;
    private static final int PADDLE_WIDTH = 100;
    private static final float PADDLE_BOTTOM_OFFSET = 30;
    private static final float BALL_DIAMETER = 35;
    private static final float BALL_SPEED = 350;
    private static final int BRICK_HEIGHT = 15;
    private static final int DEFAULT_BRICKS_PER_ROW = 8;
    private static final int DEFAULT_BRICK_ROWS = 7;
    private static final float BRICK_SPACING = 5;
    private static final int EXTRA_EDGE_SPACE = 1;
    private static final float TOP_BRICKS_OFFSET = 50;

    private static final int DEFAULT_LIVES = 3;
    private static final int MAX_LIVES = 4;
    private static final int ZERO_COUNT = 0;
    private static final float SCREEN_CENTER_FACTOR = 0.5f;
    private static final float DIRECTION_FLIP_FACTOR = -1;
    private static final float UI_BOTTOM_OFFSET = 25;
    private static final float NUMERIC_COUNTER_LEFT_OFFSET = 5;
    private static final float HEARTS_BAR_LEFT_OFFSET = 30;
    private static final float TOP_EDGE_Y = 0;

    private static final int BORDER_RED = 80;
    private static final int BORDER_GREEN = 140;
    private static final int BORDER_BLUE = 250;
    private static final Renderable BORDER_RENDERABLE =
            new RectangleRenderable(new Color(BORDER_RED, BORDER_GREEN, BORDER_BLUE));
    private static final Vector2 HEART_DIMENSIONS = new Vector2(15, 15);
    private static final Vector2 NUMERIC_COUNTER_DIMENSIONS = new Vector2(20, 20);

    private static final String BALL_IMAGE_PATH = "assets/ball.png";
    private static final String BALL_SOUND_PATH = "assets/blop.wav";
    private static final String PADDLE_IMAGE_PATH = "assets/paddle.png";
    private static final String BRICK_IMAGE_PATH = "assets/brick.png";
    private static final String HEART_IMAGE_PATH = "assets/heart.png";
    private static final String WIN_PROMPT = "You win! Play again?";
    private static final String LOSE_PROMPT = "You lose! Play again?";

    private final int bricksPerRow;
    private final int brickRows;
    private final int initialLives;
    private final Random random;
    private Counter livesCount;
    private Counter bricksOnScreen;
    private Ball ball;
    private Vector2 windowDimensions;
    private WindowController windowController;
    private UserInputListener userInputListener;

    /**
     * Constructs a BrickerGameManager with default brick grid and life settings.
     *
     * @param windowTitle The title of the game window.
     * @param windowDimensions The dimensions of the game window.
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        this(windowTitle, windowDimensions,
                DEFAULT_BRICKS_PER_ROW, DEFAULT_BRICK_ROWS, DEFAULT_LIVES);
    }

    /**
     * Constructs a BrickerGameManager with custom brick grid dimensions and default lives.
     *
     * @param windowTitle The title of the game window.
     * @param windowDimensions The dimensions of the game window.
     * @param bricksPerRow Number of bricks in each row.
     * @param brickRows Number of brick rows.
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions,
                              int bricksPerRow, int brickRows) {
        this(windowTitle, windowDimensions, bricksPerRow, brickRows, DEFAULT_LIVES);
    }

    /**
     * Constructs a BrickerGameManager with custom brick grid dimensions and initial lives.
     *
     * @param windowTitle The title of the game window.
     * @param windowDimensions The dimensions of the game window.
     * @param bricksPerRow Number of bricks in each row.
     * @param brickRows Number of brick rows.
     * @param initialLives The starting number of lives for the player.
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions,
                              int bricksPerRow, int brickRows, int initialLives) {
        super(windowTitle, windowDimensions);
        this.bricksPerRow = bricksPerRow;
        this.brickRows = brickRows;
        this.initialLives = initialLives;
        this.random = new Random();
    }

    /**
     * Initializes all game objects, borders, bricks, and UI counters.
     *
     * @param imageReader Reader used to load image assets.
     * @param soundReader Reader used to load sound assets.
     * @param inputListener Listener for player keyboard input.
     * @param windowController Controller for managing the game window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowController = windowController;
        this.userInputListener = inputListener;
        this.windowDimensions = windowController.getWindowDimensions();
        this.livesCount = new Counter(initialLives);
        this.bricksOnScreen = new Counter(ZERO_COUNT);

        createBall(imageReader, soundReader);
        createPaddle(imageReader, inputListener);
        createBorders();
        createBricks(imageReader, soundReader, inputListener);
        createUiCounters(imageReader);
    }

    /**
     * Updates the game state on each frame and checks for win or loss conditions.
     *
     * @param deltaTime The time elapsed since the last frame, in seconds.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkForGameEnd();
    }

    /**
     * Entry point for launching the Bricker game.
     * Accepts optional command-line arguments for bricks per row and number of rows.
     *
     * @param args Optional command-line arguments: [bricksPerRow, brickRows].
     */
    public static void main(String[] args) {
        Vector2 dimensions = new Vector2(WINDOW_WIDTH, WINDOW_HEIGHT);
        BrickerGameManager game;
        if (args.length == EXPECTED_ARGS_COUNT) {
            int bricksPerRow = Integer.parseInt(args[BRICKS_PER_ROW_ARG_INDEX]);
            int brickRows = Integer.parseInt(args[BRICK_ROWS_ARG_INDEX]);
            game = new BrickerGameManager(WINDOW_TITLE, dimensions, bricksPerRow, brickRows);
        } else {
            game = new BrickerGameManager(WINDOW_TITLE, dimensions);
        }
        game.run();
    }

    /*
     * Checks whether the player won, lost a life, or lost the game.
     */
    private void checkForGameEnd() {
        float ballHeight = ball.getCenter().y();
        if (bricksOnScreen.value() <= ZERO_COUNT
                || userInputListener.isKeyPressed(KeyEvent.VK_W)) {
            handleGameOver(WIN_PROMPT);
        } else if (ballHeight > windowDimensions.y()) {
            livesCount.decrement();
            if (livesCount.value() <= ZERO_COUNT) {
                handleGameOver(LOSE_PROMPT);
            } else {
                resetBall();
            }
        }
    }

    /*
     * Prompts the player to restart or exit the game.
     */
    private void handleGameOver(String prompt) {
        if (windowController.openYesNoDialog(prompt)) {
            windowController.resetGame();
        } else {
            windowController.closeWindow();
        }
    }

    /*
     * Creates the main ball and places it at the center of the window.
     */
    private void createBall(ImageReader imageReader, SoundReader soundReader) {
        Renderable ballImage = imageReader.readImage(BALL_IMAGE_PATH, true);
        Sound collisionSound = soundReader.readSound(BALL_SOUND_PATH);
        ball = new Ball(Vector2.ZERO, new Vector2(BALL_DIAMETER, BALL_DIAMETER),
                ballImage, collisionSound);
        gameObjects().addGameObject(ball);
        resetBall();
    }

    /*
     * Centers the main ball and assigns a random diagonal initial velocity.
     */
    private void resetBall() {
        ball.setCenter(windowDimensions.mult(SCREEN_CENTER_FACTOR));
        float ballVelocityX = BALL_SPEED;
        float ballVelocityY = BALL_SPEED;
        if (random.nextBoolean()) {
            ballVelocityX *= DIRECTION_FLIP_FACTOR;
        }
        if (random.nextBoolean()) {
            ballVelocityY *= DIRECTION_FLIP_FACTOR;
        }
        ball.setVelocity(new Vector2(ballVelocityX, ballVelocityY));
    }

    /*
     * Creates the main player paddle at the bottom center of the screen.
     */
    private void createPaddle(ImageReader imageReader, UserInputListener inputListener) {
        Renderable paddleImage = imageReader.readImage(PADDLE_IMAGE_PATH, false);
        GameObject paddle = new Paddle(
                Vector2.ZERO,
                new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),
                paddleImage,
                inputListener,
                windowDimensions,
                BORDER_WIDTH);
        paddle.setCenter(new Vector2(
                windowDimensions.x() * SCREEN_CENTER_FACTOR,
                windowDimensions.y() - PADDLE_BOTTOM_OFFSET));
        paddle.setTag(Paddle.MAIN_PADDLE_TAG);
        gameObjects().addGameObject(paddle);
    }

    /*
     * Creates the left, right, and top static screen borders.
     */
    private void createBorders() {
        GameObject leftBorder = new GameObject(
                Vector2.ZERO,
                new Vector2(BORDER_WIDTH, windowDimensions.y()),
                BORDER_RENDERABLE);
        GameObject rightBorder = new GameObject(
                new Vector2(windowDimensions.x() - BORDER_WIDTH, TOP_EDGE_Y),
                new Vector2(BORDER_WIDTH, windowDimensions.y()),
                BORDER_RENDERABLE);
        GameObject topBorder = new GameObject(
                Vector2.ZERO,
                new Vector2(windowDimensions.x(), BORDER_WIDTH),
                BORDER_RENDERABLE);

        gameObjects().addGameObject(leftBorder, Layer.STATIC_OBJECTS);
        gameObjects().addGameObject(rightBorder, Layer.STATIC_OBJECTS);
        gameObjects().addGameObject(topBorder, Layer.STATIC_OBJECTS);
    }

    /*
     * Populates the grid of bricks with randomly assigned collision strategies.
     */
    private void createBricks(ImageReader imageReader, SoundReader soundReader,
                              UserInputListener inputListener) {
        Renderable brickImage = imageReader.readImage(BRICK_IMAGE_PATH, true);
        float totalSpacing = (bricksPerRow + EXTRA_EDGE_SPACE) * BRICK_SPACING;
        float availableWidth = windowDimensions.x()
                - VERTICAL_BORDERS_COUNT * BORDER_WIDTH - totalSpacing;
        float brickWidth = availableWidth / bricksPerRow;

        Brick[][] bricksGrid = new Brick[brickRows][bricksPerRow];
        BrickStrategyFactory strategyFactory = createStrategyFactory(
                imageReader, soundReader, inputListener, bricksGrid);

        for (int row = 0; row < brickRows; row++) {
            float brickY = TOP_BRICKS_OFFSET + row * (BRICK_HEIGHT + BRICK_SPACING);
            for (int column = 0; column < bricksPerRow; column++) {
                float brickX = BORDER_WIDTH + BRICK_SPACING
                        + column * (brickWidth + BRICK_SPACING);
                CollisionStrategy strategy = strategyFactory.getStrategy();
                Brick brick = new Brick(
                        new Vector2(brickX, brickY),
                        new Vector2(brickWidth, BRICK_HEIGHT),
                        brickImage,
                        row,
                        column,
                        strategy);
                bricksGrid[row][column] = brick;
                gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
                bricksOnScreen.increment();
            }
        }
    }

    /*
     * Constructs the factory responsible for generating brick collision strategies.
     */
    private BrickStrategyFactory createStrategyFactory(ImageReader imageReader,
                                                       SoundReader soundReader,
                                                       UserInputListener inputListener,
                                                       Brick[][] bricksGrid) {
        return new BrickStrategyFactory(
                gameObjects(),
                bricksOnScreen,
                livesCount,
                imageReader,
                soundReader,
                inputListener,
                windowDimensions,
                bricksGrid,
                BALL_DIAMETER,
                BALL_SPEED,
                new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),
                HEART_DIMENSIONS,
                MAX_LIVES,
                BORDER_WIDTH);
    }

    /*
     * Creates the numeric and graphic life counters at the bottom-left corner of the screen.
     */
    private void createUiCounters(ImageReader imageReader) {
        float uiTopY = windowDimensions.y() - UI_BOTTOM_OFFSET;
        GameObject heartsNumber = new HeartsNumber(
                new Vector2(BORDER_WIDTH + NUMERIC_COUNTER_LEFT_OFFSET, uiTopY),
                NUMERIC_COUNTER_DIMENSIONS,
                livesCount);
        gameObjects().addGameObject(heartsNumber, Layer.UI);

        Renderable heartImage = imageReader.readImage(HEART_IMAGE_PATH, true);
        GameObject heartsBar = new HeartsBar(
                new Vector2(BORDER_WIDTH + HEARTS_BAR_LEFT_OFFSET, uiTopY),
                HEART_DIMENSIONS,
                heartImage,
                MAX_LIVES,
                gameObjects(),
                livesCount);
        gameObjects().addGameObject(heartsBar, Layer.UI);
    }
}