package sandip.example.com.multibhashijokes.adapter

import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import sandip.example.com.multibhashijokes.R
import sandip.example.com.multibhashijokes.databinding.LayoutItemJokesBinding
import sandip.example.com.multibhashijokes.objects.ValueItem
import sandip.example.com.multibhashijokes.utils.DataBoundListAdapter
import sandip.example.com.multibhashijokes.utils.helperUtils.AppExecutors

class JokesAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors
) : DataBoundListAdapter<ValueItem, LayoutItemJokesBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<ValueItem>() {
        override fun areItemsTheSame(oldItem: ValueItem, newItem: ValueItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ValueItem, newItem: ValueItem): Boolean {
            return oldItem.id == newItem.id
        }
    }
) {

    override fun createBinding(parent: ViewGroup): LayoutItemJokesBinding {
        val binding = DataBindingUtil
            .inflate<LayoutItemJokesBinding>(
                LayoutInflater.from(parent.context),
                R.layout.layout_item_jokes,
                parent,
                false,
                dataBindingComponent
            )
        return binding
    }

    override fun bind(binding: LayoutItemJokesBinding, item: ValueItem, position: Int) {
        binding.item = item
        binding.position = (position + 1).toString()
    }
}