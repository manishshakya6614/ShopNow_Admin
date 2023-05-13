package com.manish.shopnowadmin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.manish.shopnowadmin.databinding.AllOrdersDetailsItemLayoutBinding
import com.manish.shopnowadmin.model.AllOrdersModel

class AllOrdersAdapter(val context: Context, private val list : ArrayList<AllOrdersModel>) : RecyclerView.Adapter<AllOrdersAdapter.AllOrdersViewHolder>() {

    inner class AllOrdersViewHolder(val binding: AllOrdersDetailsItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllOrdersViewHolder {
        val binding  = AllOrdersDetailsItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllOrdersViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: AllOrdersViewHolder, position: Int) {
        holder.binding.productNameTV.text = list[position].name
        holder.binding.productPriceTV.text = list[position].price
        holder.binding.cancelBtn.setOnClickListener {
            holder.binding.cancelBtn.visibility = View.GONE
            updateStatus("Cancelled", list[position].orderId!!)
        }

        when (list[position].status) {
            "Ordered" -> {
                holder.binding.proceedBtn.text = "Dispatched"
                holder.binding.proceedBtn.setOnClickListener {
                    updateStatus("Dispatched", list[position].orderId!!)
                }
            }
            "Dispatched" -> {
                holder.binding.proceedBtn.text = "Delivered"
                holder.binding.proceedBtn.setOnClickListener {
                    updateStatus("Delivered", list[position].orderId!!)
                }
            }
            "Delivered" -> {
                holder.binding.cancelBtn.visibility = View.GONE
                holder.binding.proceedBtn.isEnabled = false
                holder.binding.proceedBtn.text = "Already Delivered"
            }
            "Canceled" -> {
                holder.binding.cancelBtn.isEnabled = false
                holder.binding.proceedBtn.visibility = View.GONE
            }
        }
    }
    private fun updateStatus(str : String, doc : String) {
        val data = hashMapOf<String, Any>()
        data["status"] = str
        Firebase.firestore.collection("allOrders").document(doc)
            .update(data)
            .addOnSuccessListener {
                Toast.makeText(context, "Status Updated", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
    }
}