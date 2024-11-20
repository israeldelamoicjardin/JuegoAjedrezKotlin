package es.israeldelamo.juegocaballos

import android.annotation.SuppressLint
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat


/**
 * Punto de entrada de la aplicación.
 */
class MainActivity : ComponentActivity() {

    /**
     * El color para la celda negra
     */
    private var colorCeldaNegra = "black_cell"

    /**
     * el color para la celda blanca
     */
    private var colorCeldaBlanca = "white_cell"

    /**
     * Anchura del bonus, de su barra que crece
     */
    private var width_bonus = 0

    /**
     * Contado hasta el bonus
     */
    private var bonus = 0

    /**
     *   las que pueden ser en catidad de movimientos
     */

    private var numeroOpcionesDisponibles = 0

    /**
     * los movimientos del usuario hasta terminar
     */
    private var moves = 64

    /**
     * los movimientos del usuario hasta terminar en este nivel
     */
    private var lvlMoves = 64

    /**
     * Movimientos requeridos hasta recibir un premio
     */
    private var movesRequired = 4


    /**
     * Mira si es necesario o no comprobar el movimiento, si tenemos bonus podemos hacer saltos directos
     */
    private var checkMovement = true

    /**
     * Guarda las posiciones x e Y de manera temporal
     */
    private var cellSelected_x = 0
    private var cellSelected_y = 0

    /**
     * Contiene la matriz del tablero de ajedrez
     */
    private lateinit var tablero: Array<IntArray>

    /**
     * Punto de entrada en la aplicación
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //inicializar juego
        inicializarJuego()
        //resetear el tablero
        resetTablero()

        //posicionamiento aleatorio del caballo
        setFirstPosition()


        }

    /**
     * Deveulve el tablero al estado inicial
     * cero para ningún caballo
     * uno para hay un caballo
     * dos es un bonus
     * nueve es una opción de movimiento
     */
    private fun resetTablero() {
        tablero = arrayOf(
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            )
        }

    /**
     * Este evento es llamado desde el layout cuando se pulsa una celda
     * @param v vista que ha generado el evento
     */
    fun checkCellCLicked(v: View){
        //leemos que etiquita tiene para averiguar su posición
        var posicion = v.getTag().toString()
        Log.d("MainActivity", "Has pulsado la celda $posicion")
        // de ese tag vamos a sacar el primer número como x
        // y de ese tag vamos a sacar el segundo número como y
        val x = posicion.substring(1,2).toInt()
        val y = posicion.substring(2,3).toInt()

        //habra que indicar que se ha pulsado
        checkCell(x,y)

    }


    /**
     * Mira si es un movimiento en l
     * Mira si la celda esta pulsada anteriormente
     * si tenía un uno en esa posocion devuelve true
     * su había un cero en esa posición devuelve un false
     */
    private fun checkCell(x: Int, y: Int) {
        var checkTrue = true
       if (checkMovement){

           val dif_x = x -cellSelected_x
           val dif_y = y -cellSelected_y


           checkTrue = false

           //comprueba el movimiento en L para el caballo, con valor absoutlo era más corto
           // computacionalmente más lento
           if ( dif_x == 1 && dif_y == 2) checkTrue = true
           if ( dif_x == 1 && dif_y == -2) checkTrue = true
           if ( dif_x == 2 && dif_y == 1) checkTrue = true
           if ( dif_x == 2 && dif_y == -1) checkTrue = true
           if ( dif_x == -1 && dif_y == 2) checkTrue = true
           if ( dif_x == -1 && dif_y == -2) checkTrue = true
           if ( dif_x == -2 && dif_y == 1) checkTrue = true
           if ( dif_x == -2 && dif_y == -1) checkTrue = true
       }
       else
       {
           if (tablero[x][y] != 1) {
               //nos vale el movimiento
               bonus--
               var tvBonusDato = findViewById<TextView>(R.id.bonusDato)
               tvBonusDato.text = "  +" + bonus.toString()
              if (bonus==0)   tvBonusDato.text =""
           }
       }
        if (tablero[x][y] == 1) checkTrue = false
        if (checkTrue) selectCell(x,y)


    }


