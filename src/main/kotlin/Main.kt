package edu.unicauca.aplimovil


fun main() {
    val frodo = Guerrero("Frodo")
    val atreyu = Guerrero("Atreyu")
    val asterix = Guerrero("Asterix")
    val gandalf = Hechicero("Gandalf")
    val merlin = Hechicero("Merlin")

    val pocionVida = Objeto("Poción de Vida") { personaje -> personaje.incrementarVida(10.0) }
    val pocionMana = Objeto("Poción de Mana") { personaje -> if (personaje is Hechicero) personaje.mana += 20.0 }

    atreyu.recogerObjeto(pocionVida)
    gandalf.recogerObjeto(pocionMana)

    println("Antes del ataque")
    atreyu.imprimirDatos()

    frodo.atacar(atreyu)

    println("Después del ataque")
    atreyu.imprimirDatos()

    println("Usando poción de vida en Atreyu")
    atreyu.usarObjeto(pocionVida)
    atreyu.imprimirDatos()

    println("Usando poción de mana en Gandalf")
    gandalf.usarObjeto(pocionMana)
    gandalf.imprimirDatos()

    println("Antes de caminar")
    frodo.imprimirDatos()
    frodo.caminar(1)
    println("Después de caminar")
    frodo.imprimirDatos()

    println("Antes de volar")
    gandalf.imprimirDatos()
    gandalf.volar(1, 1)
    println("Después de volar")
    gandalf.imprimirDatos()
}

abstract class Personaje(val nombre: String) {
    var vida: Double = 100.0
    var escudo: Boolean = false
    var posicion: Posicion = Posicion(0, 0)
    var experiencia: Int = 0
    val inventario: MutableList<Objeto> = mutableListOf()

    fun incrementarVida(incremento: Double) {
        if (vida == 0.0) {
            println("$nombre no puede hacer nada porque ha muerto.")
            return
        }
        val nVida = vida + incremento
        if (nVida in (0.0..100.0)) {
            vida = nVida
        }
    }

    fun incrementarExperiencia(incremento: Int) {
        if (vida == 0.0) {
            println("$nombre no puede hacer nada porque ha muerto.")
            return
        }
        if (incremento > 0) experiencia += incremento
    }

    fun reducirVida(reduccion: Double) {
        if (vida == 0.0) {
            println("$nombre no puede hacer nada porque ha muerto.")
            return
        }
        val nVida = vida - reduccion
        if (nVida <= 0) {
            vida = 0.0
            println("$nombre ha muerto")
        } else if (nVida in (0.0..100.0)) {
            vida = nVida
        }
    }

    fun mover(posicion: Posicion) {
        if (vida == 0.0) {
            println("$nombre no puede hacer nada porque ha muerto.")
            return
        }
        this.posicion = posicion
    }

    fun recogerObjeto(objeto: Objeto) {
        if (vida == 0.0) {
            println("$nombre no puede hacer nada porque ha muerto.")
            return
        }
        inventario.add(objeto)
        println("$nombre ha recogido un ${objeto.nombre}")
    }

    fun usarObjeto(objeto: Objeto) {
        if (vida == 0.0) {
            println("$nombre no puede hacer nada porque ha muerto.")
            return
        }
        if (inventario.contains(objeto)) {
            objeto.usar(this)
            inventario.remove(objeto)
        } else {
            println("$nombre no tiene un ${objeto.nombre}")
        }
    }

    open fun imprimirDatos() {
        println("Nombre: $nombre, Vida: $vida, Escudo: $escudo, Posición: ($posicion), Experiencia: $experiencia")
    }
}
@FunctionalInterface
interface Acuatico {
    fun nadar(despX: Int, despY: Int)
}

interface Terrestre {
    fun correr(despX: Int)
    fun caminar(despX: Int)
}
@FunctionalInterface
interface Aereo {
    fun volar(despX: Int, despY: Int)
}

