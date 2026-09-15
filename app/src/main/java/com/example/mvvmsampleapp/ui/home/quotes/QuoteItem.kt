package com.example.mvvmsampleapp.ui.home.quotes

import com.example.mvvmsampleapp.R
import com.example.mvvmsampleapp.data.db.entities.Quote
import com.example.mvvmsampleapp.databinding.ItemQuoteBinding
import com.xwray.groupie.databinding.BindableItem

class QuoteItem(
    private val quote: Quote
) : BindableItem<ItemQuoteBinding>() {

    override fun bind(viewBinding: ItemQuoteBinding, position: Int) {
        viewBinding.quote = quote
    }

    override fun getLayout(): Int = R.layout.item_quote
}
