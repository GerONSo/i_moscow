package com.geron.ai_moscow_mobile.adapters

import androidx.recyclerview.widget.DiffUtil
import com.geron.ai_moscow_mobile.CallbackHelper
import com.geron.ai_moscow_mobile.DiffUtilCallback
import com.geron.ai_moscow_mobile.EventViewHolder
import com.geron.ai_moscow_mobile.ServerHelper
import com.geron.ai_moscow_mobile.data_classes.Account
import com.squareup.picasso.Picasso

class AllAccountsAdapter : EventsAdapter() {
    var allAccounts: List<Account> = listOf()

    fun updateAllAccountsList(accounts_: List<Account>) {
        val diffResult: DiffUtil.DiffResult =
            DiffUtil.calculateDiff(DiffUtilCallback(allAccounts, accounts_))
        this.allAccounts = accounts_
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currentAccount = allAccounts[position]
        holder.titleTextView?.text = currentAccount.name
//            holder.descriptionTextView?.text = currentAccount.metadata.description
        Picasso.get().load(ServerHelper.BASE_URL + currentAccount.photoLink)
            .into(holder.photoImageView)
        holder.view.setOnClickListener {
            CallbackHelper.onAccountClicked(position)
        }
    }

    override fun getItemCount(): Int {
        return allAccounts.size
    }
}