package com.kent.appbastos.model.pageradapter

import android.content.Context
import androidx.viewpager.widget.PagerAdapter
import android.widget.LinearLayout
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.kent.appbastos.R

class ViewPagerAdapter(var context: Context) : PagerAdapter() {

    var txtOnboard = intArrayOf(
        R.string.txtOnboarding_1,
        R.string.txtOnboarding_2,
    )

    override fun getCount(): Int {
        return txtOnboard.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater.inflate(R.layout.slider_layout, container, false)

        val slideDesciption = view.findViewById<View>(R.id.txtOnboard) as TextView
        slideDesciption.setText(txtOnboard[position])
        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}