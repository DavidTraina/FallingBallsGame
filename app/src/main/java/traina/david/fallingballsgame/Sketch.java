package traina.david.fallingballsgame;

import java.util.ArrayList;
import java.util.LinkedList;

import processing.core.PApplet;

class Sketch extends PApplet {

    private int lives = 3;
    private float gravity = 9; //added to ySpeed every second
    private int score = 0;
    private final int MAX_BALLS = 90;
    private final GameActivity gameActivity;
    private final ArrayList<Ball> balls = new ArrayList<>();
    private final int difficulty;
    private int nextBallTime = 0;
    private final int FRAME_RATE = 60;

    Sketch(int difficulty) {
        this.gameActivity = (GameActivity) getActivity();
        this.difficulty = difficulty;
    }

    public void settings() {
        fullScreen();
        smooth(4);
    }

    public void setup() {
        frameRate(FRAME_RATE);
        //noStroke();
        ellipseMode(RADIUS); //centerX, centerY, width/2, height/2
        rectMode(CENTER); //centerX, centerY, width, height
        textMode(CENTER);
        textAlign(CENTER);
        strokeWeight(2);
        textSize(60);
    }

    public void draw() {
        //Note that the origin is located in the top-left corner and increases on both axis
        background(255);
        drawStats();
        addNewBall();
        if (random(1) < 1 / (20 * frameRate)) { // Geometric distribution mean = 1/p. Want a bad ball about
            // every 20s so mean = 20s * frameRate -> p = 1 / (20 * frameRate).
            balls.add(new BadBall());
        }

        LinkedList<Ball> deadBalls = new LinkedList<>();
        for (Ball ball : balls) {
            if (ball.isDead()) {
                if (ball instanceof BadBall) {
                    lives++;
                }
                deadBalls.addLast(ball);
            } else {
                ball.move();
                ball.drawBall();
            }
        }
        lives -= deadBalls.size();
        checkGameOver();
        balls.removeAll(deadBalls);
    }

    public void mousePressed() {
        float tapX = mouseX;
        float tapY = mouseY;
        for (Ball ball : balls) {
            ball.onTap(tapX, tapY);
        }
    }

    /**
     * Draw the lives and the current score on the screen.
     */
    private void drawStats() {
        fill(0, 160, 118);
        text("Lives: " + lives, (float) (1.0 / 4) * width, height - 50);
        fill(45, 219, 247);
        text("Score: " + score, (float) (3.0 / 4) * width, height - 50);
    }

    private void checkGameOver() {
        if (lives < 0) {
            gameActivity.switchToGameOver();
        }
    }

    private void addNewBall() {
        if (millis() >= nextBallTime || balls.isEmpty()) {
            if (random(1) < 0.15) { // Want a double ball 15% of the time.
                balls.add(new DoubleBall());
            } else {
                balls.add(new Ball());
            }
            int TIME_BETWEEN_NEW_BALLS = 7000; // Time in ms between new balls scaled by difficulty.
            nextBallTime = millis() + ((3 - difficulty) * TIME_BETWEEN_NEW_BALLS);
        }
    }

    class Ball {
        float x = (float) (width / 2.0);
        float y = -45;
        float xSpeed = 0;
        float ySpeed = -4;
        float radius = 45;
        final float X_SPEED_SCALAR = (float) 0.14;
        final int[] ballColor = {round(random(255)), round(random(255)),
                round(random(255))};

        void onTap(float tapX, float tapY) {
            final float TAP_ERROR_PERCENTAGE = (float) 1.2;
            if (dist(tapX, tapY, x, y) <= radius * TAP_ERROR_PERCENTAGE) {
                xSpeed = X_SPEED_SCALAR * (x - mouseX); // Tapping off-center causes ball to move in
                // direction opposite to tap.
                float extraBoost = -height / (2 * frameRate);
                if (ySpeed <= 0) { // Going upward.
                    ySpeed += extraBoost;
                } else { // Going downward.
                    ySpeed *= -1;
                }
                score++;
            }
        }

        boolean isDead() {
            return y - radius > height;
        }

        void move() {
            float terminalVelocity = height / (float) (1.25 * frameRate); //Ball takes 1.25s to
            // traverse screen.
            ySpeed += gravity / frameRate;
            if (ySpeed > terminalVelocity) {
                ySpeed = terminalVelocity;
            }
            x += xSpeed;
            y += ySpeed;
            if ((x - radius) <= 0) { // Ball hits left wall.
                xSpeed = abs(xSpeed);
            } else if ((x + radius) >= width) { // Ball hits right wall.
                xSpeed = -abs(xSpeed);
            }
        }

        void drawBall() {
            if ((y + radius) > 0) { // Ball on screen
                fill(ballColor[0], ballColor[1], ballColor[2]);
                ellipse(x, y, radius, radius);
            } else {
                fill(ballColor[0], ballColor[1], ballColor[2], 200);
                triangle(x, 0, x + 10, 25, x - 10, 25); // Vertices
            }
        }
    }

    class BadBall extends Ball {
        @Override
        void drawBall() {
            for (int i = 0; i < ballColor.length; i++) {
                ballColor[i] = round(random(255));
            }
            super.drawBall();
        }

        @Override
        void onTap(float tapX, float tapY) {
            final float TAP_ERROR_PERCENTAGE = (float) 1.2;
            if (dist(tapX, tapY, x, y) <= radius * TAP_ERROR_PERCENTAGE) {
                xSpeed = X_SPEED_SCALAR * (x - mouseX); // Tapping off-center causes ball to move in
                // direction opposite to tap.
                float extraBoost = -height / (2 * frameRate);
                if (ySpeed <= 0) { // Going upward.
                    ySpeed += extraBoost;
                } else { // Going downward.
                    ySpeed *= -1;
                }
                lives--;
            }
        }
    }

    class DoubleBall extends Ball {
        DoubleBall() {
            for (int i = 0; i < ballColor.length; i++) {
                ballColor[i] = 0;
            }
        }

        //TODO: fix all balls getting 2x pts
        @Override
        void onTap(float tapX, float tapY) {
            super.onTap(tapX, tapY);
            score++;
        }
    }
}


