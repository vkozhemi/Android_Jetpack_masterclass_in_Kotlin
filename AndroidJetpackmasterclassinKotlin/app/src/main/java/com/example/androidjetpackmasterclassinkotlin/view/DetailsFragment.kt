package com.example.androidjetpackmasterclassinkotlin.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.androidjetpackmasterclassinkotlin.R
import com.example.androidjetpackmasterclassinkotlin.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.fragment_list.*


class DetailsFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel
    private var uid = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        viewModel.fetch()

        arguments?.let {
            uid = DetailsFragmentArgs.fromBundle(it).uid
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.dogLiveData.observe(viewLifecycleOwner, Observer { dog ->
            dog?.let {
                nameDetail.text = dog.breed
                purposeDetail.text = dog.breedFor
                temperamentDetail.text = dog.temperament
                lifeSpanDetail.text = dog.lifeSpan
            }
        })
    }
}