class Guerrero(nombre: String) : Personaje(nombre), Terrestre, Acuatico {
    var fuerza: Double = 100.0

    fun atacar(enemigo: Personaje) {
        if (vida == 0.0) {
            println("$nombre no puede hacer nada porque ha muerto.")
            return
        }
        if (this.posicion == enemigo.posicion) {
            if (enemigo.escudo) enemigo.escudo = false
            else enemigo.reducirVida(fuerza / 10)
            incrementarExperiencia(5)
            fuerza--
        } else {
            println("El enemigo no está en la misma posición.")
        }
    }

    override fun nadar(despX: Int, despY: Int) {
        if (vida == 0.0) {
            println("$nombre no puede hacer nada porque ha muerto.")
            return
        }
        val nuevaY = posicion.y + despY
        if (nuevaY > 0) {
            mover(Posicion(posicion.x + despX, posicion.y))
        }
        mover(Posicion(posicion.x + despX, nuevaY))
    }

    override fun correr(despX: Int) {
        if (vida == 0.0) {
            println("$nombre no puede hacer nada porque ha muerto.")
            return
        }
        mover(Posicion(posicion.x + despX, posicion.y))
    }

    override fun caminar(despX: Int) {
        if (vida == 0.0) {
            println("$nombre no puede hacer nada porque ha muerto.")
            return
        }
        mover(Posicion(posicion.x + despX, posicion.y))
    }

    override fun imprimirDatos() {
        println("******** Guerrero ********")
        super.imprimirDatos()
        println("Fuerza: $fuerza")
    }
}

class Hechicero(nombre: String) : Personaje(nombre), Aereo, Terrestre {
    var mana: Double = 100.0

    fun proteger(amigo: Personaje) {
        if (vida == 0.0) {
            println("$nombre no puede hacer nada porque ha muerto.")
            return
        }
        if (this.posicion.x == amigo.posicion.x) {
            incrementarExperiencia(1)
            amigo.escudo = true
            mana--
        } else {
            println("El amigo no está en la misma línea horizontal.")
        }
    }

    fun hechizar(enemigo: Personaje) {
        if (vida == 0.0) {
            println("$nombre no puede hacer nada porque ha muerto.")
            return
        }
        if (this.posicion.x == enemigo.posicion.x) {
            incrementarExperiencia(2)
            enemigo.escudo = false
            mana -= 2
        } else {
            println("El enemigo no está en la misma línea horizontal.")
        }
    }

    fun sanar(amigo: Personaje) {
        if (vida == 0.0) {
            println("$nombre no puede hacer nada porque ha muerto.")
            return
        }
        incrementarExperiencia(2)
        amigo.incrementarVida(mana / 10)
        mana--
    }

    override fun correr(despX: Int) {
        if (vida == 0.0) {
            println("$nombre no puede hacer nada porque ha muerto.")
            return
        }
        mover(Posicion(posicion.x + despX, posicion.y))
    }

    override fun caminar(despX: Int) {
        if (vida == 0.0) {
            println("$nombre no puede hacer nada porque ha muerto.")
            return
        }
        mover(Posicion(posicion.x + despX, posicion.y))
    }

    override fun volar(despX: Int, despY: Int) {
        if (vida == 0.0) {
            println("$nombre no puede hacer nada porque ha muerto.")
            return
        }
        val nuevaY = posicion.y + despY
        if (nuevaY < 0) {
            mover(Posicion(posicion.x + despX, posicion.y))
        }
        mover(Posicion(posicion.x + despX, nuevaY))
    }

    override fun imprimirDatos() {
        println("******** Hechicero ********")
        super.imprimirDatos()
        println("Mana: $mana")
    }
}

data class Posicion(val x: Int, val y: Int)

class Objeto(val nombre: String, val efecto: (Personaje) -> Unit) {
    fun usar(personaje: Personaje) {
        efecto(personaje)
    }
}