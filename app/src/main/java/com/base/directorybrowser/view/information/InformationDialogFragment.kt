package com.base.directorybrowser.view.information

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.base.directorybrowser.R
import com.base.directorybrowser.base.adapter.RecyclerViewType
import com.base.directorybrowser.view.information.delegateAdapter.InformationDialogAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.information_fragment_parent.view.*

class InformationDialogFragment : BottomSheetDialogFragment() {

    private lateinit var currentView: View
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var informationDialogAdapter: InformationDialogAdapter
    private val informationDialogViewModel: InformationDialogViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheet = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view = View.inflate(context, R.layout.information_fragment_parent, null)
        view?.let {
            currentView = view
            initView()
        }
        bottomSheet.setContentView(view)
        return bottomSheet
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        informationDialogViewModel.typesMutable.observe(this, Observer(this::onTypesRecyclerChanged))
    }

    private fun initView(view: View = currentView) {
        setupRecycler()
        view.information_fragment_close_button?.setOnClickListener {
            dismiss()
        }
    }

    private fun onTypesRecyclerChanged(list: List<RecyclerViewType>?) {
        list?.let {
            informationDialogAdapter.setElements(it)
        }
    }

    private fun setupRecycler() {
        informationDialogAdapter = InformationDialogAdapter()
        val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(resources.getDrawable(R.drawable.adapter_separator, null))
        currentView.information_fragment_recycler_adapter?.adapter = informationDialogAdapter
        currentView.information_fragment_recycler_adapter?.addItemDecoration(itemDecorator)
    }

    companion object {
        const val BOTTOM_DIALOG_INFORMATION_FRAGMENT = "bottom_dialog_information_fragment"
    }
}