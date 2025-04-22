package com.example.house_under_safe.policy_design.step_5

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Log
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.house_under_safe.MainActivity
import com.example.house_under_safe.PolicyStorageHelper
import com.example.house_under_safe.R
import com.example.house_under_safe.model.DocumentType
import com.example.house_under_safe.model.InsurancePolicy
import com.example.house_under_safe.model.PolicyStatus
import com.example.house_under_safe.model.PropertySubtype
import com.example.house_under_safe.model.PropertyType
import com.example.house_under_safe.policy_design.DesignPolicyActivity
import com.example.house_under_safe.policy_design.PolicyDesignViewModel
import com.example.house_under_safe.ui.home.HomeItemUiModel
import com.example.house_under_safe.ui.home.InsuranceRiskUi
import com.example.house_under_safe.ui.home.insuranceRiskIcons
import java.text.SimpleDateFormat
import java.util.*

class FiveStepFragment : Fragment(R.layout.fragment_five_step) {

    private lateinit var viewModel: PolicyDesignViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[PolicyDesignViewModel::class.java]

        val property = viewModel.propertyInfo.value
        val insurer = viewModel.insurerInfo.value
        val conditions = viewModel.insuranceConditions.value
        val documents = viewModel.documents.value ?: emptyList()

        // === 1. Данные о недвижимости ===
        view.findViewById<TextView>(R.id.type_nedvij).text = property?.propertyType?.javaClass?.simpleName.orEmpty()
        view.findViewById<TextView>(R.id.region_city).text = property?.region.orEmpty()
        view.findViewById<TextView>(R.id.adress_nedvij).text = property?.address.orEmpty()
        view.findViewById<TextView>(R.id.plochad_nedvij).text = property?.totalArea?.toString().orEmpty()
        updateField(view, R.id.kol_etaj, R.id.textView27, property?.floorsTotal)
        updateField(view, R.id.etaj, R.id.textView28, property?.floor)
        updateField(view, R.id.god_postroiki, R.id.textView29, property?.yearBuilt)
        updateField(view, R.id.kadastrovi_number, R.id.textView30, property?.cadastralNumber)

        // === 2. Страхователь ===
        view.findViewById<TextView>(R.id.pasport_vydan).text = insurer?.passportVydan.orEmpty()
        view.findViewById<TextView>(R.id.date_vidachi).text = insurer?.dateVydachi.orEmpty()
        view.findViewById<TextView>(R.id.kod_podrazdeleny).text = insurer?.kodPodrazdel.orEmpty()
        view.findViewById<TextView>(R.id.seria_and_nomer).text = insurer?.passportNumber.orEmpty()
        view.findViewById<TextView>(R.id.familia).text = insurer?.lastname.orEmpty()
        view.findViewById<TextView>(R.id.name).text = insurer?.name.orEmpty()
        view.findViewById<TextView>(R.id.otchestvo).text = insurer?.otchestvo.orEmpty()
        view.findViewById<TextView>(R.id.date_birth).text = insurer?.dateBirth.orEmpty()
        view.findViewById<TextView>(R.id.place_birth).text = insurer?.placeBirth.orEmpty()
        view.findViewById<TextView>(R.id.adress_registred).text = insurer?.adressRegistry.orEmpty()
        view.findViewById<TextView>(R.id.adress_live).text = insurer?.adressLive.orEmpty()

        // === 3. Условия страхования ===
        conditions?.let {
            view.findViewById<TextView>(R.id.konstruction).text = it.constructionCost.toString()
            updateField(view, R.id.otdelka, R.id.textView46, it.finishingCost)
            updateField(view, R.id.technik_oborudovanie, R.id.textView47, it.equipmentCost)
            updateField(view, R.id.dvijimoe_imuchestvo, R.id.textView48, it.movablePropertyCost)
            updateField(view, R.id.grajdan_otvetstvennost, R.id.textView49, it.civilLiabilityCost)
            view.findViewById<TextView>(R.id.strah_risk).text =
                it.risks.joinToString(", ") { r -> r.label }
            view.findViewById<TextView>(R.id.srok_strah).text =
                "${it.insuranceTermInYears} ${getYearEnding(it.insuranceTermInYears)}"
            view.findViewById<TextView>(R.id.chastota_payment).text = it.paymentFrequency.label
        }

        // === 4. Документы ===
        val container = view.findViewById<LinearLayout>(R.id.cardContainer)
        val pdfImage = view.findViewById<ImageView>(R.id.imageView6)

