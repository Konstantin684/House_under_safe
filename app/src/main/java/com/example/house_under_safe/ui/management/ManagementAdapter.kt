package com.example.house_under_safe.ui.management

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.house_under_safe.R

class ManagementAdapter(
    private val context: Context,
    private val items: List<ManagementItem>
) : ArrayAdapter<ManagementItem>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position)
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.management_fragment_list, parent, false)

        val icon = view.findViewById<ImageView>(R.id.property_icon)
        val title = view.findViewById<TextView>(R.id.property_title)
        val description = view.findViewById<TextView>(R.id.property_description)

        icon.setImageResource(item!!.iconResId)
        title.text = item.title
        description.text = item.description

        return view
    }
}
