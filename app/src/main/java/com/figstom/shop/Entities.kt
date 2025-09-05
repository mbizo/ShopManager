package com.figstom.shop.data

import androidx.room.*

@Entity(indices = [Index(value = ["barcode"], unique = true)])
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val barcode: String? = null,
    val costPrice: Double,
    val sellPrice: Double,
    val stockQty: Int
)

@Entity
data class Sale(
    @PrimaryKey(autoGenerate = true) val saleId: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val totalAmount: Double = 0.0
)

@Entity(
    foreignKeys = [
        ForeignKey(entity = Sale::class, parentColumns = ["saleId"], childColumns = ["saleOwnerId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Product::class, parentColumns = ["id"], childColumns = ["productId"])
    ],
    indices = [Index("saleOwnerId"), Index("productId")]
)
data class SaleItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val saleOwnerId: Long,
    val productId: Long,
    val qty: Int,
    val unitPrice: Double
)
