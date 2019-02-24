package sandip.example.com.multibhashijokes.adapter

import android.content.Context
import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.support.constraint.ConstraintLayout
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import sandip.example.com.multibhashijokes.R
import sandip.example.com.multibhashijokes.databinding.LayoutItemCategoryBinding


class CategoryPagerAdapter(
    val context: Context,
    private val dataBindingComponent: DataBindingComponent,
    val pagerItem: List<String>,
    private val callback: ((String) -> Unit)?
) : PagerAdapter() {

    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return p0 == (p1 as ConstraintLayout);
    }

    override fun getCount(): Int {
        return pagerItem.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding = DataBindingUtil
            .inflate<LayoutItemCategoryBinding>(
                LayoutInflater.from(context),
                R.layout.layout_item_category,
                container,
                false,
                dataBindingComponent
            )

        binding.category = pagerItem[position]

        binding.details.setOnClickListener {
            binding.category?.let {
                callback?.invoke(it)
            }
        }

        container.addView(binding.root)

        return binding.root
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val binding = `object` as ConstraintLayout
        container.removeView(binding)
    }

}