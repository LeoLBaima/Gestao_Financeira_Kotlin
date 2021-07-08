package br.unifor.gfinanceira.model

data class UserWithDespesas (

    val user: User,
    val expenses: List<Expense>
)