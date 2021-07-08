package br.unifor.gfinanceira.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.unifor.gfinanceira.R
import br.unifor.gfinanceira.model.Expense
import java.text.NumberFormat

class ExpenseAdapter(val expenses:List<Expense>):RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    private var listener:ExpenseItemListener? = null;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(itemView, listener);
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val format = NumberFormat.getInstance().format(expenses[position].value);
        val string = "R$ ${format},00";
        holder.name.text = expenses[position].name
        holder.value.text = string;
    }

    override fun getItemCount(): Int {
        return expenses.size
    }

    fun setExpenseItemListener(listener: ExpenseItemListener){
        this.listener = listener;
    }


    class ExpenseViewHolder(itemView: View, listener: ExpenseItemListener?):RecyclerView.ViewHolder(itemView){
        val name:TextView = itemView.findViewById(R.id.item_expense_textview_name);
        val value:TextView = itemView.findViewById(R.id.item_expense_textview_value);


        init {
            itemView.setOnLongClickListener {
                listener?.onExpenseItemLongClick(it, adapterPosition)
                true
            }
        }



        }




}