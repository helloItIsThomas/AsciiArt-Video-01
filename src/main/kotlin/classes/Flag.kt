package classes

import GLOBAL.cellArray

class Flag {
    var storedTime: Int
    var counter: Int
    var sceneCounter: Int

    init {
        storedTime = 0
        counter = 0
        sceneCounter = 0
    }
    fun check(_updateClockRef: Double, _sceneInterval: Int){
        if(storedTime != _updateClockRef.toInt()){
            // FIRE
//                    println("                 fire: $frameCount")
            storedTime = _updateClockRef.toInt()
            counter = 0
            sceneCounter++
            if(sceneCounter >= _sceneInterval){
//                        println("           Fire Scene Interval")
                cellArray.forEachIndexed { i, row ->
                    row.forEachIndexed { j, value ->
                        value.updateBright(value.brightness)
                        value.calcW()
                    }
                }
                sceneCounter = 0
            }
        } else {
            counter++
        }
        cellArray.forEachIndexed { i, row ->
            row.forEachIndexed { j, value ->
                value.animW(_updateClockRef)
            }
        }
    }
}