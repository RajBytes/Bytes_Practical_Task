package com.bytes.app.bind

import android.annotation.SuppressLint
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bytes.app.R
import com.bytes.app.databinding.RowItemHomeListBinding
import com.bytes.app.ui.home.Product
import com.bytes.app.ui.home.ProductFavoriteClickListener
import java.io.File

/**
 * Bind data used for data binding
 */
class BindAdapters {
    companion object {


        /**
         * This method is used for binding the list of products in to the RecycleView using Generic Adapter.
         * Generic Adapter that we developed with respect to that by which we just have to pass the list of Objects
         * and layout other thing will be managed by some minor changes.
         */
        @BindingAdapter(value = ["bind:vendor_items_list", "bind:callback"], requireAll = false)
        @JvmStatic
        fun bindVendorItemList(
            view: RecyclerView,
            subscription_meal_items_list: ArrayList<*>?,
            callback: ProductFavoriteClickListener?
        ) {
            if (subscription_meal_items_list != null) {
                val adapter = object :
                    GenericRecyclerViewAdapterWithFilter<Product, RowItemHomeListBinding>(
                        view.context,
                        subscription_meal_items_list as ArrayList<Product>
                    ) {
                    override val layoutResId: Int
                        get() = R.layout.row_item_home_list

                    override fun onBindData(
                        model: Product,
                        position: Int,
                        dataBinding: RowItemHomeListBinding
                    ) {
                        dataBinding.model = model
                        dataBinding.ivFavourite.setOnClickListener {
                            if (callback != null) {
                                callback.favoriteClick(model)
                            }
                            notifyItemChanged(position)
                        }
                        dataBinding.executePendingBindings()
                    }

                    override fun onItemClick(model: Product, position: Int) {
                    }

                    override fun onFilterQueryReceived(query: CharSequence?): ArrayList<Product> {
                        if (query.isNullOrEmpty()) {
                            return mArrayList!!
                        } else {
                            val filteredList: ArrayList<Product> = ArrayList()
                            for (row in mArrayList!!) {
                                if (row.product_Name!!.toLowerCase()
                                        .contains(query.toString().toLowerCase())
                                ) {
                                    filteredList.add(row)
                                }
                            }
                            return filteredList
                        }
                    }

                    override fun onFilterComplete(
                        query: CharSequence?,
                        result: ArrayList<Product>
                    ) {
                        mFilterList = result
                        notifyDataSetChanged()
                    }
                }
                view.adapter = adapter
            }
        }


        /**
         * This is the method where we are doing a local filter based on the Search by user.
         * Also Updating the RecycleView to display filtered list.
         * @param view  View that one we need to notify.
         * @param searchValue Search String based on which we have to filter.
         */
        @BindingAdapter(value = ["bind:filterValue"], requireAll = false)
        @JvmStatic
        fun filterValue(
            view: RecyclerView,
            searchValue: String?
        ) {
            val adapter = view.adapter as GenericRecyclerViewAdapterWithFilter<*, *>?
            adapter?.let { it.filter.filter(searchValue ?: "") }
        }


        /**
         * This method is used for the notifying the RecycleView.
         * @param view  View that one we need to notify.
         * @param isNotify if flag is tru that we will notify the view about some update.
         */
        @BindingAdapter(value = ["bind:notifyData"], requireAll = false)
        @JvmStatic
        fun notifyValue(
            view: RecyclerView,
            isNotify: Boolean?
        ) {

            if (isNotify == true) {
                val adapter = view.adapter as GenericRecyclerViewAdapterWithFilter<*, *>?
                adapter?.notifyDataSetChanged()
            }
        }

        /**
         * Load Image in imageview using Glide
         * @param imageView AppCompatImageView imageview object
         * @param url String? URL
         */
        @SuppressLint("CheckResult")
        @BindingAdapter(value = ["bind:imageSet", "bind:placeHolder"], requireAll = false)
        @JvmStatic
        fun bindImageData(imageView: AppCompatImageView, url: String?, placeHolder: Drawable?) {
            if (url == null || url.isEmpty()) {
                if (placeHolder != null) {
                    imageView.setImageDrawable(placeHolder)
                } else {
                    imageView.setImageResource(R.drawable.ic_launcher_background)
                }
                return
            } else {
                var placeHolder1: Drawable =
                    ContextCompat.getDrawable(
                        imageView.context,
                        R.drawable.ic_launcher_background
                    )!!
                if (placeHolder != null)
                    placeHolder1 = placeHolder
                val requestOptions = RequestOptions()
                requestOptions.placeholder(placeHolder1)
                requestOptions.error(placeHolder1)
                requestOptions.dontAnimate()
                requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
                when {
                    url.contains("https://") || url.contains("http://") -> {
                        Glide.with(imageView.context).setDefaultRequestOptions(requestOptions)
                            .load(url)
                            .into(imageView)
                    }
                    url.contains("R.drawable") -> {
                        Glide.with(imageView.context).setDefaultRequestOptions(requestOptions).load(
                            imageView.context.resources.getIdentifier(
                                url.replace(
                                    "R.drawable.",
                                    ""
                                ), "drawable", imageView.context.packageName
                            )
                        )
                            .into(imageView)
                    }
                    else -> {
                        Glide.with(imageView.context).setDefaultRequestOptions(requestOptions)
                            .load(File(url)).into(imageView)
                    }
                }
            }
        }

        /**
         * This method is used for the Striking the TextView like display the Price with Dash one.
         * @param view TextView on which wehave to apply Dash Price.
         * @param isStrikeThruText Boolean flag on which we are deciding to apply the dash view.
         */
        @JvmStatic
        @BindingAdapter("isStrikeThruText")
        fun isStrikeThruText(
            view: TextView,
            isStrikeThruText: Boolean
        ) {
            if (isStrikeThruText) {
                view.paintFlags = view.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
        }

    }
}