    /**
     * Para que el juego siempre sea diferente se posiciona inicialmente el caballo en un sitio o
     * en otro de manera aleatorioa
     */
    private fun setFirstPosition() {

        val x = (0..7).random()
        val y = (0..7).random()

        //recordamos las que hemos pintado
        cellSelected_x = x
        cellSelected_y = y

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
        // ha pulsado, una posición menos para llenar
        moves--
        val tv  =  findViewById<TextView>(R.id.movimientosDatos)
        tv.text = moves.toString()

        //dibuja la barra horizontal de bonus en función de los puntos que falten para nuevo bonus
        refrescarBarraBonus()


        // hemos caido en un bonus?
        if (tablero[x][y] == 2) {
            bonus++
            val tvBonusDato = findViewById<TextView>(R.id.bonusDato)
            //concatena la info de bonus
            tvBonusDato.text = "  +" + bonus.toString()
        }




        //señalizamos en la matriz que en esa posición hay un caballo
        tablero[x][y] = 1


        //como sé cual era la celda anterior pues la he guardado en setFirtPosition antes de llamar
        // la pinto como usada
        pintarCaballoEnCelda(cellSelected_x, cellSelected_y, "previous_cell" )
        //las nueva posición anterior sera justo la que vamos a pintar ahora
        cellSelected_x = x
        cellSelected_y = y

        //borramos las opciones posibles anteriores
        borrarOpcionesAntiguas()

        //ahora pinto la nueva celda
        pintarCaballoEnCelda(x, y, "selected_cell" )

        //despues de pintar que vuelva a fijar  checkMovemen a true
        checkMovement = true
        // revisamos las posibles opciones de movimiento
        checkPosiblesOpciones(x,y)

        //si aún quedan movimientos por hacer
        if (moves > 0) {
                //mirar si hay premio
            checkNuevoBonus()
            //mirar si es fin de partida
           checkGameOver(x,y)
        }
        else  mostrarMensaje( "Has ganado","Muy bien",false)

    }

