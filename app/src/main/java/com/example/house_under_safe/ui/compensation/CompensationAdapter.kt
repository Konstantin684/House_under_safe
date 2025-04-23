package com.example.house_under_safe.ui.compensation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.house_under_safe.R
import com.example.house_under_safe.model.PropertyType
import com.example.house_under_safe.ui.home.HomeItemUiModel

class CompensationAdapter(
    private var items: List<HomeItemUiModel>
) : RecyclerView.Adapter<CompensationAdapter.CompensationViewHolder>() {

    inner class CompensationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val numberPolice: TextView = view.findViewById(R.id.number_polise)
        val region: TextView = view.findViewById(R.id.region_polise)
        val typeRealEstate: TextView = view.findViewById(R.id.type_real_estate)
        val adres: TextView = view.findViewById(R.id.adres_real_estate)
        val period: TextView = view.findViewById(R.id.validaty_period)
        val plan: ImageView = view.findViewById(R.id.imageView_plan)
        val risksContainer: LinearLayout = view.findViewById(R.id.risksContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompensationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_compensation, parent, false)
        return CompensationViewHolder(view)
    }

    override fun onBindViewHolder(holder: CompensationViewHolder, position: Int) {
        val item = items[position]

        holder.numberPolice.text = item.policyNumber
        holder.region.text = item.region
        holder.typeRealEstate.text = item.subtype.label  // 🔄 напрямую обращаемся к label
        holder.adres.text = item.address
        holder.period.text = item.period
        holder.plan.setImageResource(item.planResId)

        holder.risksContainer.removeAllViews()

        item.risks.forEach { risk ->
            val riskView = LayoutInflater.from(holder.itemView.context)
                .inflate(R.layout.item_risk, holder.risksContainer, false)

            val icon = riskView.findViewById<ImageView>(R.id.riskIcon)
            val label = riskView.findViewById<TextView>(R.id.riskLabel)

            icon.setImageResource(risk.iconRes)
            label.text = risk.risk.label

            holder.risksContainer.addView(riskView)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<HomeItemUiModel>) {
        items = newItems
        notifyDataSetChanged()
    }

    fun Enum<*>.getLabel(): String = when (this) {
        is PropertyType.CityResidential.Subtype -> label
        is PropertyType.CountryResidential.Subtype -> label
        is PropertyType.CountryNotResidential.Subtype -> label
        is PropertyType.Commercial.Subtype -> label
        is PropertyType.Industrial.Subtype -> label
        else -> name
    }
}
