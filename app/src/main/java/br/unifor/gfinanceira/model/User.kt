package br.unifor.gfinanceira.model

import androidx.room.Entity;
import android.provider.ContactsContract
import androidx.room.ColumnInfo
import androidx.room.Index
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull


data class User(
    val firstName:String,
    val lastName: String,
    val email: String,
    val phone: String
)
