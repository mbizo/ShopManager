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
