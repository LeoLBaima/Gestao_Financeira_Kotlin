package br.unifor.gfinanceira.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.unifor.gfinanceira.R
import br.unifor.gfinanceira.model.Revenue
import java.text.NumberFormat

class RevenueAdapter(val revenues:List<Revenue>):RecyclerView.Adapter<RevenueAdapter.RevenueViewHolder>() {

    private var listener:RevenueItemListener? = null;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RevenueViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_revenue, parent, false)
        return RevenueViewHolder(itemView, listener);
    }

    override fun onBindViewHolder(holder: RevenueViewHolder, position: Int) {
        val format = NumberFormat.getInstance().format(revenues[position].value);
        val string = "R$ ${format},00";
        holder.name.text = revenues[position].name;
        holder.value.text = string;
    }

    override fun getItemCount(): Int {
        return revenues.size;
    }

    fun setRevenueItemListener(listener: RevenueItemListener) {
        this.listener = listener;
    }

    class RevenueViewHolder(itemView: View, listener: RevenueItemListener?):RecyclerView.ViewHolder(itemView){

        val name:TextView = itemView.findViewById(R.id.item_revenue_textview_name);
        val value:TextView = itemView.findViewById(R.id.item_revenue_textview_value);


        init {
            itemView.setOnLongClickListener {
                listener?.onRevenueItemLongClick(it, adapterPosition);
                true
            }
        }


    }

}