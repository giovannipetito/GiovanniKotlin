package it.giovanni.kotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import it.giovanni.kotlin.customview.popup.CustomDialogPopup
import it.giovanni.kotlin.interfaces.IProgressLoader
import it.giovanni.kotlin.R
import it.giovanni.kotlin.activities.MainActivity

abstract class BaseFragment(private var sectionType: SectionType) : Fragment() {

    lateinit var currentActivity: MainActivity
    protected var popupError: CustomDialogPopup? = null

    companion object {
        var NO_TITLE : Int = -1
    }

    abstract fun getTitle(): Int

    // define layout
    enum class SectionType {
        SPLASH,
        LOGIN,
        MAIN,
        HOME,
        DETAIL,
        TAB_DETAIL,
        PERMITS_TAB_DETAIL
    }

    fun showPopupError(msg: String) {
        popupError = CustomDialogPopup(currentActivity, R.style.PopupTheme)
        popupError!!.setCancelable(false)
        popupError!!.setTitle("")
        popupError!!.setMessage(msg)
        popupError!!.setButton(resources.getString(R.string.popup_button_ok),
            View.OnClickListener {
                popupError!!.dismiss()
            })
        popupError!!.show()
    }

    fun showPopupError(msg: String, clickListener: View.OnClickListener) {
        popupError = CustomDialogPopup(currentActivity, R.style.PopupTheme)
        popupError!!.setCancelable(false)
        popupError!!.setTitle("")
        popupError!!.setMessage(msg)
        popupError!!.setButton(resources.getString(R.string.popup_button_ok), clickListener)
        popupError!!.show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view: View?
        when (sectionType) {
            SectionType.MAIN -> {
                view = inflater.inflate(R.layout.main_layout, container, false)
            }
            SectionType.TAB_DETAIL -> {
                view = inflater.inflate(R.layout.working_area_tab_detail, container, false)
            }
            SectionType.PERMITS_TAB_DETAIL -> {
                view = inflater.inflate(R.layout.workpermits_list_layout, container, false)
            }
            SectionType.HOME -> {
                view = inflater.inflate(R.layout.homepage, container, false)
            }
            SectionType.DETAIL -> {
                view = inflater.inflate(R.layout.detail_layout, container, false)
            }
            SectionType.SPLASH -> {
                view = inflater.inflate(R.layout.splash, container, false)
            }
            SectionType.LOGIN -> {
                view = inflater.inflate(R.layout.login, container, false)
            }
            else -> {
                view = super.onCreateView(inflater, container, savedInstanceState)
            }
        }
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentActivity = activity as MainActivity
    }

    // Used to define fragment type {Home, Detail, ...}
    fun getSectionType(): SectionType {
        return this.sectionType
    }

    fun showProgressDialog() {
        (currentActivity as IProgressLoader).showProgressDialog()
    }

    fun hideProgressDialog() {
        (currentActivity as IProgressLoader).hideProgressDialog()
    }
}