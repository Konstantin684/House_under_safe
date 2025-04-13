package com.example.house_under_safe.ui.compensation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.house_under_safe.R

import com.example.house_under_safe.ui.home.HomeItem
import com.example.house_under_safe.ui.home.RiskType

class CompensationAdapter (
    private val items: List<HomeItem>
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

        holder.numberPolice.text = item.numberPolice.toString()
        holder.region.text = item.locationRegion
        holder.typeRealEstate.text = item.typeRealEstate
        holder.adres.text = item.adresRealEstate
        holder.period.text = item.validatyPeriod
        holder.plan.setImageResource(item.planResId)

        holder.risksContainer.removeAllViews()

        item.risks.forEach { risk ->
            val riskView = LayoutInflater.from(holder.itemView.context)
                .inflate(R.layout.item_risk, holder.risksContainer, false)

            val icon = riskView.findViewById<ImageView>(R.id.riskIcon)
            val label = riskView.findViewById<TextView>(R.id.riskLabel)

            icon.setImageResource(risk.iconRes)
            label.text = risk.label

            holder.risksContainer.addView(riskView)
        }

    }

    override fun getItemCount(): Int = items.size
}