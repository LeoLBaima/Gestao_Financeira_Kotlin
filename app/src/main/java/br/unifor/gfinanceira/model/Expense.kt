package br.unifor.gfinanceira.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class Expense(
    //Despesa Id
    val did:String = "",
    val name:String = "",
    val value:Int = 0,
)