    /**
     * En función de los puntos que faltan para bonus, hace crecer la barra horizontal de bonus
     */
    private fun refrescarBarraBonus() {
            //calculamos la anchura proporcional
        var moves_done = lvlMoves - moves
        var bonus_done = moves_done / movesRequired
        var moves_rest = movesRequired * (bonus_done)
        var bonus_grow = moves_done - moves_rest


        //voy a trabajar sobre este view de vNuevoBonus
        var v = findViewById<View>(R.id.vNuevoBonus)
        //el nuevo ancho ser calcula´ra así
        var widthBonus = ((width_bonus/movesRequired) * bonus_grow).toFloat()
        // recojo el tamaño del view que usaremos como barra
        var height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, getResources().getDisplayMetrics()).toInt()
        var width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, widthBonus, getResources().getDisplayMetrics()).toInt()
       //cambiamos el parámetro de su ancho y largo, dibujándolo efectivamente
        v.setLayoutParams(TableRow.LayoutParams(width, height))
    }

    /**
     * Pinta un bonus en la celda que le digas
     */
    private fun pintarBonusCell(bonusCellX: Int, bonusCellY: Int) {
            val iv : ImageView
            iv = findViewById(resources.getIdentifier("ivc$bonusCellX$bonusCellY", "id", packageName))
        //sobre esa celda iv pintamos el bonus
        iv.setImageResource(R.drawable.bonus)

    }

    /**
     * Tienes bonus, puedes seguir
     */
    private fun checkNuevoBonus() {
       if (moves%movesRequired == 0) { //en bloques de cada 4
            //creo un nuevo punto
           var bonusCellX = 0
           var bonusCellY = 0

           var bonusCell = false
           while (bonusCell == false){
               //hasta que no encuentre una celda para bonus buena
               bonusCellX = (0..7).random()
               bonusCellY = (0..7).random()

               //como esta libre, dejamos de buscar
                if(tablero[bonusCellX][bonusCellY] == 0) bonusCell = true

           }
           //le damos el valor de bonus, recuerda 0 libre, 9 opcion y 2 bonus
           tablero[bonusCellX][bonusCellY] = 2
           //pintamos el bonus
           pintarBonusCell(bonusCellX, bonusCellY)

       }
    }

    /**
     * Has ganado la partida, enhorabuena
     */
    private fun checkPartidaGanada() {
        TODO("Not yet implemented")
    }


    /**
     * Desde una posición dada comprueba si aún quedan movimientos posibles
     * en función de ello es gameOver o no
     */
    private fun checkGameOver(x: Int, y: Int) {
        if (numeroOpcionesDisponibles == 0) {
          //  //estas en gameOver, a ver si te quedan bonus
            if (bonus== 0) {
                //también estas en bonus cero
                mostrarMensaje( "FIN DE JUEGO","Paquetón",true) //le pasamos el recurso del string finDeJuego
            } else {
                //como si tiene bonus puede hacer un salto directo
                checkMovement = false
                paintAllOptions()

            }
        }
    }

    /**
     * Dibuja todas las opciones que no esten libres
     */

    private fun paintAllOptions() {
        //recorremos el tablero completo y en las posiciónes cero les damos un resalto
        for (i in 0..7){
            for (j in 0..7) {
                //todo lo que no sea 1, es decir, bonus, opción o cero
                if (tablero[i][j] !=1) refrescaOpciones(i, j)
                if (tablero[i][j] ==0) tablero[i][j]=9
            }
        }
    }

    /**
     * Muestra la pantalla de información para nuevo nivel o de game over
     * @param mensaje la frase a mostrar
     * @param subtexto segunda frase a mostrar
     * @param esGameOver si es true, es que es mensaje de gameOver, si es false, es nextleveñ
     */
    private fun mostrarMensaje(mensaje: String, subtexto: String, esGameOver:Boolean) {
        //busco el layout de mensaje
        val llMensaje = findViewById<LinearLayout>(R.id.llMensaje)

        //busco los textviews de ese panel
        val tvMessage = findViewById<TextView>(R.id.tvMessage)
        val tvTitleMessage = findViewById<TextView>(R.id.tvTitleMesage)
        val tvScore = findViewById<TextView>(R.id.tvScore)

        llMensaje.visibility = View.VISIBLE

        // si es gameover que se vean sus puntos si no el tiempo del nivel
        val puntos : String
        if (esGameOver) {
            puntos = "Puntos: " + (lvlMoves - moves) + "/" + lvlMoves

        } else {
            val tiempo = "0:00"
            puntos = "Tiempo: " + tiempo
        }
        //llenamos los textos
        tvMessage.text = mensaje
        tvTitleMessage.text = subtexto
        tvScore.text = puntos

    }

    /**
     * Reinicia en el tablero las posibles opciones de movimiento pintadas anteriormente
     * es decir cambiamos las tipo 9 a tipo anterior
     */
    private fun borrarOpcionesAntiguas() {
        for (i in 0..7){
            for (j in 0..7) {
                if (tablero[i][j] == 9 || tablero[i][j] == 2) {
                    if (tablero[i][j] == 9) tablero[i][j] = 0
                    borrarOpcionAntigua(i, j)
                }
            }
        }
    }

    /**
     * limpia una celda concreta
     * @param x posición x de la celda a limpiar
     * @param y posición y de la celda a limpiar
     */
    private fun borrarOpcionAntigua(x: Int, y: Int) {
        // recogemos una celda
        val iv : ImageView = findViewById(resources.getIdentifier("ivc$x$y", "id", packageName))
        if (mirarColor(x,y) =="negra")
                iv.setBackgroundColor(ContextCompat.getColor(this,
                    resources.getIdentifier(colorCeldaNegra, "color", packageName)))
        else
            iv.setBackgroundColor(ContextCompat.getColor(this,
                resources.getIdentifier(colorCeldaBlanca, "color", packageName)))

        if (tablero[x][y]==1 ) iv.setBackgroundColor(ContextCompat.getColor(this,
            resources.getIdentifier("previous_cell", "color", packageName)))




    }

    /**
     * en función de la posición dada y teniendo el cuenta lo que queda libre del tablero, cuenta
     * los movimientos posibles de un solo salto
     * @param x posición en x
     * @param y posición en y
     * @return cantidad opciones
     */
    private fun checkPosiblesOpciones(x: Int, y: Int) : Int {
        // las que pueden ser
        numeroOpcionesDisponibles = 0
        //cuento los posibles y los voy sumando
       // numeroOpcionesDisponibles =
        checkMovimiento(x,y,1,2)
        checkMovimiento(x,y,1,-2)
        checkMovimiento(x,y,2,1)
        checkMovimiento(x,y,2,-1)
        checkMovimiento(x,y,-1,2)
        checkMovimiento(x,y,-1,-2)
        checkMovimiento(x,y,-2,1)
        checkMovimiento(x,y,-2,-1)

        //de paso actualizo el campo opciones disponibles del interfaz
          var tv = findViewById<TextView>(R.id.opcionesDato)
         tv.text = numeroOpcionesDisponibles.toString()

        //devuelvo la cantidad posible
        return numeroOpcionesDisponibles
    }

    /**
     * True si el movimiento es posible
     * @param x el parámetro x en el que se encuentr actualmente
     * @param y el parámetro y en el que se encuentra actualmente
     * @param i la posición x a la que quiere saltar
     * @param j la posición y a la que quiere saltar
     * @return 1 para libre, 0 para ocupado asi puedo hacer una suma total al llamar a esto por
     * las diferentes celdas
     */
    private fun checkMovimiento(x: Int, y: Int, i: Int, j: Int) {
            var opcionX = x + i
            var opcionY = y + j

            if (opcionX < 8 && opcionY < 8 && opcionX >= 0 && opcionY >= 0){
                if ((tablero[opcionX][opcionY] == 0)
                    || (tablero[opcionX][opcionY] == 2)) {
                    numeroOpcionesDisponibles++
                    refrescaOpciones(opcionX,opcionY)
                    // si es una casilla vacia, entonces se puede marcar como premio
                   if(tablero[opcionX][opcionY] == 0) tablero[opcionX][opcionY] = 9

                }
            }


    }


    /**
     * Redibula el campo opciones con las opciones nuevas
     */
    private fun refrescaOpciones(x: Int, y: Int) {


        // vamos a dar un borde extra para que se vea en el tablero
        var iv : ImageView = findViewById(resources.getIdentifier("ivc$x$y", "id", packageName))
        // el reborde depende del color en función del color de la propia celda
        // sera reborde opcionblanca.xml o opcionnegra.xml
        if (mirarColor(x,y) =="negra"){
            iv.setBackgroundResource(R.drawable.opcionblanca)

        } else {
            //debe ser negra, añade blanca
            iv.setBackgroundResource(R.drawable.opcionnegra)
        }

    }


    /**
     * devuelve el color de una celda en forma de cadena
     * @param x la posición x de la celda a mirar
     * @param y la posición y de la celda a mirar
     * @return el color de esa celda como "negra" o "blanca"
     */
    private fun mirarColor(x: Int, y: Int): String {

        var color = ""
        //las columnas negras son estas
        var columnaNegra = arrayOf(0,2,4,6)
        //las filas negras son estas
        var filaNegra = arrayOf(1,3,5,7)
        //si coincide en esa posición
        if ((columnaNegra.contains(x) && columnaNegra.contains(y))
            || (filaNegra.contains(x) && filaNegra.contains(y)))
            color = "negra"
        else color = "blanca"

        return color

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
    @SuppressLint("SuspiciousIndentation")
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


        //el tamaño del bonus es el doble que el de las celdas
        width_bonus = widthCell.toInt() * 2



        for (i in 0..7){
            for (j in 0..7){
                iv = findViewById(resources.getIdentifier("ivc$i$j", "id", packageName))

                //ahora asignamos los nuevos anchos y largos a cada celda
                var height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, heightCell, getResources().getDisplayMetrics()).toInt()
                var width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, widthCell, getResources().getDisplayMetrics()).toInt()

                    iv.setLayoutParams(TableRow.LayoutParams(width, height))



            }


        }
    }


}
