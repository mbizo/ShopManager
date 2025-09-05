package com.figstom.shop.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.figstom.shop.data.*
import com.figstom.shop.databinding.FragmentSalesBinding

class SalesFragment : Fragment() {
    private var _binding: FragmentSalesBinding? = null
    private val binding get() = _binding!!

    private val vm: SalesViewModel by viewModels {
        object : ViewModelProvider.AndroidViewModelFactory() {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SalesViewModel(requireActivity().application) as T
            }
        }
    }

    private val productsVm: ProductsViewModel by viewModels {
        object : ViewModelProvider.AndroidViewModelFactory() {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ProductsViewModel(requireActivity().application) as T
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSalesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = SaleAdapter()
        binding.rvSaleItems.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSaleItems.adapter = adapter

        vm.cartItems.observe(viewLifecycleOwner) { list ->
            adapter.submit(list)
        }

        binding.btnStartSale.setOnClickListener {
            showAddItemDialog()
        }
        binding.btnCompleteSale.setOnClickListener {
            vm.complete()
        }
    }

    private fun showAddItemDialog() {
        // simple dialog: choose first product by name and a qty
        val ctx = requireContext()
        val products = productsVm.products.value ?: emptyList()
        if (products.isEmpty()) {
            AlertDialog.Builder(ctx).setMessage("No products available. Add some first.").setPositiveButton("OK", null).show()
            return
        }
        val names = products.map { it.name }.toTypedArray()
        var selected = 0
        val container = layoutInflater.inflate(com.figstom.shop.R.layout.item_sale, null) // just reuse for padding
        val qtyInput = EditText(ctx); qtyInput.hint = "Quantity"; qtyInput.inputType = android.text.InputType.TYPE_CLASS_NUMBER
        val wrapper = androidx.appcompat.widget.LinearLayoutCompat(ctx)
        wrapper.orientation = androidx.appcompat.widget.LinearLayoutCompat.VERTICAL
        wrapper.setPadding(48, 24, 48, 0)
        wrapper.addView(qtyInput)
        AlertDialog.Builder(ctx)
            .setTitle("Add item")
            .setSingleChoiceItems(names, 0) { _, which -> selected = which }
            .setView(wrapper)
            .setPositiveButton("Add") { d, _ ->
                val qty = qtyInput.text.toString().toIntOrNull() ?: 1
                vm.addItem(products[selected], qty)
                d.dismiss()
            }.setNegativeButton("Cancel", null).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class SaleAdapter : RecyclerView.Adapter<SaleVH>() {
    private val items = mutableListOf<SaleItem>()
    fun submit(list: List<SaleItem>) { items.clear(); items.addAll(list); notifyDataSetChanged() }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaleVH {
        val v = LayoutInflater.from(parent.context).inflate(com.figstom.shop.R.layout.item_sale, parent, false)
        return SaleVH(v)
    }
    override fun getItemCount(): Int = items.size
    override fun onBindViewHolder(holder: SaleVH, position: Int) { holder.bind(items[position]) }
}

class SaleVH(v: View): RecyclerView.ViewHolder(v) {
    fun bind(si: SaleItem) {
        val tv = itemView.findViewById<android.widget.TextView>(com.figstom.shop.R.id.tvSaleLine)
        tv.text = "ProductId=${si.productId} x ${si.qty} @ ${si.unitPrice}"
    }
}
