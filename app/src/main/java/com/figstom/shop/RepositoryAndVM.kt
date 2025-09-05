package com.figstom.shop.data

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Repository(app: Application) {
    private val db = AppDatabase.getInstance(app)
    private val productDao = db.productDao()
    private val saleDao = db.saleDao()

    fun products(): LiveData<List<Product>> = productDao.getAll()
    fun sales(): LiveData<List<Sale>> = saleDao.getSales()

    fun addProduct(p: Product) = liveData(Dispatchers.IO) {
        productDao.insert(p); emit(true)
    }

    fun completeSale(items: List<SaleItem>) = liveData(Dispatchers.IO) {
        // reduce stock per item, then complete sale
        items.forEach { productDao.reduceStock(it.productId, it.qty) }
        val id = saleDao.completeSale(items)
        emit(id)
    }
}

class ProductsViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = Repository(app)
    val products: LiveData<List<Product>> = repo.products()

    fun addProduct(name: String, barcode: String?, cost: Double, price: Double, qty: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addProduct(Product(name = name, barcode = barcode, costPrice = cost, sellPrice = price, stockQty = qty))
        }
    }
}

class SalesViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = Repository(app)
    val sales: LiveData<List<Sale>> = repo.sales()
    val cartItems = MutableLiveData<MutableList<SaleItem>>(mutableListOf())

    fun addItem(product: Product, qty: Int) {
        val list = cartItems.value ?: mutableListOf()
        list.add(SaleItem(saleOwnerId = 0, productId = product.id, qty = qty, unitPrice = product.sellPrice))
        cartItems.postValue(list)
    }

    fun complete() {
        val items = cartItems.value?.toList() ?: emptyList()
        viewModelScope.launch(Dispatchers.IO) {
            repo.completeSale(items)
            cartItems.postValue(mutableListOf())
        }
    }
}
