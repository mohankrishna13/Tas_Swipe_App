package com.mohankrishna.swipeapp.viewmodels

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mohankrishna.swipeapp.model.retrofit.ApiInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class AddProductsViewModel:ViewModel() {
    var productName= MutableLiveData<String>()
    var productTax= MutableLiveData<String>()
    var productPrice= MutableLiveData<String>()
    var pickImages= MutableLiveData<Boolean>()
    var filePaths= ArrayList<Uri>()
    var toast_msg=MutableLiveData<String>()
    val productTypes = listOf("Food","Electronics","Servies")
    var productType= MutableLiveData<String>()

    var internet=MutableLiveData<Boolean>()

    var isProcessStart=MutableLiveData<Boolean>()
    var isSuccess=MutableLiveData<Boolean>()

    fun addProducts(){
        if(productName.value.isNullOrEmpty()){
            toast_msg.postValue("Please Enter Valid product name")
            return
        }
        if(productType.value.isNullOrEmpty()){
            toast_msg.postValue("Please Select Valid Product Type")
            return
        }
        if(productPrice.value.isNullOrEmpty()){
            toast_msg.postValue("Please Enter Valid Product Price")
            return
        }
        if(productTax.value.isNullOrEmpty()){
            toast_msg.postValue("Please Enter Valid Product Tax")
            return
        }
        callRetrofitPostMethod()
    }

    private fun callRetrofitPostMethod() {
        val handleCoroutine=CoroutineScope(Dispatchers.IO).launch {
            try {
                //Creating Multipart Body for images
                val emptyRequestBody = RequestBody.create(MediaType.parse("application/octet-stream"), ByteArray(0))
                var emptyFilePart = MultipartBody.Part.createFormData("emptyFile", "empty.png", emptyRequestBody)
                var listOfImagesSelected = ArrayList<MultipartBody.Part>()
                if(filePaths.size!=0){
                    listOfImagesSelected=createImagesRequestBody(filePaths)
                }else{
                    listOfImagesSelected.add(emptyFilePart)
                }

                //Hit the API
                var resultModel= ApiInstance().apiService.addProducts(productName.value.toString()
                    ,productType.value.toString(),productPrice.value.toString().toDouble(),
                    productTax.value.toString().toDouble(), listOfImagesSelected)


                //checking results
                if(resultModel.isSuccessful){
                    toast_msg.postValue(resultModel.body()?.message)
                    isSuccess.postValue(true)
                }else{
                   toast_msg.postValue(resultModel.errorBody().toString())
                }


            }catch (e:Exception){
                if(isProcessStart.value==true){
                    toast_msg.postValue(e.message.toString())
                }
            }
            catch (e:Throwable){
                if(isProcessStart.value==true){
                    toast_msg.postValue(e.message.toString())
                }
            }
        }
        //Checking internet
        if(internet.value==true){
            isProcessStart.postValue(true)
            handleCoroutine.start()
        }else{
            if(handleCoroutine.isActive && !(handleCoroutine.isCompleted)){
                toast_msg.postValue("Please check Internet Connection")
                isProcessStart.postValue(false)
                handleCoroutine.cancel()
            }
        }
    }

    fun createImagesRequestBody(uris: ArrayList<Uri>): ArrayList<MultipartBody.Part> {
        val imageParts = ArrayList<MultipartBody.Part>()

        for (uri in uris) {
            val file = File(uri.path)
            val requestBody = RequestBody.create(MediaType.parse("image/*"), file)
            val part = MultipartBody.Part.createFormData("images[]", file.name, requestBody)
            imageParts.add(part)
        }

        return imageParts
    }
    fun selectFiles(){
        pickImages.postValue(true)
    }

    fun setImagesPath(filePath: ArrayList<Uri>) {
        filePaths=filePath
    }

    fun setInternetConnection(it: Boolean?) {
        internet.postValue(it)
    }


}