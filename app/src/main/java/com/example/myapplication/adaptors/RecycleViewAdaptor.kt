package com.example.myapplication.adaptors

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.dataClass.ItemClass

class RecycleViewAdaptor(private val dataSet: ArrayList<ItemClass>) :
    RecyclerView.Adapter<RecycleViewAdaptor.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nameText: TextView = view.findViewById(R.id.name_text)
        private val countText: TextView = view.findViewById(R.id.count_text)
        private val bestBeforeText: TextView = view.findViewById(R.id.best_before_text)
        init {
            // Define click listener for the ViewHolder's View.
        }
        fun bind(qrStorageItem:ItemClass){
            Log.d("debug1", qrStorageItem.toString())
            nameText.text = qrStorageItem.name
            countText.text = qrStorageItem.count.toString()
            bestBeforeText.text = qrStorageItem.best_before.toDate().toString()

        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
//        viewHolder.nameText.text = dataSet[position].name
//        Log.d("debug1", dataSet.toString())
        val qrStorageItem:ItemClass = dataSet[position]
        viewHolder.bind(qrStorageItem)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size


}