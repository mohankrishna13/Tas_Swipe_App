package com.mohankrishna.swipeapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mohankrishna.swipeapp.model.dataClass.Products
import com.mohankrishna.swipeapp.model.retrofit.ApiInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse


class ProductsScreenViewModel:ViewModel() {
    var productsList=MutableLiveData<ArrayList<Products>>()
    var error_Message=MutableLiveData<String>()
    var isRefresh=MutableLiveData<Boolean>()
    var isNavigation=MutableLiveData<Boolean>()

    fun getProducts(){
        CoroutineScope(Dispatchers.IO).launch {
            try{
                var dataList=ApiInstance().apiService.getProducts()
                productsList.postValue(dataList)
            }catch (e:Exception){
                error_Message.postValue(e.message)
            }
            catch (t:Throwable){
                error_Message.postValue(t.message)
            }
        }
    }

    fun navigate(){
        isNavigation.postValue(true)
    }
    fun refresh(){
        isRefresh.postValue(true)
    }



}