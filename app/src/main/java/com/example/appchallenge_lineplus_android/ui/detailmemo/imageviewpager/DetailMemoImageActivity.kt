package com.example.appchallenge_lineplus_android.ui.detailmemo.imageviewpager

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.*
import com.example.appchallenge_lineplus_android.R
import kotlinx.android.synthetic.main.activity_detail_image.*


class DetailMemoImageActivity : AppCompatActivity(), View.OnClickListener {
    var listImage: ArrayList<String> = ArrayList()
    var currentIndex: Int = 0

    private fun getImagePathData(){
        if (intent.hasExtra("detailImagePath")){
            currentIndex = intent.getIntExtra("currentPostion", 0)
            var detailImagePath = intent.getStringExtra("detailImagePath")

            val testData = stringToWords(detailImagePath)
            for (i in 0 until testData.size) listImage.add(testData[i])
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_image)
        getImagePathData()

        val fragmentAdapter =
            FragmentAdapter(
                supportFragmentManager
            )
        pager.adapter = fragmentAdapter
        pager.clipToPadding = false

        for (i in 0 until listImage.size) {
            val detailImageFragment =
                DetailImageFragment()
            val bundle = Bundle()
            bundle.putString("imgRes", listImage[i].toString())
            bundle.putInt("currentImageIndex", i+1)
            bundle.putInt("ImageSize", listImage.size)
            detailImageFragment.arguments = bundle
            fragmentAdapter.addItem(detailImageFragment)
        }
        pager.setCurrentItem(currentIndex, true)
        fragmentAdapter.notifyDataSetChanged()
    }

    internal class FragmentAdapter
        (fm: FragmentManager?) : FragmentPagerAdapter(fm!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val fragments: ArrayList<Fragment> = ArrayList()

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        fun addItem(fragment: Fragment) = fragments.add(fragment)
    }

    override fun onClick(p0: View?) {

    }

    private fun stringToWords(s: String) = s.trim().splitToSequence(',', ' ', '[', ']')
        .filter { it.isNotEmpty() } // or: .filter { it.isNotBlank() }
        .toMutableList()
}