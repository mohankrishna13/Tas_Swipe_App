package com.mohankrishna.swipeapp.UI.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.graphics.green
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohankrishna.swipeapp.R
import com.mohankrishna.swipeapp.UI.activities.AddProductsActivity
import com.mohankrishna.swipeapp.UI.activities.ProductScreenActivity
import com.mohankrishna.swipeapp.databinding.FragmentProductScreenBinding
import com.mohankrishna.swipeapp.model.adapters.ProductsAdapter
import com.mohankrishna.swipeapp.viewmodels.ProductsScreenViewModel


class ProductScreenFragment : Fragment() {
    private lateinit var productScreenBinding: FragmentProductScreenBinding
    private lateinit var viewModel: ProductsScreenViewModel
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var searchView: SearchView

    lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        productScreenBinding= FragmentProductScreenBinding.inflate(inflater,container,false)
        viewModel=ViewModelProvider(this).get(ProductsScreenViewModel::class.java)
        return productScreenBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productsAdapter= ProductsAdapter(arrayListOf())
        productScreenBinding.productsUIBinding=viewModel
        productScreenBinding.productsRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        productScreenBinding.productsRecyclerview.adapter=productsAdapter
        progressDialog = ProgressDialog(requireContext())

        //Check Internet to load data
        checkInternetConnectivity()
        //checkdata is fetched or not
        observeForPoductsList()
        //issue while fetching
        observeForErrorToast()
        //Observe for navigation
        observeForNavigation()
        //observeForReload
        observeForRefresh()
        //observeForEmptyData()
        observeForEmptyData()

    }

    private fun observeForEmptyData() {
        productsAdapter.isDataNull.observe(requireActivity(), Observer {
            if(it){
               showNODataLayout()
            }else{
                showMainLayout()
            }
        })
    }

    private fun observeForRefresh() {
        viewModel.isRefresh.observe(requireActivity(), Observer {
            if(it){
                //Clearing search text
                searchView.setQuery("",false)
                searchView.clearFocus()
                //check internet
                checkInternetConnectivity()
            }
        })
    }

    private fun observeForNavigation() {
        viewModel.isNavigation.observe(requireActivity(), Observer {
            if(it){
              navigateToAddProductsScreen()
            }
        })
    }

    private fun navigateToAddProductsScreen() {
        var intent=Intent(requireContext(),AddProductsActivity::class.java)
        startActivity(intent)
    }

    private fun initProgressBar(title:String,message:String) {
        progressDialog.setTitle(title)
        progressDialog.setMessage(message)
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    private fun observeForErrorToast() {
        viewModel.error_Message.observe(requireActivity(), Observer {
            progressDialog.dismiss()
            showToastMessage(it)
        })
    }

    private fun showToastMessage(it: String?) {
        Toast.makeText(requireContext(),it,Toast.LENGTH_LONG).show()
    }

    private fun checkInternetConnectivity() {
        (activity as ProductScreenActivity?)?.internetIsConnected?.observe(requireActivity(), Observer {
            if(it){
                setHasOptionsMenu(true)
                callFetchDataMethod()
            }else{
                if(viewModel.productsList.value?.size==0 || viewModel.productsList.value==null){
                   showInternetLayout()
                }else{
                    showToastMessage("Please check Internet Connection.")
                }
            }
        })
    }

    private fun callFetchDataMethod() {
        initProgressBar("Please Wait","Data Fetching")
        showMainLayout()
        viewModel.getProducts()
    }

    private fun showInternetLayout() {
        productScreenBinding.internetLayout.visibility=View.VISIBLE
        productScreenBinding.IntroScreen.visibility=View.GONE
        productScreenBinding.mainLayout.visibility=View.GONE
    }

    private fun observeForPoductsList() {
        viewModel.productsList.observe(requireActivity(), Observer {
            progressDialog.dismiss()
            if(it.size==0){
                showNODataLayout()
            }else{
                showMainLayout()
            }
            productsAdapter.updateProductsList(it)
        })
    }

    private fun showMainLayout() {
        productScreenBinding.noData.visibility=View.GONE
        productScreenBinding.IntroScreen.visibility=View.GONE
        productScreenBinding.internetLayout.visibility=View.GONE
        productScreenBinding.mainLayout.visibility=View.VISIBLE
        productScreenBinding.productsRecyclerview.visibility=View.VISIBLE

    }

    private fun showNODataLayout() {
        productScreenBinding.noData.visibility=View.VISIBLE
        productScreenBinding.IntroScreen.visibility=View.GONE
        productScreenBinding.internetLayout.visibility=View.GONE
        productScreenBinding.productsRecyclerview.visibility=View.GONE
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_option, menu)

        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                productsAdapter.filter(newText)
                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)

    }
}