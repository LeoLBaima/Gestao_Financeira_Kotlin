package br.unifor.gfinanceira.adapter

import android.view.View

interface ExpenseItemListener {

    fun onExpenseItemLongClick(v: View, pos: Int)
}