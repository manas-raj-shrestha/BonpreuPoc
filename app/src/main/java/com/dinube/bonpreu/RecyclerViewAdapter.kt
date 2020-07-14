package com.dinube.bonpreu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TransactionAdapter(private val transactionItems: ArrayList<TransactionItems>) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tv_date)
        val tvCode: TextView = itemView.findViewById(R.id.tv_code)
        val tvAmount: TextView = itemView.findViewById(R.id.tv_amount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.transaction_item_view, parent, false))
    }

    override fun getItemCount(): Int {
        return this.transactionItems.size
     }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvAmount.text = transactionItems[position].amount.plus(" €")
        holder.tvCode.text = transactionItems[position].code
        holder.tvDate.text = transactionItems[position].date
    }

    data class TransactionItems(val amount: String, val detail:String, val date: String, val code:String)
}