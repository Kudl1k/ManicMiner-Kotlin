
import javafx.geometry.Point2D
import javafx.geometry.Rectangle2D
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color


class World(
    val width: Double,
    val height: Double
) {
    lateinit var player: Player
    var boundingBoxes: List<Rectangle2D> = emptyList();



    var ground: Rectangle2D = Rectangle2D(
        0.0,
        height-GROUND_HEIGHT,
        width,
        GROUND_HEIGHT
    )

    var platform1: Rectangle2D = Rectangle2D(
        100.0,
        height-GROUND_HEIGHT-PLAYER_HEIGHT,
        width/2,
        GROUND_HEIGHT
    )


    var objects = mutableListOf<Rectangle2D>(
        ground,
        platform1
    )


    fun getCanvasPoint(worldPoint: Point2D, heightOfEntity: Double): Point2D {
        return Point2D(worldPoint.x, height - worldPoint.y - heightOfEntity)
    }

    fun draw(gc: GraphicsContext) {
        drawGround(gc)
        player.draw(gc)
    }
    fun simulate(timeDelta: Double) {
        player.simulate(timeDelta, world = this)
    }

    fun drawGround(gc: GraphicsContext){
        gc.fill = Color.RED
        for (i in objects.indices){
            gc.fillRect(objects[i].minX,objects[i].minY,objects[i].width,objects[i].height)
        }
    }



    fun checkCollisions() {
        // assuming ground and objects are of type `Rectangle2D`
        if (player.currentGround.intersects(player.getBoundingBox())){
            this.player.isOnGround = true
        } else {
            this.player.isOnGround = false
        }

        // here you can also check for collisions with other objects
    }

}