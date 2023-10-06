
import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.stage.Stage
import lab.DrawingThread


class Main: Application() {
    private lateinit var canvas: Canvas
    private lateinit var timer: DrawingThread
    override fun start(primaryStage: Stage?) {
        try {
            //Construct a main window with a canvas.
            val root = Group()
            canvas = Canvas(WINDOW_WIDTH, WINDOW_HEIGHT)
            root.getChildren().add(canvas)
            val scene = Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT)
            scene.stylesheets.add(javaClass.getResource("application.css").toExternalForm())
            primaryStage!!.setScene(scene)
            primaryStage.resizableProperty().set(false)
            primaryStage.title = "Manic Miner"
            primaryStage.show()

            timer = DrawingThread(canvas)
            timer.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun localMain() {
        launch()
    }

}

fun main() {
    val main: Main = Main()
    main.localMain()
}
