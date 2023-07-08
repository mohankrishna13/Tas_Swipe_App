package com.mohankrishna.swipeapp.model.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.mohankrishna.swipeapp.R
import com.mohankrishna.swipeapp.databinding.SingleProductViewBinding
import com.mohankrishna.swipeapp.model.dataClass.Products
import com.squareup.picasso.Picasso

class ProductsAdapter(var productsList: ArrayList<Products>): RecyclerView.Adapter<ProductsAdapter.MyViewHolder>() {
    var dupProductList=ArrayList<Products>()
    var isDataNull=MutableLiveData<Boolean>()
    class MyViewHolder (var singleProductViewBinding: SingleProductViewBinding):
        RecyclerView.ViewHolder(singleProductViewBinding.root) {
        fun bind(item:Products){
            singleProductViewBinding.singleProductUIBinding=item
            if(!(item.image.isEmpty())){
                Picasso.get()
                    .load(item.image)
                    .resize(80, 90)
                    .error(R.drawable.demo)
                    .into(singleProductViewBinding.imageView)
            }else{
                //Default image
                Picasso.get()
                    .load(R.drawable.demo)
                    .resize(80, 90)
                    .error(R.drawable.demo)
                    .into(singleProductViewBinding.imageView)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        val binding=SingleProductViewBinding.inflate(inflater,parent,false)
        return MyViewHolder(binding)    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var item=productsList.get(position)
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    fun updateProductsList(it: ArrayList<Products>?) {
        if (it != null) {
            dupProductList=it
            productsList=it
        }
        notifyDataSetChanged()
    }

    fun filter(newText: String?) {
        var data=ArrayList<Products>()
        if(newText!=null){
            data = dupProductList.filter { item ->
                item.product_name.contains(newText, ignoreCase = true)
            } as ArrayList<Products>

            updateProductListWithFilter(data)
        }else{
            updateProductsList(dupProductList)
        }
    }

    private fun updateProductListWithFilter(data:ArrayList<Products>) {
        productsList=data
        if(data.size==0){
            isDataNull.postValue(true)
        }else{
            isDataNull.postValue(false)

        }
        notifyDataSetChanged()
    }
}