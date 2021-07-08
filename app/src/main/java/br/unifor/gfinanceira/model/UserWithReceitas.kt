package br.unifor.gfinanceira.model

data class UserWithReceitas (

    val user: User,

    val revenues: List<Revenue>
)