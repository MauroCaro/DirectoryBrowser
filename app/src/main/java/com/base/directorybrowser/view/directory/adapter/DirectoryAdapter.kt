package com.base.directorybrowser.view.directory.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.base.directorybrowser.R
import com.base.directorybrowser.view.directory.DirectoryFileUiModel
import com.base.directorybrowser.view.directory.DirectoryFolderUiModel
import com.base.directorybrowser.view.directory.DirectoryUiModel
import kotlinx.android.synthetic.main.directory_item.view.*

class DirectoryAdapter(
    private val folderItemListener: ((path: String) -> Unit),
    private val fileItemListener: ((item: DirectoryFileUiModel) -> Unit),
    private val informationItemListener: ((item: DirectoryUiModel) -> Unit)
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = mutableListOf<DirectoryUiModel>()

    fun setElements(newListOfItems: List<DirectoryUiModel>) {
        items.clear()
        items.addAll(newListOfItems)
        notifyDataSetChanged()
    }

    fun getElements(): List<DirectoryUiModel> = items

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.directory_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(holder.itemView) {
            items[position].let { item ->
                directory_item_information.setOnClickListener {
                    informationItemListener.invoke(item)
                }
                when (item) {
                    is DirectoryFileUiModel -> {
                        directory_item_image?.setBackgroundResource(item.drawableImage)
                        directory_item_text.text = item.name
                        directory_item_image.setOnClickListener {
                            fileItemListener.invoke(item)
                        }
                    }
                    is DirectoryFolderUiModel -> {
                        directory_item_image?.setBackgroundResource(item.drawableImage)
                        directory_item_text.text = item.name
                        directory_item_information.visibility = View.GONE
                        directory_item_image.setOnClickListener {
                            folderItemListener.invoke(item.path)
                        }
                    }
                    else -> {
                        //Do nothing
                    }
                }
            }
        }
    }

    inner class ViewHolder(
        viewHolder: View
    ) : RecyclerView.ViewHolder(viewHolder)

    companion object {

    }
}