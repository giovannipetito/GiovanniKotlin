package it.giovanni.arkivio.fragments.detail.layoutmanager

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import it.giovanni.arkivio.App
import it.giovanni.arkivio.R
import it.giovanni.arkivio.customview.TimelineView
import it.giovanni.arkivio.databinding.LayoutManagerLayoutBinding
import it.giovanni.arkivio.fragments.DetailFragment
import it.giovanni.arkivio.model.DarkModeModel
import it.giovanni.arkivio.presenter.DarkModePresenter
import it.giovanni.arkivio.utils.Utils.Companion.checkEmail
import kotlinx.android.synthetic.main.layout_manager_layout.*

class LayoutManagerFragment: DetailFragment(), TimelineView.TimelineViewListener {

    private var viewFragment: View? = null
    private var viewParent: View? = null
    private var progressBarContainer: RelativeLayout? = null

    private lateinit var list: List<String>

    override fun getLayout(): Int {
        return NO_LAYOUT
    }

    override fun getTitle(): Int {
        return R.string.layout_manager_title
    }

    override fun getActionTitle(): Int {
        return NO_TITLE
    }

    override fun searchAction(): Boolean {
        return false
    }

    override fun backAction(): Boolean {
        return false
    }

    override fun closeAction(): Boolean {
        return true
    }

    override fun deleteAction(): Boolean {
        return false
    }

    override fun editAction(): Boolean {
        return false
    }

    override fun editIconClick() {
    }

    override fun onActionSearch(search_string: String) {
    }

    override fun onCreateBindingView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: LayoutManagerLayoutBinding? = DataBindingUtil.inflate(inflater, R.layout.layout_manager_layout, container, false)
        viewFragment = binding?.root

        val darkModePresenter = DarkModePresenter(this, requireContext())
        val model = DarkModeModel(requireContext())
        binding?.temp = model
        binding?.presenter = darkModePresenter

        return viewFragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewGroup = viewFragment?.findViewById(R.id.progress_bar_viewgroup) as ViewGroup
        viewParent = layoutInflater.inflate(R.layout.layout_manager_layout, viewGroup, false)

        // val inflater = LayoutInflater.from(context)
        // val viewParent = inflater.inflate(R.layout.layout_manager_layout, null)
        // NOTA: viewParent e viewFragment sono equivalenti.

        val progressBarContent = viewFragment?.findViewById(R.id.progress_bar_content) as View
        progressBarContainer = progressBarContent.findViewById(R.id.progress_bar_container)
        val progressBar = progressBarContent.findViewById(R.id.progress_bar) as RelativeLayout
        val bar = progressBarContent.findViewById(R.id.bar) as ImageView

