package com.instabug.androidtask.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<BINDING : ViewBinding?, VM : BaseViewModel?> : AppCompatActivity() {
    var viewModel: VM? = null
    var binding: BINDING? = null
    protected abstract fun createViewModel(): VM
    protected abstract fun createViewBinding(layoutInflater: LayoutInflater?): BINDING
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = createViewBinding(LayoutInflater.from(this))
        setContentView(binding!!.root)
        viewModel = createViewModel()
    }
}