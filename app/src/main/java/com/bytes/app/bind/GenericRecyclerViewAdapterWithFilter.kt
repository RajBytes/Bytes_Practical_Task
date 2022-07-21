package com.bytes.app.bind

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Generic Adapter for set recycler view
 * @param T Response model
 * @param D Item Layout binding class
 * @property context Context object
 * @property mArrayList ArrayList<T>? model type of list
 * @property layoutResId Int layout name
 * @constructor
 */
abstract class GenericRecyclerViewAdapterWithFilter<T, D>(
    val context: Context,
    var mArrayList: ArrayList<T>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    var mFilterList: ArrayList<T>? = arrayListOf()

    init {
        mFilterList = mArrayList
    }

    abstract val layoutResId: Int

    abstract fun onBindData(model: T, position: Int, dataBinding: D)

    abstract fun onItemClick(model: T, position: Int)

    abstract fun onFilterQueryReceived(query: CharSequence?): ArrayList<T>

    abstract fun onFilterComplete(query: CharSequence?, result: ArrayList<T>)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val dataBinding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layoutResId,
            parent,
            false
        )
        return ItemViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindData(
            mFilterList!![position], holder.layoutPosition,
            dataBinding = (holder as GenericRecyclerViewAdapterWithFilter<*, *>.ItemViewHolder).mDataBinding as D
        )

        (holder.mDataBinding as ViewDataBinding).root.setOnClickListener {
            onItemClick(
                mFilterList!![position],
                position
            )
        }
    }


    override fun getItemCount(): Int {
        return mFilterList?.size ?: 0
    }

    fun addItems(arrayList: ArrayList<T>) {
        mArrayList = arrayList
        mFilterList = mArrayList
        this.notifyDataSetChanged()
 }

    fun updateItem(model: T, position: Int) {
        mArrayList?.set(mArrayList?.indexOf(mFilterList?.get(position))!!,model)
        mFilterList?.set(position, model)

        this.notifyItemChanged(position)
    }

    //pending to update filterlist in this method
    fun addItemsRange(arrayList: ArrayList<T>, startSize: Int) {
        this.mArrayList = arrayList
        this.notifyItemRangeChanged(startSize, arrayList.size)
    }

    fun getItem(position: Int): T {
        return mFilterList!![position]
    }

    internal inner class ItemViewHolder(binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var mDataBinding: D = binding as D
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                filterResults.values = onFilterQueryReceived(constraint)
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                onFilterComplete(constraint, results?.values as ArrayList<T>)
            }
        }
    }
}
