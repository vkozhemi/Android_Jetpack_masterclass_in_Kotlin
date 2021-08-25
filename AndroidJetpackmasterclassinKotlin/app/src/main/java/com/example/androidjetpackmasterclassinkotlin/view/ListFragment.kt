package com.example.androidjetpackmasterclassinkotlin.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidjetpackmasterclassinkotlin.R
import com.example.androidjetpackmasterclassinkotlin.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.*


class ListFragment : Fragment() {

    private lateinit var viewModel: ListViewModel
    private val listAdapter = ListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.refresh()

        recyclerViewList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }

        refreshLayout.setOnRefreshListener {
            Log.d("LOG_TAG", "refreshLayout.setOnRefreshListener")
            recyclerViewList.visibility = View.GONE
            textViewError.visibility = View.GONE
            loadingViewProgressBar.visibility = View.VISIBLE
            viewModel.refreshBypassCache()
            refreshLayout.isRefreshing = false
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.dogs.observe(viewLifecycleOwner, Observer { dogs ->
            dogs?.let {
                recyclerViewList.visibility = View.VISIBLE
                listAdapter.updateList(dogs)
            }
        })

        viewModel.dogsLoadError.observe(viewLifecycleOwner, Observer { isError ->
            isError?.let {
                textViewError.visibility = if (it) View.VISIBLE else View.GONE
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            isLoading?.let {
                loadingViewProgressBar.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    textViewError.visibility = View.GONE
                    recyclerViewList.visibility = View.GONE

                }
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_detailsFragment_to_listFragment -> {
                view?.let { Navigation.findNavController(it).navigate(ListFragmentDirections.actionListFragmentToSettingsFragment()) }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}