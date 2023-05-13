package com.manish.shopnowadmin.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.manish.shopnowadmin.R
import com.manish.shopnowadmin.adapter.CategoryAdapter
import com.manish.shopnowadmin.databinding.FragmentCategoryBinding
import com.manish.shopnowadmin.model.CategoryModel
import java.util.UUID

class CategoryFragment : Fragment() {
    private lateinit var binding: FragmentCategoryBinding
    private var imageUrl: Uri? = null
    private lateinit var dialog: Dialog

    // This is launching Gallery activity and getting the image for the slider
    private var launchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK){
            imageUrl = it.data!!.data
            binding.categoryImageView.setImageURI(imageUrl)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(layoutInflater)

        dialog = Dialog(requireContext()).apply {
            setContentView(R.layout.progress_layout)
            setCancelable(false)
        }

        binding.apply {
            categoryImageView.setOnClickListener {
                val intent = Intent("android.intent.action.GET_CONTENT")
                intent.type = "image/*"
                launchGalleryActivity.launch(intent)
            }
            uploadCategory.setOnClickListener {
                validateData(binding.categoryName.text.toString())
            }
        }
        getData()
        return binding.root
    }

    private fun getData() {
        val list = ArrayList<CategoryModel>()
        Firebase.firestore.collection("categories")
            .get().addOnSuccessListener {
                list.clear()
                for (doc in it.documents) {
                    val data = doc.toObject(CategoryModel::class.java)
                    list.add(data!!)
                }
                binding.categoryRV.adapter = CategoryAdapter(requireContext(), list)
            }
    }

    private fun validateData(categoryName : String) {
        if (categoryName.isEmpty()){
            Toast.makeText(requireContext(), "Please provide category name", Toast.LENGTH_SHORT).show()
        } else if (imageUrl == null) {
            Toast.makeText(requireContext(), "Please select image", Toast.LENGTH_SHORT).show()
        } else {
            uploadImage(categoryName)
        }
    }

    private fun uploadImage(categoryName: String) {
        dialog.show()
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val refStorage = FirebaseStorage.getInstance().reference.child("category/$fileName")

        refStorage.putFile(imageUrl!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image ->
                    storeData(categoryName, image.toString())
                }
            }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireContext(), "Something went wrong with storage", Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeData(categoryName: String, url: String) {
        val db = Firebase.firestore
        val data = hashMapOf<String, Any>(
            "cat" to categoryName,
            "img" to url
        )

        db.collection("categories").add(data)
            .addOnSuccessListener {
                dialog.dismiss()
                binding.categoryImageView.setImageResource(R.drawable.image_preview)
                binding.categoryName.text = null
                getData()
                Toast.makeText(requireContext(), "Category added", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
            }
    }
}