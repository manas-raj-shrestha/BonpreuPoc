package com.dinube.bonpreu.demo.bankconnection.providers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dinube.bonpreu.R
import com.dinube.bonpreu.TransactionAdapter
import com.dinube.bonpreu.data.afterbanks.Provider

class ProviderRvAdapter(val body: ArrayList<Provider>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvBankName: TextView = itemView.findViewById(R.id.tv_bank_name)
        val ivBankLogo: ImageView = itemView.findViewById(R.id.iv_bank_logo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProviderRvAdapter.ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.provider_list_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return this.body!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).tvBankName.text = body?.get(position)?.fullname
        Glide.with(holder.itemView.context).load(body?.get(position)?.image).circleCrop().into((holder as ViewHolder).ivBankLogo)
    }
}
