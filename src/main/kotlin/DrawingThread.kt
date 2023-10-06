package lab

import Direction
import GROUND_HEIGHT
import PLAYER_HEIGHT
import PLAYER_WIDTH
import Player
import World
import javafx.animation.AnimationTimer
import javafx.geometry.Point2D
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.paint.Color

class DrawingThread(private var canvas: Canvas) : AnimationTimer() {
    private var gc: GraphicsContext = canvas.getGraphicsContext2D()
    private val FPS = 100;
    private var lasttime: Long = -1
    private var GRAVITY = 600.0
    var JUMP_FORCE:Double = 300.0
    private var world: World
    private var player: Player


    init {
        gc = canvas.getGraphicsContext2D()
        this.world = World(
            canvas.width,
            canvas.height
        )
        this.player = Player(
            position = Point2D(20.0, canvas.height-PLAYER_HEIGHT-GROUND_HEIGHT),
            velocity = Point2D(0.0, 0.0),
            size = Point2D(PLAYER_WIDTH, PLAYER_HEIGHT),
            world = this.world
        )
        this.world.player = this.player

        canvas.setOnKeyPressed {
            movement(it,true)
        }
        canvas.setOnKeyReleased {
            movement(it, false)
        }
        canvas.isFocusTraversable = true
        canvas.requestFocus()
    }

    private fun movement(ke: KeyEvent,pressed: Boolean){
        if (pressed){
            when (ke.code){
                KeyCode.A -> {
                    if (!player.jumped && player.direction != Direction.LEFT){
                        player.left()
                        player.direction = Direction.LEFT
                    }
                }
                KeyCode.D -> {
                    if (!player.jumped && player.direction != Direction.RIGHT){
                        player.right()
                        player.direction = Direction.RIGHT
                    }
                }
                KeyCode.SPACE -> {
                    if (!player.jumped){
                        player.velocity = player.velocity.add(Point2D(0.0,-JUMP_FORCE))
                        player.jumped = true
                    }
                }
                else -> {

                }
            }

        } else {
            if (!player.jumped ){
                player.stop()
            }
            when(ke.code){
                KeyCode.A, KeyCode.D -> {
                    player.direction = Direction.STOP
                }
                else -> {

                }
            }
        }
    }




    override fun handle(now: Long) {
        val deltaT: Double = (now - lasttime) / 1e9
        if (deltaT >= 1.0 / FPS) {
            gc.clearRect(0.0,0.0,world.width,world.height)
            gc.fill = Color.BLACK
            gc.fillRect(0.0,0.0,world.width,world.height)
            world.draw(gc)
            if (lasttime > 0) {
                player.collision()
                player.ground()
                player.move(deltaT,GRAVITY)


            }
            lasttime = now
        }
    }


}