        documents.filter { it.type == DocumentType.PHOTO }.forEach { doc ->
            val image = ImageView(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(300, 300).apply {
                    setMargins(8, 8, 8, 8)
                }
                scaleType = ImageView.ScaleType.CENTER_CROP
                Glide.with(this).load(doc.url).into(this)
            }
            container.addView(image)
        }

        documents.find { it.type == DocumentType.POLICY_PDF }?.let {
            renderPdfPreview(Uri.parse(it.url), pdfImage)
        }

        // === Кнопка "Назад" ===
        view.findViewById<ImageButton>(R.id.imageButton3).setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
            (requireActivity() as? DesignPolicyActivity)?.updateProgress(3)
        }

        // === Кнопка "Подтвердить" ===
        view.findViewById<Button>(R.id.button).setOnClickListener {
            if (property != null && conditions != null && insurer != null) {

                val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                val issueDate = dateFormat.format(Date())
                val startDate = issueDate
                val endCalendar = Calendar.getInstance().apply {
                    time = dateFormat.parse(issueDate) ?: Date()
                    add(Calendar.YEAR, conditions.insuranceTermInYears)
                }
                val endDate = dateFormat.format(endCalendar.time)

                val policyNumber = generatePolicyNumber(property.propertyType)

                val insurancePolicy = InsurancePolicy(
                    policyNumber = policyNumber,
                    issueDate = issueDate,
                    startDate = startDate,
                    endDate = endDate,
                    status = PolicyStatus.ACTIVE,
                    insurer = insurer,
                    property = property,
                    conditions = conditions,
                    risks = conditions.risks,
                    documents = documents,
                    payments = emptyList()
                )

                // ⬇️ Сохраняем полноценную модель
                PolicyStorageHelper.saveFullPolicy(requireContext(), insurancePolicy)

                Log.d("FiveStepFragment", "Полис сохранён: $insurancePolicy")
                Toast.makeText(requireContext(), "Полис успешно сохранён!", Toast.LENGTH_SHORT).show()

                val intent = Intent(requireContext(), MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                }
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "Ошибка: данные не собраны", Toast.LENGTH_SHORT).show()
            }
        }


        (requireActivity() as? DesignPolicyActivity)?.updateProgress(5)
    }

    private fun getPlanIconRes(propertyType: PropertyType): Int {
        return when (propertyType) {
            is PropertyType.CityResidential -> R.drawable.gor_jil_ned
            is PropertyType.CountryResidential -> R.drawable.zagor_jil_ned
            is PropertyType.CountryNotResidential -> R.drawable.gor_nejil_ned
            is PropertyType.Commercial -> R.drawable.komer_ned
            is PropertyType.Industrial -> R.drawable.prom_ned
            PropertyType.ProdleniePolisa -> R.drawable.unchecked_profile
        }
    }

    private fun generatePolicyNumber(propertyType: PropertyType): String {
        val prefix = when (propertyType) {
            is PropertyType.CityResidential -> "CITY"
            is PropertyType.CountryResidential -> "COUNTRY_RES"
            is PropertyType.CountryNotResidential -> "COUNTRY_NONRES"
            is PropertyType.Commercial -> "COM"
            is PropertyType.Industrial -> "IND"
            PropertyType.ProdleniePolisa -> "EXT"
        }
        val timestamp = System.currentTimeMillis().toString().takeLast(6)
        return "$prefix-$timestamp"
    }

    private fun getYearEnding(years: Int): String = when (years) {
        1 -> "год"
        2, 3, 4 -> "года"
        else -> "лет"
    }

    private fun updateField(view: View, valueId: Int, labelId: Int, value: Any?) {
        if (value != null) {
            val text = view.findViewById<TextView>(valueId)
            val label = view.findViewById<TextView>(labelId)
            text.text = value.toString()
            text.visibility = View.VISIBLE
            label.visibility = View.VISIBLE
        }
    }

    private fun renderPdfPreview(uri: Uri, imageView: ImageView) {
        val contentResolver = requireContext().contentResolver
        val fileDescriptor = contentResolver.openFileDescriptor(uri, "r")?.fileDescriptor ?: return
        val pdfRenderer = PdfRenderer(ParcelFileDescriptor.dup(fileDescriptor))
        val page = pdfRenderer.openPage(0)
        val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        page.close()
        pdfRenderer.close()
        imageView.setImageBitmap(bitmap)
    }
}
