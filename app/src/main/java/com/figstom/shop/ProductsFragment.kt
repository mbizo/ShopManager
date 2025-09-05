package com.figstom.shop.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.figstom.shop.data.Product
import com.figstom.shop.data.ProductsViewModel
import com.figstom.shop.databinding.FragmentProductsBinding

class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!

    private val vm: ProductsViewModel by viewModels {
        object : ViewModelProvider.AndroidViewModelFactory() {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ProductsViewModel(requireActivity().application) as T
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ProductAdapter()
        binding.rvProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProducts.adapter = adapter

        vm.products.observe(viewLifecycleOwner) { list ->
            adapter.submit(list)
        }

        binding.btnAddProduct.setOnClickListener {
            showAddDialog()
        }
    }

    private fun showAddDialog() {
        val dlg = AlertDialog.Builder(requireContext()).create()
        val v = layoutInflater.inflate(com.figstom.shop.R.layout.dialog_add_product, null)
        dlg.setView(v)
        dlg.setButton(AlertDialog.BUTTON_POSITIVE, getString(com.figstom.shop.R.string.save)) { d, _ ->
            val name = v.findViewById<EditText>(com.figstom.shop.R.id.etName).text.toString()
            val barcode = v.findViewById<EditText>(com.figstom.shop.R.id.etBarcode).text.toString().ifBlank { null }
            val cost = v.findViewById<EditText>(com.figstom.shop.R.id.etCost).text.toString().toDoubleOrNull() ?: 0.0
            val price = v.findViewById<EditText>(com.figstom.shop.R.id.etPrice).text.toString().toDoubleOrNull() ?: 0.0
            val qty = v.findViewById<EditText>(com.figstom.shop.R.id.etQty).text.toString().toIntOrNull() ?: 0
            vm.addProduct(name, barcode, cost, price, qty)
            d.dismiss()
        }
        dlg.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel") { d, _ -> d.dismiss() }
        dlg.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class ProductAdapter : RecyclerView.Adapter<ProductVH>() {
    private val items = mutableListOf<Product>()
    fun submit(list: List<Product>) {
        items.clear(); items.addAll(list); notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductVH {
        val v = LayoutInflater.from(parent.context).inflate(com.figstom.shop.R.layout.item_product, parent, false)
        return ProductVH(v)
    }
    override fun getItemCount(): Int = items.size
    override fun onBindViewHolder(holder: ProductVH, position: Int) {
        holder.bind(items[position])
    }
}

class ProductVH(v: View) : RecyclerView.ViewHolder(v) {
    fun bind(p: Product) {
        val name = itemView.findViewById<android.widget.TextView>(com.figstom.shop.R.id.tvName)
        val details = itemView.findViewById<android.widget.TextView>(com.figstom.shop.R.id.tvDetails)
        name.text = p.name
        details.text = "Barcode: ${p.barcode ?: "-"}  |  Stock: ${p.stockQty}  |  $${p.sellPrice}"
    }
}
