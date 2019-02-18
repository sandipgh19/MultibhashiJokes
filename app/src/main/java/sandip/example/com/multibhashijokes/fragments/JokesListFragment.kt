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
import sandip.example.com.multibhashijokes.MainActivity

import sandip.example.com.multibhashijokes.R
import sandip.example.com.multibhashijokes.adapter.JokesAdapter
import sandip.example.com.multibhashijokes.binding.FragmentDataBindingComponent
import sandip.example.com.multibhashijokes.databinding.FragmentJokesListBinding
import sandip.example.com.multibhashijokes.di.Injectable
import sandip.example.com.multibhashijokes.utils.helperUtils.AppExecutors
import sandip.example.com.multibhashijokes.utils.helperUtils.autoCleared
import sandip.example.com.multibhashijokes.utils.remoteUtils.Status
import sandip.example.com.multibhashijokes.viewModel.JokesListViewModel
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 *
 */
class JokesListFragment : Fragment(), Injectable {

    var binding by autoCleared<FragmentJokesListBinding>()
    var adapter by autoCleared<JokesAdapter>()

    private lateinit var viewModel: JokesListViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    @Inject
    lateinit var executors: AppExecutors

    private val CATEGORY_NAME = "category_name"
    private lateinit var category: String

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CATEGORY_NAME, category)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_jokes_list,
            container,
            false,
            dataBindingComponent
        )

        val actionBar = (activity as MainActivity).supportActionBar!!
        actionBar.title = getString(R.string.jokes)


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(JokesListViewModel::class.java)

        category = savedInstanceState?.getString(CATEGORY_NAME) ?: JokesListFragmentArgs.fromBundle(arguments).category

        initViewModel(viewModel = viewModel)

        viewModel.init(refID = category)

        adapter = JokesAdapter(dataBindingComponent = dataBindingComponent, appExecutors = executors)

        binding.let {
            it.adapter = adapter
            it.lifecycleOwner = this
        }
    }

    private fun initViewModel(viewModel: JokesListViewModel) {

        viewModel.result.observe(this, Observer { listResource ->
            // we don't need any null checks here for the adapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            binding.resource = listResource
            binding.count = listResource?.data?.size
            endProgress()
            when (listResource?.status) {
                Status.SUCCESS -> {
                    endProgress()
                    adapter.submitList(listResource.data)
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


}
