package com.manish.shopnowadmin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.manish.shopnowadmin.adapter.AllOrdersAdapter
import com.manish.shopnowadmin.databinding.ActivityAllOrdersBinding
import com.manish.shopnowadmin.model.AllOrdersModel

class AllOrdersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAllOrdersBinding
    private lateinit var list: ArrayList<AllOrdersModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list = ArrayList()

        val builder = AlertDialog.Builder(this)
            .setTitle("Loading....")
            .setMessage("Please Wait")
            .setCancelable(false)
            .create()
        builder.show()

        Firebase.firestore.collection("allOrders").get().addOnSuccessListener {
            list.clear()

            for (doc in it) {
                val data = doc.toObject(AllOrdersModel::class.java)
                list.add(data)
            }
            binding.recyclerView.adapter = AllOrdersAdapter(this, list)
            builder.dismiss()
        }
    }
}