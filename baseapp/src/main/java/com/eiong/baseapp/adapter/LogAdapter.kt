package com.eiong.baseapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.style.BackgroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eiong.baseapp.databinding.ItemLogBinding

/**
 * 日志
 *
 * @author EIong
 */
class LogAdapter(private val context: Context, private var data: MutableList<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var searchKey = "" // 搜索关键字
    var searchPosition = -1 // 搜索匹配的位置

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return object :
            RecyclerView.ViewHolder(ItemLogBinding.inflate(LayoutInflater.from(context)).root) {}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder.itemView as TextView).apply {
            data[position].apply {
                text = this
                setTextColor(if (length > 13 && get(13) == 'E') Color.RED else Color.BLACK)

                if (position == searchPosition) {
                    text = SpannableString(this).apply {
                        var start = this.indexOf(searchKey)
                        var end = start + searchKey.length

                        while (start > 0) {
                            setSpan(
                                BackgroundColorSpan(Color.parseColor("#CCCCCC")),
                                start,
                                end,
                                SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                            if (end >= length - 1) {
                                break
                            }
                            start = this.indexOf(searchKey, end)
                            end = start + searchKey.length
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    /**
     * 设置数据
     *
     * @param data 数据
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: MutableList<String>) {
        this.data = data
        notifyDataSetChanged()
    }

    /**
     * 设置搜索结果
     *
     * @param searchKey      关键字
     * @param searchPosition 匹配的位置
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setSearchResult(searchKey: String, searchPosition: Int) {
        this.searchKey = searchKey
        this.searchPosition = searchPosition
        notifyDataSetChanged()
    }
}