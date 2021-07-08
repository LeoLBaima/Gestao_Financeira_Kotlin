package br.unifor.gfinanceira.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


data class Revenue(
    //Receita ID
    val rid:String = "",
    val name:String = "",
    val value:Int = 0,
)
