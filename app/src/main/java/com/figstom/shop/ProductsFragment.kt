package com.figstom.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProductsFragment : Fragment() {

    private val viewModel: RepositoryAndVM by viewModels()

    private lateinit var adapter: ProductsAdapter
    private lateinit var etName: EditText
    private lateinit var etCostPrice: EditText
    private lateinit var etSellingPrice: EditText
    private lateinit var etStock: EditText
    private lateinit var btnAddProduct: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_products, container, false)

        // Setup input fields
        etName = root.findViewById(R.id.etName)
        etCostPrice = root.findViewById(R.id.etCostPrice)
        etSellingPrice = root.findViewById(R.id.etSellingPrice)
        etStock = root.findViewById(R.id.etStock)
        btnAddProduct = root.findViewById(R.id.btnAddProduct)

        // Setup RecyclerView
        val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerViewProducts)
        adapter = ProductsAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Observe products
        viewModel.allProducts.observe(viewLifecycleOwner) { products ->
            adapter.submitList(products)
        }

        // Add product button
        btnAddProduct.setOnClickListener {
            val name = etName.text.toString().trim()
            val costPrice = etCostPrice.text.toString().toDoubleOrNull() ?: 0.0
            val sellingPrice = etSellingPrice.text.toString().toDoubleOrNull() ?: 0.0
            val stock = etStock.text.toString().toIntOrNull() ?: 0

            if (name.isNotEmpty()) {
                val product = Product(
                    name = name,
                    costPrice = costPrice,
                    sellingPrice = sellingPrice,
                    stock = stock
                )

                viewModel.insertProduct(product)

                Toast.makeText(requireContext(), "Product added!", Toast.LENGTH_SHORT).show()

                // Clear input fields
                etName.text.clear()
                etCostPrice.text.clear()
                etSellingPrice.text.clear()
                etStock.text.clear()

            } else {
                Toast.makeText(requireContext(), "Enter product name", Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }
}

