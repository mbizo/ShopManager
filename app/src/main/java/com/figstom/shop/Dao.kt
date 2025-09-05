package com.figstom.shop.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProductDao {
    @Query("SELECT * FROM Product ORDER BY name")
    fun getAll(): LiveData<List<Product>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(product: Product)

    @Update
    suspend fun update(product: Product)

    @Delete
    suspend fun delete(product: Product)

    @Query("UPDATE Product SET stockQty = stockQty - :qty WHERE id = :productId")
    suspend fun reduceStock(productId: Long, qty: Int)
}

@Dao
interface SaleDao {
    @Insert
    suspend fun insertSale(sale: Sale): Long

    @Insert
    suspend fun insertItems(items: List<SaleItem>)

    @Query("SELECT * FROM Sale ORDER BY timestamp DESC")
    fun getSales(): LiveData<List<Sale>>

    @Transaction
    suspend fun completeSale(items: List<SaleItem>): Long {
        val total = items.sumOf { it.qty * it.unitPrice }
        val saleId = insertSale(Sale(totalAmount = total))
        insertItems(items.map { it.copy(saleOwnerId = saleId) })
        return saleId
    }
}
