package com.example.androidjetpackmasterclassinkotlin.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.androidjetpackmasterclassinkotlin.R
import com.example.androidjetpackmasterclassinkotlin.databinding.ItemViewBinding
import com.example.androidjetpackmasterclassinkotlin.model.DogModel
import com.example.androidjetpackmasterclassinkotlin.util.getProgressDrawable
import com.example.androidjetpackmasterclassinkotlin.util.loadImage
import kotlinx.android.synthetic.main.item_view.view.*
import java.util.ArrayList

class ListAdapter(private val list: ArrayList<DogModel>) : RecyclerView.Adapter<ListAdapter.ViewHolder>(), DogClickListener {

    class ViewHolder(var view: ItemViewBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemViewBinding>(inflater, R.layout.item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.dog = list[position]
        holder.view.listener = this
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(newList: List<DogModel>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onDogClicked(v: View) {
        val uuid = v.dogId.text.toString().toInt()
        val action = ListFragmentDirections.actionListFragmentToDetailsFragment()
        action.uid = uuid
        Navigation.findNavController(v).navigate(action)
    }
}