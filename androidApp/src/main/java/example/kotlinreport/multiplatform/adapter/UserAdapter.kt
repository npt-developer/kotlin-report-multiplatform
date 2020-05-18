package example.kotlinreport.multiplatform.adapter

import android.content.Context
import android.content.ContextWrapper
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import example.kotlinreport.multiplatform.R
import example.kotlinreport.multiplatform.shared.config.AppConfig
import example.kotlinreport.multiplatform.shared.model.SexType
import example.kotlinreport.multiplatform.shared.model.User
import java.io.File
import java.io.FileInputStream

abstract class UserAdapter(var mList: ArrayList<User?>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        @JvmStatic
        private val VIEW_ITEM: Int = 0
        @JvmStatic
        private val VIEW_LOADING: Int = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder
        if (viewType == VIEW_ITEM) {
            var itemView: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_holder_user, parent, false)
            viewHolder = UserViewHolder(itemView)
        } else {
            var itemView: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_holder_progress_bar, parent, false)
            viewHolder = LoadingViewHolder(itemView)
        }

        return viewHolder
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun getItemViewType(position: Int): Int {
        if (mList.get(position) == null) {
            return VIEW_LOADING
        }
        return VIEW_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is UserViewHolder) {
            val user: User = mList.get(position)!!

            holder.bindView(user)
        } else if (holder is LoadingViewHolder) {
            holder.bindView()
        }
    }

    fun clear(): Unit {
        mList.clear()
        this.notifyDataSetChanged()
    }

    fun updateItem(user: User, position: Int) {
        mList[position] = user
        this.notifyItemChanged(position)
    }

    fun removeItem(position: Int) {
        mList.removeAt(position)
        this.notifyItemRemoved(position)
    }

    fun addAll(listAdd: ArrayList<User>) {
        val start: Int = mList.size
        val count: Int = listAdd.size
        mList.addAll(listAdd)
        notifyItemRangeInserted(start, count)
    }

    fun openLoading() {
        mList.add(null)
        notifyItemInserted(mList.size - 1)
    }

    fun closeLoading() {
        mList.removeAt(mList.size - 1)
        notifyItemRemoved(mList.size - 1)
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // swipe
        var mLinearLayout: LinearLayout
        // item
        var mViewContent: ConstraintLayout
        var mTextViewName: TextView

        init {
            mLinearLayout = itemView.findViewById(R.id.viewHolderUserLinearLayout)

            mViewContent = itemView.findViewById(R.id.viewHolderUserContent)
            mTextViewName = itemView.findViewById(R.id.textViewViewHolderUserName)
        }

        fun bindView(user: User) {
            mTextViewName.text = "Name: " + user.name + ". Sex: " + SexType.getLabel(user.sex)
        }

    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mProgressBar: ProgressBar

        init {
            mProgressBar = itemView.findViewById(R.id.progressBarViewHolder)
        }

        fun bindView() {
            mProgressBar.isIndeterminate = true
        }
    }
}
