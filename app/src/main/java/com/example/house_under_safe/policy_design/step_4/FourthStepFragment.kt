package com.example.house_under_safe.policy_design.step_4

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.house_under_safe.R
import com.example.house_under_safe.model.DocumentType
import com.example.house_under_safe.model.PolicyDocument
import com.example.house_under_safe.policy_design.DesignPolicyActivity
import com.example.house_under_safe.policy_design.PolicyDesignViewModel
import com.example.house_under_safe.policy_design.step_5.FiveStepFragment
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor


class FourthStepFragment : Fragment() {

    private lateinit var viewModel: PolicyDesignViewModel
    private lateinit var cardContainer: LinearLayout
    private lateinit var pdfCard: ImageView

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        uris?.forEach { uri ->
            addImageCard(uri)
            addDocumentToViewModel(uri, DocumentType.PHOTO)
        }
    }

    private val pdfPickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            renderPdfPreview(it, pdfCard)
            addDocumentToViewModel(it, DocumentType.POLICY_PDF)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_fourth_step, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[PolicyDesignViewModel::class.java]
        cardContainer = view.findViewById(R.id.cardContainer)
        val addCardButton = cardContainer.findViewById<ImageView>(R.id.imageView9)
        pdfCard = view.findViewById(R.id.imageView10)

        addCardButton.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        pdfCard.setOnClickListener {
            pdfPickerLauncher.launch("application/pdf")
        }

        restoreDocuments()

        view.findViewById<ImageButton>(R.id.imageButton3).setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
            (requireActivity() as? DesignPolicyActivity)?.updateProgress(2)
        }

        view.findViewById<ImageButton>(R.id.imageButton4).setOnClickListener {
            (requireActivity() as? DesignPolicyActivity)?.updateProgress(4)

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, FiveStepFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun addImageCard(uri: Uri) {
        val cardView = layoutInflater.inflate(R.layout.item_image_card, cardContainer, false)
        val imageView = cardView.findViewById<ImageView>(R.id.cardImage)
        imageView.setImageURI(uri)

        cardContainer.addView(cardView, cardContainer.childCount - 1)
    }

    private fun showPdfInfo(uri: Uri) {
        Toast.makeText(requireContext(), "Загружен PDF: ${uri.lastPathSegment}", Toast.LENGTH_SHORT).show()
    }

    private fun addDocumentToViewModel(uri: Uri, type: DocumentType) {
        val currentList = viewModel.documents.value?.toMutableList() ?: mutableListOf()
        currentList.add(
            PolicyDocument(
                name = uri.lastPathSegment ?: "Документ",
                type = type,
                url = uri.toString()
            )
        )
        viewModel.documents.value = currentList
    }

    private fun restoreDocuments() {
        viewModel.documents.value?.forEach { document ->
            when (document.type) {
                DocumentType.PHOTO -> {
                    addImageCard(Uri.parse(document.url))
                }
                DocumentType.POLICY_PDF -> {
                    Toast.makeText(requireContext(), "PDF уже загружен: ${document.name}", Toast.LENGTH_SHORT).show()
                }
            }
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
