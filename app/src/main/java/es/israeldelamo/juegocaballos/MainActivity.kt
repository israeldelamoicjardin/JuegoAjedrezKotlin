package es.israeldelamo.juegocaballos

import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TableRow
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat


/**
 * Punto de entrada de la aplicación.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //inicializar juego
        inicializarJuego()
        //posicionamiento aleatorio del caballo
        setFirstPosition()


        }


    /**
     * Para que el juego siempre sea diferente se posiciona inicialmente el caballo en un sitio o
     * en otro de manera aleatorioa
     */
    private fun setFirstPosition() {
        var x=0
        var y = 0
        x = (0..7).random()
        y = (0..7).random()
        //con esa posición aleatoria, pinta el caballo
        selectCell(x,y)
    }

    /**
     * Selecciona una celda dada la posición y pinta el caballo en ella
     * nunca debe ser mayor que el rano 0 ..7
     * @param x fila
     * @param y columna
     */
    private fun selectCell(x: Int, y: Int) {
        pintarCaballoEnCelda(x, y, "selected_cell" )
    }


    /**
     * Dibuja el camballo en la celda dada
     * @param x
     * @param y
     * @param color
     */
    private fun pintarCaballoEnCelda(x: Int, y: Int, color: String) {
        var iv: ImageView = findViewById(resources.getIdentifier("ivc$x$y", "id", packageName))
        //cambiamos el color de fondo, se referencia mediate ContextCompact
        iv.setBackgroundColor(ContextCompat.getColor(this, resources.getIdentifier(color, "color", packageName)))
        //pintamos el caballo directamente asignando el recurso de imagen
        iv.setImageResource(R.drawable.caballo)
    }


    /**
     * Función de inicialización del juego
     */
   private fun inicializarJuego(){
        //ajustamos el tamaño del tablero
        setSizeBoard()
       //ocultamos el mensaje de vidas y nivel
       hide_message()

   }


    /**
     * Oculta el layout de las vidas y el nivel
     */
    private fun hide_message() {
       var lyMessage: LinearLayout = findViewById(R.id.llMensaje)
        lyMessage.visibility = View.INVISIBLE
    }

    /**
     * Ajusta el tamaño del tablero
     */
    private fun setSizeBoard() {
        //recorremos todas las celdas y reasignamos altura y anchura
        var iv: ImageView
        //leo las características de la pantalla
        val display =  windowManager.defaultDisplay
        //creo una variable para guardar su tamaño
        val size = Point()
        //guardo el tamaño de la pantalla
        display.getSize(size)
        //el ancho total de la pantalla es del punto size la parte x
        val width = size.x

        // el ancho expresado en dp es el resultado del ancho entre la densidad de la pantalla del telefono
        var width_dp = (width / getResources().getDisplayMetrics().density)

        // añadimos un tamaño para el margen lateral
        var lateralMarginDP = 0

        val widthCell = (width_dp - lateralMarginDP) / 8
        //como altura y anchura son iguales, solo necesitamos una
        val heightCell = widthCell


        for (i in 0..7){
            for (j in 0..7){
                iv = findViewById(resources.getIdentifier("ivc$i$j", "id", packageName))
                if (iv != null){
                    //ahora asignamos los nuevos anchos y largos a cada celda
                    var height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, heightCell, getResources().getDisplayMetrics()).toInt()
                    var width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, widthCell, getResources().getDisplayMetrics()).toInt()

                    iv.setLayoutParams(TableRow.LayoutParams(width, height))
                } else {
                    Log.e("MainActivity", "Estas accediendo a un null")
                }


            }


        }
    }


}
