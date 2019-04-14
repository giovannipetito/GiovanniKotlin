package it.giovanni.kotlin.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import it.giovanni.kotlin.HomeFragmentAdapter
import it.giovanni.kotlin.interfaces.IDataRefresh
import it.giovanni.kotlin.utils.Globals
import it.giovanni.kotlin.BuildConfig
import it.giovanni.kotlin.R
import kotlinx.android.synthetic.main.main_content_layout.*
import kotlinx.android.synthetic.main.main_layout.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainFragment : BaseFragment(SectionType.MAIN) {

    private var currentPosition: Int = 0

    override fun getTitle(): Int {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        var TAB_INDEX_HOME: Int = 1
        var TAB_INDEX_WORKING_AREA: Int = 2
        var TAB_INDEX_ADMINISTRATIVE: Int = 3
    }

    var displayWidth: Int = 0
    var defaultX: Float = 0f
    var TRANSITION_TIME: Long = 100
    var animationFinish = true
    var previousTab: Int = TAB_INDEX_HOME
    lateinit var pagesAdapter: HomeFragmentAdapter
    private val END_SCALE = 0.9f

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        hideProgressDialog() // TODO: Questo l'ho messo io qui.

        val displayMetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        displayWidth = displayMetrics.widthPixels

        return view
    }

    override fun onFragmentReady() {
        super.onFragmentReady()

        attachViewPager()
        attachRightMenu()
        attachTabListener()
        resetBackgroundTabNav()
    }

    private fun attachViewPager() {
        pagesAdapter = HomeFragmentAdapter(childFragmentManager, 3, this)

        view_pager.adapter = pagesAdapter
        view_pager.offscreenPageLimit = 3
        view_pager.currentItem = (TAB_INDEX_HOME - 1)

        view_pager.addOnPageChangeListener(
            object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                }

                override fun onPageSelected(position: Int) {
                    translateAnimation(position + 1, getTabInstanceByPosition(position + 1),
                        getDrawableTabInstanceByPosition(position + 1, false),
                        getDrawableTabInstanceByPosition(position + 1, true))

                    when (position) {
                        0 -> {
                            rooms_available.visibility = View.GONE
                            currentPosition = 0
                        }
                        1 -> {
                            rooms_available.visibility = View.VISIBLE
                            currentPosition = 1
                        }
                        2 -> {
                            rooms_available.visibility = View.GONE
                            currentPosition = 2
                        }
                    }
                }
            }
        )
    }

    private fun attachRightMenu() {
        drawer_layout.isEnabled = false
        drawer_layout.setScrimColor(Color.TRANSPARENT)

        drawer_layout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

                // Scale the View based on current slide offset
                val diffScaledOffset = slideOffset * (1 - END_SCALE)
                val offsetScale = 1 - diffScaledOffset
                container.scaleX = offsetScale
                container.scaleY = offsetScale

                // Translate the View, accounting for the scaled width
                val xOffset = drawerView.width * slideOffset
                val xOffsetDiff = container.width * diffScaledOffset / 2
                val xTranslation = xOffset - xOffsetDiff
                container.translationX = - xTranslation
                currentActivity.window.statusBarColor = ContextCompat.getColor(context!!, R.color.dark)
            }

            override fun onDrawerClosed(drawerView: View) {
                currentActivity.window.statusBarColor = ContextCompat.getColor(context!!, R.color.colorPrimary)
                tab_menu.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ico_bottom_menu))
            }
        })

        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        if (BuildConfig.CREDITS_PREFIX == "DEBUG") {
            val versionName = BuildConfig.VERSION_NAME + " " + BuildConfig.CREDITS_PREFIX
            version_name.text = versionName
        } else {
            val versionName = BuildConfig.VERSION_NAME
            version_name.text = versionName
        }

        val versionCode = "(" + BuildConfig.VERSION_CODE.toString() + ")"
        version_code.text = versionCode

        menu_intranet.setOnClickListener {
            closeRightMenu()
//            currentActivity.openDetail(Globals.FRAGMENT_WEB_VIEW, bundleIntranet)
        }

        menu_rubrica.setOnClickListener {
            closeRightMenu()
//            currentActivity.openDetail(Globals.SEARCH_RUBRICA, null)
        }

        link_feedback.setOnClickListener {
            closeRightMenu()
//            currentActivity.openDetail(Globals.FRAGMENT_WEB_VIEW, bundleFeedback)
        }
    }

    private fun attachTabListener() {
        tab_home.setOnClickListener(tabHomeClick)
        tab_working_area.setOnClickListener(tabWorkingAreaClick)
        tab_administrative.setOnClickListener(tabAdministrativeClick)
        tab_menu.setOnClickListener(tabMenuClick)
    }

    private fun resetBackgroundTabNav() {
        val start = tab_home.x + (tab_home.width / 2)
        val end = tab_menu.x + (tab_menu.width / 2)
        val delta = (end - start) / 4

        background_bottom_bar.x = -(delta * 2)
        tab_home.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ico_bottom_home_rvd))
        defaultX = (tab_home.width).toFloat()
    }

    private fun getTabInstanceByPosition(position: Int): ImageView {
        return when (position) {
            1 -> tab_home
            2 -> tab_working_area
            3 -> tab_administrative
            else -> tab_home
        }
    }

    private fun getDrawableTabInstanceByPosition(position: Int, rvdType: Boolean): Drawable {
        when (position) {
            1 -> {
                return if (!rvdType)
                    ContextCompat.getDrawable(context!!, R.drawable.ico_bottom_home)!!
                else
                    ContextCompat.getDrawable(context!!, R.drawable.ico_bottom_home_rvd)!!
            }
            2 -> {
                return if (!rvdType)
                    ContextCompat.getDrawable(context!!, R.drawable.ico_bottom_working_area)!!
                else
                    ContextCompat.getDrawable(context!!, R.drawable.ico_bottom_working_area_rvd)!!
            }
            3 -> {
                return if (!rvdType)
                    ContextCompat.getDrawable(context!!, R.drawable.ico_bottom_administrative_area)!!
                else
                    ContextCompat.getDrawable(context!!, R.drawable.ico_bottom_administrative_area_rvd)!!
            }
            else -> {
                return if (!rvdType)
                    ContextCompat.getDrawable(context!!, R.drawable.ico_bottom_home)!!
                else
                    ContextCompat.getDrawable(context!!, R.drawable.ico_bottom_home_rvd)!!
            }
        }
    }

    private var tabHomeClick: View.OnClickListener = View.OnClickListener {
        translateAnimation(
            TAB_INDEX_HOME,
            tab_home,
            ContextCompat.getDrawable(context!!, R.drawable.ico_bottom_home)!!,
            ContextCompat.getDrawable(context!!, R.drawable.ico_bottom_home_rvd)!!
        )
        view_pager.currentItem = TAB_INDEX_HOME - 1
    }

    private var tabWorkingAreaClick: View.OnClickListener = View.OnClickListener {
        translateAnimation(
            TAB_INDEX_WORKING_AREA,
            tab_working_area,
            ContextCompat.getDrawable(context!!, R.drawable.ico_bottom_working_area)!!,
            ContextCompat.getDrawable(context!!, R.drawable.ico_bottom_working_area_rvd)!!
        )
        view_pager.currentItem = TAB_INDEX_WORKING_AREA - 1
    }

    private var tabAdministrativeClick: View.OnClickListener = View.OnClickListener {
        translateAnimation(
            TAB_INDEX_ADMINISTRATIVE,
            tab_administrative,
            ContextCompat.getDrawable(context!!, R.drawable.ico_bottom_administrative_area)!!,
            ContextCompat.getDrawable(context!!, R.drawable.ico_bottom_administrative_area_rvd)!!
        )
        view_pager.currentItem = TAB_INDEX_ADMINISTRATIVE - 1
    }

    private var tabMenuClick: View.OnClickListener = View.OnClickListener {
        openRightMenu()
    }

    private fun closeRightMenu() {
        if (drawer_layout.isDrawerOpen(GravityCompat.END)) {
            drawer_layout.closeDrawer(GravityCompat.END)
            tab_menu.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ico_bottom_menu))
        }
    }

    private fun openRightMenu() {
        drawer_layout.isEnabled = true
        if (drawer_layout.isDrawerOpen(GravityCompat.END)) {
            drawer_layout.closeDrawer(GravityCompat.END)
            tab_menu.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ico_bottom_menu))
        } else {
            drawer_layout.openDrawer(GravityCompat.END)
            tab_menu.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ico_close_menu))
        }
    }

    private fun translateAnimation(tab: Int, imageView: ImageView, drawable_from: Drawable, drawable_to: Drawable) {
        if (animationFinish) {
            resetAllImages()
            val offset: Float = background_bottom_bar.x + ((tab - previousTab) * defaultX)
            previousTab = tab
            val valueAnimator: ValueAnimator = ObjectAnimator.ofFloat(background_bottom_bar, "translationX", offset).apply {
                duration = TRANSITION_TIME
            }

            valueAnimator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    // done
                    animationFinish = true
                    // check in right menu is open
                    closeRightMenu()
                }
            })

            animationFinish = false
            valueAnimator.start()
            tabSelectedDrawableAnimation(imageView, drawable_from, drawable_to)
        }
    }

    private fun tabSelectedDrawableAnimation(imageView: ImageView, drawable_from: Drawable, drawable_to: Drawable) {

        // set res image array
        val transitionDrawable = TransitionDrawable(arrayOf(drawable_from, drawable_to))

        // your image view here - backgroundImageView
        imageView.setImageDrawable(transitionDrawable)
        transitionDrawable.startTransition(TRANSITION_TIME.toInt())
        transitionDrawable.isCrossFadeEnabled = false // call public methods
    }

    private fun resetAllImages() {
        tab_home.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ico_bottom_home))
        tab_working_area.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ico_bottom_working_area))
        tab_administrative.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ico_bottom_administrative_area))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Globals.REQUEST_CODE_EVENT_WORKING_AREA) {
            if (data != null) {
                (pagesAdapter.getItem(1) as IDataRefresh).refresh()
            }
        } else if (requestCode == Globals.REQUEST_CODE_EVENT_HOME) {
            if (data != null) {
                (pagesAdapter.getItem(1) as IDataRefresh).refresh()
            }
        }
    }
}