        val drawableBar = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(ContextCompat.getColor(requireContext(), R.color.rosso),
                ContextCompat.getColor(requireContext(), R.color.giallo),
                ContextCompat.getColor(requireContext(), R.color.blu))
        )

        drawableBar.cornerRadius = 50f
        progressBar.setBackgroundDrawable(drawableBar)

        /*
        var progressbarwidth: Int? = null
        viewParent?.viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                if (progressBarContainer != null)
                    progressbarwidth = progressBarContainer?.width!! * 1000 / 5
            }
        })
        viewParent?.requestLayout()
        */

        multi_spinner_view.setValues(104F, 140F, 104F, 20F)
        number_picker_1.minValue = 104
        number_picker_1.maxValue = 167
        number_picker_1.value = 104
        // number_picker_1.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        number_picker_1.setOnValueChangedListener { _, _, newVal ->

            multi_spinner_view.setValues(newVal.toFloat(), 140F, 104F, 20F)
        }

        number_picker_2.minValue = 0
        number_picker_2.maxValue = 350
        number_picker_2.setOnValueChangedListener { _, _, newVal ->
            bar.translationX = newVal.toFloat()
        }

        // icon_1.background = resources.getDrawable(R.drawable.giovanni)
        icon_1.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.giovanni))

        Glide.with(requireContext())
            .load(R.drawable.giovanni)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(28)))
            .into(icon_2)

        Glide.with(requireContext())
            .load(R.drawable.giovanni)
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(54)))
            .into(icon_3)

        Glide.with(requireContext())
            .load(R.drawable.giovanni)
            .apply(RequestOptions()
                .circleCrop()
                .placeholder(R.mipmap.ic_launcher))
            .into(icon_4)

        Glide.with(requireContext())                     // Passing context.
            .load(R.drawable.giovanni)                   // Passing your url to load image.
            .placeholder(R.mipmap.ic_launcher)           // The default image. It would be loaded at initial time and it will replace with your loaded image once Glide successfully load image using url.
            .error(R.mipmap.ic_launcher)                 // In case of any Glide exception or not able to download then this image will be appear.
            .diskCacheStrategy(DiskCacheStrategy.ALL)    // Using to load into cache, then second time it will load fast.
            .apply(RequestOptions.circleCropTransform()) // This CircleTransform class help to crop an image as circle.
            // .animate(R.anim.fade_in)                  // When image (url) will be loaded by Glide, then this face in animation help to replace url image in the place of placeHolder image.
            .into(icon_5)                                // Passing imageView reference to appear the image.

        val timelineView: TimelineView = view.findViewById(R.id.bar_tagli)
        list = listOf("10€", "20€", "30€", "40€", "50€")

        timelineView.setValues(list)
        timelineView.setTimelineViewListener(this)

        selectedValue = "20€"
        timelineView.setValue(selectedValue, TimelineView.DISCRETE)

        val buttonMinus: ImageView = view.findViewById(R.id.button_minus)
        val buttonPlus: ImageView = view.findViewById(R.id.button_plus)

        buttonMinus.setOnClickListener {
            if (list.isNotEmpty()) {
                selectedValue = getPreviousValue(list)
                timelineView.setValue(selectedValue, TimelineView.DISCRETE)
            }
        }

        buttonPlus.setOnClickListener {
            if (list.isNotEmpty()) {
                selectedValue = getNextValue(list)
                timelineView.setValue(selectedValue, TimelineView.DISCRETE)
            }
        }

        edit_email.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (checkEmail(edit_email.text.toString()))
                    Toast.makeText(context, "Email valida", Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(context, "Email errata", Toast.LENGTH_LONG).show()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    override fun onFinishDragging(var1: Int, var2: String) {
        selectedValue = var2
    }

    private var selectedValue : String = ""
        set(value) {
            field = value
            processSelectedValue()
        }

    private fun processSelectedValue() {
        if (selectedValue.isEmpty())
            return

        /*
        var index = -1
        for (i in list.indices) {
            if (list[i].equals(selectedValue, ignoreCase = true)) {
                index = i
            }
        }
        if (index > -1) {
            topUpViewModel.pickedTopUpValue.postValue(topUpViewModel.creditCardPrices.value?.get(index))
        }
        */
    }

    private fun getPreviousValue(list: List<String>): String {
        for (i in list.indices) {
            if (list[i].equals(selectedValue, ignoreCase = true)) {
                return if (i == 0)
                    list[0]
                else
                    list[i - 1]
            }
        }
        return list[0]
    }

    private fun getNextValue(list: List<String>): String {
        for (i in list.indices) {
            if (list[i].equals(selectedValue, ignoreCase = true)) {
                return if (i == list.size - 1)
                    list[list.size - 1]
                else
                    list[i + 1]
            }
        }
        return list[0]
    }

    private fun setStatusBarColor() {
        // currentActivity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        currentActivity.window.statusBarColor = ContextCompat.getColor(
            App.context,
            android.R.color.transparent
        )
        // currentActivity.window.navigationBarColor = currentActivity.resources.getColor(android.R.color.transparent)
        currentActivity.window.setBackgroundDrawable(
            ContextCompat.getDrawable(
                App.context,
                R.drawable.background_dark_mode
            )
        )
    }
}