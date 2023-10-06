import javafx.geometry.Point2D
import javafx.geometry.Rectangle2D
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

class Player(
    var world: World,
    var position: Point2D,
    var size: Point2D,
    var velocity: Point2D
) {
    var isOnGround: Boolean = true;
    var jumped: Boolean = false;
    var felt: Boolean = false
    var direction: Direction = Direction.STOP
    var currentAnimation: Int = 0
    var currentGround: Rectangle2D = world.objects[0]

    val spriteSheetList: List<SpriteSheetValue> = mutableListOf(
        SpriteSheetValue(0,0,10,16),
        SpriteSheetValue(17,0, 10,16),
        SpriteSheetValue(35,0,10,16),
        SpriteSheetValue(53,0,10,16),
        SpriteSheetValue(118, 0, 10,16),
        SpriteSheetValue(101,0,10,16),
        SpriteSheetValue(83,0,10,16),
        SpriteSheetValue(65,0,10,16)
    )


    fun simulate(timedelta: Double,world: World){
        println(position)
        position = position.add(Point2D((velocity.x * timedelta) ,0.0))
        position = Point2D((position.x + world.width) % world.width, (position.y + world.height) % world.height)

    }




    fun draw(gc: GraphicsContext){
        when(direction){
            Direction.RIGHT -> {
                when ((position.x % 40).toInt()) {
                    in 0..10 -> {
                        gc.drawImage(SPRITESHEET, spriteSheetList[0].sourceX.toDouble(), spriteSheetList[0].sourceY.toDouble(), spriteSheetList[0].sourceWidth.toDouble(), spriteSheetList[0].sourceHeight.toDouble(), position.x, position.y, size.x, size.y)
                        currentAnimation = 0
                    }

                    in 11..20 -> {
                        gc.drawImage(SPRITESHEET, spriteSheetList[1].sourceX.toDouble(), spriteSheetList[1].sourceY.toDouble(), spriteSheetList[1].sourceWidth.toDouble(), spriteSheetList[1].sourceHeight.toDouble(), position.x, position.y, size.x, size.y)
                        currentAnimation = 1
                    }

                    in 21..30 -> {
                        gc.drawImage(SPRITESHEET, spriteSheetList[2].sourceX.toDouble(), spriteSheetList[2].sourceY.toDouble(), spriteSheetList[2].sourceWidth.toDouble(), spriteSheetList[2].sourceHeight.toDouble(), position.x, position.y, size.x, size.y)
                        currentAnimation = 2
                    }

                    else -> {
                        gc.drawImage(SPRITESHEET, spriteSheetList[3].sourceX.toDouble(), spriteSheetList[3].sourceY.toDouble(), spriteSheetList[3].sourceWidth.toDouble(), spriteSheetList[3].sourceHeight.toDouble(), position.x, position.y, size.x, size.y)
                        currentAnimation = 3
                    }
                }
            }
            Direction.LEFT -> {
                when ((position.x % 40).toInt()) {
                    in 0..10 -> {
                        gc.drawImage(SPRITESHEET, spriteSheetList[4].sourceX.toDouble(), spriteSheetList[4].sourceY.toDouble(), spriteSheetList[4].sourceWidth.toDouble(), spriteSheetList[4].sourceHeight.toDouble(), position.x, position.y, size.x, size.y)
                        currentAnimation = 4
                    }

                    in 11..20 -> {
                        gc.drawImage(SPRITESHEET, spriteSheetList[5].sourceX.toDouble(), spriteSheetList[5].sourceY.toDouble(), spriteSheetList[5].sourceWidth.toDouble(), spriteSheetList[5].sourceHeight.toDouble(), position.x, position.y, size.x, size.y)
                        currentAnimation = 5
                    }

                    in 21..30 -> {
                        gc.drawImage(SPRITESHEET, spriteSheetList[6].sourceX.toDouble(), spriteSheetList[6].sourceY.toDouble(), spriteSheetList[6].sourceWidth.toDouble(), spriteSheetList[6].sourceHeight.toDouble(), position.x, position.y, size.x, size.y)
                        currentAnimation = 6
                    }

                    else -> {
                        gc.drawImage(SPRITESHEET, spriteSheetList[7].sourceX.toDouble(), spriteSheetList[7].sourceY.toDouble(), spriteSheetList[7].sourceWidth.toDouble(), spriteSheetList[7].sourceHeight.toDouble(), position.x, position.y, size.x, size.y)
                        currentAnimation = 7
                    }
                }

            }
            Direction.AIR -> {
                gc.drawImage(SPRITESHEET, spriteSheetList[currentAnimation].sourceX.toDouble(), spriteSheetList[currentAnimation].sourceY.toDouble(), spriteSheetList[currentAnimation].sourceWidth.toDouble(), spriteSheetList[currentAnimation].sourceHeight.toDouble(), position.x, position.y, size.x, size.y)
            }
            Direction.STOP -> {
                gc.drawImage(SPRITESHEET, spriteSheetList[currentAnimation].sourceX.toDouble(), spriteSheetList[currentAnimation].sourceY.toDouble(), spriteSheetList[currentAnimation].sourceWidth.toDouble(), spriteSheetList[currentAnimation].sourceHeight.toDouble(), position.x, position.y, size.x, size.y)
            }
        }
        val boundingBox = getBoundingBox()
        gc.fill = Color.CYAN
        gc.strokeRect(boundingBox.minX,boundingBox.minY,boundingBox.width,boundingBox.height)
    }

    fun getBoundingBox(): Rectangle2D{
        return Rectangle2D(position.x,position.y,size.x,size.y)
    }

    fun left(){
        this.velocity = velocity.add(-200.0,0.0)
    }
    fun right(){
        this.velocity = velocity.add(200.0,0.0)
    }
    fun stop(){
        this.velocity = Point2D(0.0,0.0)
    }


    fun collision(){
        if (getBoundingBox().intersects(currentGround)) {
            this.position = Point2D(position.x, currentGround.minY-PLAYER_HEIGHT)
            this.jumped = false
            this.velocity = Point2D(velocity.x,0.0)
            if (direction == Direction.STOP){
                stop()
            }
        }
    }
    fun jump(gravity: Double,timedelta: Double){
        if (jumped) {
            this.velocity = velocity.add(Point2D(0.0, gravity*timedelta))
        }
    }
    fun checkfall(gravity: Double,timedelta: Double){
        if (!checkGround() && !jumped) {
            ground()
            this.velocity = Point2D(0.0,100.0)
            felt = true
        }
        if (felt){
            if (getBoundingBox().maxY < currentGround.minY){
                this.velocity = velocity.add(Point2D(0.0, gravity*timedelta))
            } else {
                felt = false
            }
        }
    }


    fun move(timedelta: Double,gravity: Double){
        if (position.x < 0 && (direction == Direction.LEFT || direction == Direction.STOP) ) {
            stop()
            position = Point2D(0.1,position.y)
            jump(gravity, timedelta)
        }else if(position.x+PLAYER_WIDTH > world.width && (direction == Direction.RIGHT || direction == Direction.STOP)){
            stop()
            position = Point2D(WINDOW_WIDTH-PLAYER_WIDTH-0.1,position.y)
            jump(gravity, timedelta)
        } else{
            position = position.add(velocity.x * timedelta,velocity.y*timedelta)
            jump(gravity, timedelta)
            checkfall(gravity,timedelta)
        }
    }


    fun ground(){
        val boundingBox = getBoundingBox()
        for (i in world.objects.indices){
//            println("Player: $boundingBox")
//            println("World $i: ${world.objects[i]}")
//            println("World $i my custom: minX:${world.objects[i].minX-PLAYER_WIDTH} maxX:${world.objects[i].maxX+PLAYER_WIDTH}")
            if (
                boundingBox.minX > world.objects[i].minX-PLAYER_WIDTH &&
                boundingBox.maxX < world.objects[i].maxX+PLAYER_WIDTH &&
                boundingBox.maxY <= world.objects[i].minY
                ){
                currentGround = world.objects[i]
//                println("passed")
            }
        }
    }
    fun checkGround(): Boolean{
        val boundingBox = getBoundingBox()
        if (
            boundingBox.minX > currentGround.minX-PLAYER_WIDTH &&
            boundingBox.maxX < currentGround.maxX+PLAYER_WIDTH &&
            boundingBox.maxY-PLAYER_WIDTH/2 <= currentGround.minY
        ){
            println(boundingBox)
            println(currentGround)
            return true
        } else {
            return false
        }
    }

}