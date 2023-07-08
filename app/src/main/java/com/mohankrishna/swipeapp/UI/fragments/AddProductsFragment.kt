package com.mohankrishna.swipeapp.UI.fragments

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mohankrishna.swipeapp.R
import com.mohankrishna.swipeapp.UI.activities.AddProductsActivity
import com.mohankrishna.swipeapp.databinding.FragmentAddProductsBinding
import com.mohankrishna.swipeapp.viewmodels.AddProductsViewModel


class AddProductsFragment: Fragment() {

    private lateinit var addProductsBinding: FragmentAddProductsBinding
    private lateinit var addProductsViewModel: AddProductsViewModel
    lateinit var progressDialog: ProgressDialog
    private var imagePickerLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imagePickerLauncher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                var filePaths=ArrayList<Uri>()
                // Handle the selected image URIs
                val data = result.data
                if (data != null) {
                    val clipData = data.clipData
                    if (clipData != null) {
                        val count = clipData.itemCount
                        for (i in 0 until count) {
                            val imageUri = clipData.getItemAt(i).uri
                            filePaths.add(Uri.parse(getImagePath(imageUri)))
                        }
                        addProductsViewModel.setImagesPath(filePaths)
                        addProductsBinding.countOfImages.text="Total "+count+" images Selected"
                    } else {
                        val imageUri = data.data
                        if (imageUri != null) {
                            filePaths.add(imageUri)
                        }
                        addProductsViewModel.setImagesPath(filePaths)
                        addProductsBinding.countOfImages.text="Total 1 image Selected"
                    }
                }
            }
        }

    }

    private fun getImagePath(imageUri: Uri?): String? {
        var imagePath: String? = null
        if (imageUri != null) {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            try {
                requireActivity().contentResolver.query(imageUri, projection, null, null, null)
                    .use { cursor ->
                        if (cursor != null && cursor.moveToFirst()) {
                            val columnIndex =
                                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                            imagePath = cursor.getString(columnIndex)
                        }
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return imagePath
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addProductsBinding= FragmentAddProductsBinding.inflate(inflater,container,false)
        addProductsViewModel=ViewModelProvider(this).get(AddProductsViewModel::class.java)

        return addProductsBinding.root
    }
    private fun initProgressBar(title:String,message:String) {
        progressDialog.setTitle(title)
        progressDialog.setMessage(message)
        progressDialog.setCancelable(false)
        progressDialog.show()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().actionBar?.setDisplayHomeAsUpEnabled(true)
        requireActivity().actionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)

        addProductsBinding.addProductsUI=addProductsViewModel
        progressDialog=ProgressDialog(requireContext())
        //Images Selection
        observeForImageSelection()
        //Upload Started
        observeForProcessIsStarted()
        //Toast Messages
        observeForProcessToastMessage()

        //ObserveForInternet
        observeForInternet()

        //observerForSuccess
        observeIsSuccess()
        addProductsBinding.productSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCountry = addProductsViewModel.productTypes[position]
                addProductsViewModel.productType.value = selectedCountry
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun observeForInternet() {
        (activity as AddProductsActivity?)?.internetConnection?.observe(requireActivity(),
            Observer {
                addProductsViewModel.setInternetConnection(it)
            })

    }

    private fun observeIsSuccess() {
        addProductsViewModel.isSuccess.observe(requireActivity(), Observer {
            progressDialog.dismiss()
            requireActivity().finish()
        })
    }

    private fun observeForProcessToastMessage() {
        addProductsViewModel.toast_msg.observe(requireActivity(), Observer {
            progressDialog.dismiss()
            Toast.makeText(requireContext(),it,Toast.LENGTH_LONG).show()
        })
    }

    private fun observeForProcessIsStarted() {
        addProductsViewModel.isProcessStart.observe(requireActivity(), Observer {
            if(it){
                initProgressBar("Please Wait","Uploading Data")
            }
        })
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun observeForImageSelection() {
        addProductsViewModel.pickImages.observe(requireActivity(), Observer {
            pickImages()
        })

    }

    private fun pickImages() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        imagePickerLauncher!!.launch(intent)
    }
}