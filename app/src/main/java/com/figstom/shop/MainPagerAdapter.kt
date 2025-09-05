package com.figstom.shop

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.figstom.shop.ui.ProductsFragment
import com.figstom.shop.ui.SalesFragment

class MainPagerAdapter(activity: FragmentActivity): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment =
        if (position == 0) ProductsFragment() else SalesFragment()
}
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

        // Use ViewModel to insert
        viewModel.insertProduct(product)

        Toast.makeText(requireContext(), "Product added!", Toast.LENGTH_SHORT).show()

        // Clear fields
        etName.text?.clear()
        etCostPrice.text?.clear()
        etSellingPrice.text?.clear()
        etStock.text?.clear()
    } else {
        Toast.makeText(requireContext(), "Enter product name", Toast.LENGTH_SHORT).show()
    }
}
