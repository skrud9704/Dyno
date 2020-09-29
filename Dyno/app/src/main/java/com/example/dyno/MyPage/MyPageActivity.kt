package com.example.dyno.MyPage

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.dyno.R
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.example.dyno.MyPage.adapter.MedicineAdapter
import com.example.dyno.MyPage.adapter.DurAdapter
import com.example.dyno.MyPage.adapter.SupplementAdapter
import java.util.*


class MyPageActivity : AppCompatActivity() {
    var SHARED_PREF = "SaveLogin"
    var SHARED_PREF_NAME = "name"

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        // 툴바
        val toolbar = findViewById<Toolbar>(R.id.htab_toolbar)
        setSupportActionBar(toolbar)

        // 사용자 명 가져와 툴바에 set
        val pref : SharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val prefName = pref.getString(SHARED_PREF_NAME,"null")
        if (supportActionBar != null)
            supportActionBar!!.title = "$prefName 님"

        // 뒤로가기 버튼 setting true
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        // 뷰 페이저 setting - 프래그먼트 및 각 리싸이클러어댑터 설정.
        val viewPager = findViewById<ViewPager>(R.id.htab_viewpager)
        setupViewPager(viewPager)

        // 탭 레이아웃 setting - 위의 뷰 페이저를 붙임.
        val tabLayout = findViewById<TabLayout>(R.id.htab_tabs)
        tabLayout.setupWithViewPager(viewPager)


        // 접어지는 툴바
        val collapsingToolbarLayout =
            findViewById<View>(R.id.htab_collapse_toolbar) as CollapsingToolbarLayout

        /*try {
            val bitmap = BitmapFactory.decodeResource(resources,
                R.drawable.dyno_logo
            )
            Palette.from(bitmap).generate { palette ->
                //val vibrantColor: Int = palette!!.getVibrantColor(R.color.primary_500)
                val vibrantColor: Int = palette!!.getVibrantColor(R.color.dynoMainWhite)
                //val vibrantDarkColor: Int = palette.getDarkVibrantColor(R.color.primary_700)
                val vibrantDarkColor: Int = palette.getDarkVibrantColor(R.color.dynoMainBeige)
                collapsingToolbarLayout.setContentScrimColor(vibrantColor)
                collapsingToolbarLayout.setStatusBarScrimColor(vibrantDarkColor)
            }
        } catch (e: Exception) {
            // if Bitmap fetch fails, fallback to primary colors
            Log.e(TAG, "onCreate: failed to create bitmap from background", e.fillInStackTrace())
            collapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(this,
                R.color.dynoMainWhite
            ))
            collapsingToolbarLayout.setStatusBarScrimColor(ContextCompat.getColor(this,
                R.color.dynoMainBeige
            ))
        }*/

        // 탭 변경 될 때 리스너
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
                Log.d(TAG, "onTabSelected: pos: " + tab.position)
                /*when (tab.position) {
                    0 -> showToast("의약품")
                    1 -> showToast("건강기능식품")
                    2 -> showToast("병용판단")
                }*/
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(
            supportFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        adapter.addFrag(
            MyFragment(
                ContextCompat.getColor(this, R.color.dynoMainBeige), 0
            ), "의약품"
        )
        adapter.addFrag(
            MyFragment(
                ContextCompat.getColor(this, R.color.dynoMainBeige),
                1
            ), "건강기능식품"
        )
        adapter.addFrag(
            MyFragment(
                ContextCompat.getColor(this, R.color.dynoMainBeige),
                2
            ), "병용판단"
        )
        viewPager.adapter = adapter
    }

    // 상단 점 3개 메뉴
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.action_settings -> return true
        }
        return super.onOptionsItemSelected(item)
    }

    private class ViewPagerAdapter(
        fm: FragmentManager?,
        behavior: Int
    ) :
        FragmentPagerAdapter(fm!!, behavior) {
        private val mFragmentList: MutableList<Fragment> =
            ArrayList()
        private val mFragmentTitleList: MutableList<String> =
            ArrayList()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFrag(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
        }
    }

    // 의약품과 건강기능식품, 병용판단을 위한 프래그먼트
    class MyFragment : Fragment {
        var color = 0
        var data_type_num = -1

        constructor() {}

        @SuppressLint("ValidFragment")
        constructor(color: Int, data_type: Int) {
            this.color = color
            this.data_type_num = data_type
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view: View =
                inflater.inflate(R.layout.fragment_my_page, container, false)
            val frameLayout = view.findViewById<FrameLayout>(R.id.dummyfrag_bg)
            frameLayout.setBackgroundColor(color)
            val recyclerView: RecyclerView = view.findViewById(R.id.dummyfrag_scrollableview)
            val linearLayoutManager =
                LinearLayoutManager(activity!!.baseContext)
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.setHasFixedSize(true)
            //val list = listOf(VersionModel.data.toString())

            // data_type_num = 0 : 의약품 탭의 리싸이클러 뷰를 위한 어댑터
            // data_type_num = 1 : 건강기능식품 탭의 리싸이클러 뷰를 위한 어댑터
            // data_type_num = 2 : 병용판단 탭의 리싸이클러 뷰를 위한 어댑터
            when(data_type_num){
                0 ->{
                    val adapter = MedicineAdapter(this.context!!)
                    recyclerView.adapter = adapter
                }
                1->{
                    val adapter = SupplementAdapter(this.context!!)
                    recyclerView.adapter = adapter
                }
                2->{
                    val adapter = DurAdapter(this.context!!)
                    recyclerView.adapter = adapter
                }
            }

            return view
        }
    }

    companion object {
        private val TAG = MyPageActivity::class.java.simpleName
    }
}