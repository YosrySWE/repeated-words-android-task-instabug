package com.instabug.androidtask.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.instabug.androidtask.data.model.Word
import com.instabug.androidtask.databinding.ItemWordBinding
import java.util.*

class WordAdapter(private val listener: WordListener) :
    RecyclerView.Adapter<WordAdapter.WordViewHolder>() {
    interface WordListener {
        fun onWordClicked(word: Word?)
    }

    private var items: MutableList<Word>?
    fun setItems(items: MutableList<Word>?) {
        this.items?.clear()
        this.items = items
        notifyDataSetChanged()
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val binding = ItemWordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return items!!.size
    }

    private fun getItem(position: Int): Word {
        return items!![position]!!
    }

    inner class WordViewHolder internal constructor(var binding: ItemWordBinding) :
        RecyclerView.ViewHolder(
            binding.root
        ), View.OnClickListener {
        fun bind(position: Int) {
            val word = getItem(position)
            setClickListener(word)
            binding.wordTV.text = word.name
            binding.quantityTV.text = word.quantity.toString()
        }


        private fun setClickListener(word: Word) {
            itemView.tag = word
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            listener.onWordClicked(view.tag as Word)
        }
    }


    init {
        items = ArrayList()
    }
}