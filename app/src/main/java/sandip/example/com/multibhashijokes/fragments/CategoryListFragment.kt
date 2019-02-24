package sandip.example.com.multibhashijokes.fragments


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import sandip.example.com.multibhashijokes.MainActivity
import sandip.example.com.multibhashijokes.R
import sandip.example.com.multibhashijokes.adapter.CategoryPagerAdapter
import sandip.example.com.multibhashijokes.binding.FragmentDataBindingComponent
import sandip.example.com.multibhashijokes.databinding.FragmentCategoryListBinding
import sandip.example.com.multibhashijokes.di.Injectable
import sandip.example.com.multibhashijokes.utils.helperUtils.autoCleared
import sandip.example.com.multibhashijokes.utils.remoteUtils.Status
import sandip.example.com.multibhashijokes.viewModel.CategoryListViewModel
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 *
 */
class CategoryListFragment : Fragment(), Injectable {

    var binding by autoCleared<FragmentCategoryListBinding>()

    var adapter by autoCleared<CategoryPagerAdapter>()

    private lateinit var viewModel: CategoryListViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var pagerItem: MutableList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_category_list,
            container,
            false,
            dataBindingComponent
        )

        val actionBar = (activity as MainActivity).supportActionBar!!
        actionBar.title = getString(R.string.category)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CategoryListViewModel::class.java)

        initViewModel(viewModel = viewModel)

        adapter = CategoryPagerAdapter(context = requireContext(), dataBindingComponent = dataBindingComponent, pagerItem = pagerItem) { item ->

            navController().navigate(
                CategoryListFragmentDirections.actionCategoryListFragmentToJokesListFragment(item)
            )

        }

        binding.let {
            it.adapter = adapter
            it.setLifecycleOwner(this)
        }

        viewModel.init(refID = "value")


    }

    private fun initViewModel(viewModel: CategoryListViewModel) {

        viewModel.result.observe(this, Observer { listResource ->
            // we don't need any null checks here for the adapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            binding.resource = listResource
            binding.count = listResource?.data?.value?.size
            endProgress()
            when (listResource?.status) {
                Status.SUCCESS -> {
                    endProgress()
                    pagerItem.clear()
                    listResource.data?.value?.let {
                        it.mapIndexed { index, child ->
                            child?.let {
                                pagerItem.add(it)
                            }
                        }
                        adapter.notifyDataSetChanged()
                    }
                }

                Status.ERROR -> {
                    endProgress()
                    binding.count = 0
                    Toast.makeText(requireContext(), getString(R.string.generalError), Toast.LENGTH_LONG).show()
                }

                Status.LOADING -> {
                    startProgress()
                }
            }
        })
    }

    private fun startProgress() {
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    private fun endProgress() {
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun navController() = findNavController()

}
