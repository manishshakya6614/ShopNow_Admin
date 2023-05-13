package com.manish.shopnowadmin.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.manish.shopnowadmin.R
import com.manish.shopnowadmin.databinding.FragmentProductBinding

class ProductFragment : Fragment() {
    private lateinit var binding: FragmentProductBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductBinding.inflate(layoutInflater)

        binding.fabProduct.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_productFragment_to_addProductFragment)
        }
        return binding.root
    }
}