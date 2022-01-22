package com.instabug.androidtask.ui.main

import android.app.SearchManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.instabug.androidtask.R
import com.instabug.androidtask.data.DataManager
import com.instabug.androidtask.data.model.Word
import com.instabug.androidtask.data.repository.WordRepository
import com.instabug.androidtask.databinding.ActivityMainBinding
import com.instabug.androidtask.ui.base.BaseActivity


class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(), WordAdapter.WordListener {
    private var wordAdapter: WordAdapter? = null

    var searchText = ""
    override fun createViewModel(): MainViewModel {
        val wordRepository: WordRepository =
            DataManager.getInstance(context = applicationContext).wordRepository
        return ViewModelProvider(
            this,
            MainViewModelFactory(wordRepository)
        )[MainViewModel::class.java]
    }

    override fun createViewBinding(layoutInflater: LayoutInflater?): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        wordAdapter = WordAdapter(this)
        setSupportActionBar(binding!!.toolbar)
        binding!!.contentRV.layoutManager = LinearLayoutManager(this@MainActivity)
        binding!!.contentRV.adapter = wordAdapter

        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.requestNetwork(networkRequest, viewModel!!.networkCallback)
        observeViewModel()
        viewModel!!.loadWords()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        // Add SearchWidget.

        // Add SearchWidget.
        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchView: SearchView =
            menu!!.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(this@MainActivity, "Search for $query", Toast.LENGTH_SHORT).show()
                viewModel!!.loadWords(query!!)
                searchText = query
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel!!.loadWords(newText!!)
                searchText = newText
                return true
            }
        })

        searchView.setOnCloseListener {
            viewModel!!.loadWords("")
            true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.search -> {

            }
            R.id.sort -> {
                if (viewModel!!.sortBy == "ASC") {
                    viewModel!!.sortBy = "DESC"

                    Toast.makeText(
                        this@MainActivity,
                        "The list is sorted by Descending",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    viewModel!!.sortBy = "ASC"
                    Toast.makeText(
                        this@MainActivity,
                        "The list is sorted by Ascending",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                viewModel!!.loadWords(searchText)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun observeViewModel() {
        viewModel!!.loadingLiveData.observe(
            this,
            {
                if (it) {
                    binding!!.progressBar.visibility = View.VISIBLE
                } else {
                    binding!!.progressBar.visibility = View.GONE
                    if (binding!!.swipeRefresher.isRefreshing) {
                        binding!!.swipeRefresher.isRefreshing = false
                    }
                }

                binding!!.resultTV.visibility = View.GONE

            })

        viewModel!!.wordsLiveData.observe(this, { words ->
            binding!!.contentRV.visibility = View.VISIBLE
            binding!!.resultTV.visibility = View.GONE
            wordAdapter!!.setItems(words!!.toMutableList())

        })

        viewModel!!.onlineLiveData.observe(this, { flag ->
            if (flag) {
                binding!!.connectionTextView.visibility = View.GONE
                viewModel!!.loadWords()
            } else {
                binding!!.connectionTextView.visibility = View.VISIBLE
            }
        })

        viewModel!!.showErrorMessageLiveData.observe(
            this,
            { message ->
                binding!!.resultTV.visibility = View.VISIBLE
                binding!!.resultTV.text = message

                binding!!.contentRV.visibility = View.GONE
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
            })

        binding!!.swipeRefresher.setOnRefreshListener {
            viewModel!!.loadWords(searchText)
        }
    }

    override fun onWordClicked(word: Word?) {
        Toast.makeText(this@MainActivity, word.toString(), Toast.LENGTH_SHORT).show()
    }

}