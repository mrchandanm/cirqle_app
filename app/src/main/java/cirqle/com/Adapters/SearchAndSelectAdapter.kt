package cirqle.com.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import cirqle.com.R

class SearchAndSelectAdapter(context: Context, resource: Int, private val dataList: ArrayList<String>) :
    ArrayAdapter<String>(context, resource, dataList), Filterable {

    private val filteredList = ArrayList<String>()
    private val mContext: Context = context

    init {
        filteredList.addAll(dataList)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            val inflater = LayoutInflater.from(mContext)
            convertView = inflater.inflate(R.layout.row_item_autocomplete_text_layout, parent, false)
        }

        val textView = convertView!!.findViewById<TextView>(R.id.college_name)
        textView.text = getItem(position)

        return convertView
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val results = FilterResults()
                val suggestions = ArrayList<String>()

                if (constraint != null) {
                    for (item in dataList) {
                        if (item.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            suggestions.add(item)
                        }
                    }
                }

                results.values = suggestions
                results.count = suggestions.size
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                filteredList.clear()
                if (results != null && results.count > 0) {
                    @Suppress("UNCHECKED_CAST")
                    filteredList.addAll(results.values as ArrayList<String>)
                }
                notifyDataSetChanged()
            }
        }
    }

    override fun getCount(): Int {
        return filteredList.size
    }

    override fun getItem(position: Int): String? {
        return filteredList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}