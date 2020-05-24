package com.zj.timeaxis

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    lateinit var adpter: NewsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val timeLayout = findViewById<RelativeLayout>(R.id.time_layout)
        val tvDay = findViewById<TextView>(R.id.tv_day)
        val tvMonth = findViewById<TextView>(R.id.tv_month)

        //设置下拉刷新
        refreshLayout.setOnRefreshListener(object : OnRefreshListener {
            override fun onRefresh(refreshlayout: RefreshLayout) {
                val initData = initData()
                adpter.setNewData(initData)
                refreshlayout.finishRefresh(2000 /*,false*/) //传入false表示刷新失败
            }
        })

        //上拉加载更多
        refreshLayout.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore(refreshlayout: RefreshLayout) {
                val initData = initData()
                initData?.let { adpter.addData(it) }
                refreshlayout.finishLoadMore(2000 /*,false*/) //传入false表示加载失败
            }
        })

        val linearLayoutManager = LinearLayoutManager(this)
        rl_new.layoutManager = linearLayoutManager
        adpter = NewsListAdapter(R.layout.item_layout_new)
        rl_new.adapter = adpter
        rl_new.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (linearLayoutManager.itemCount > 0) {
                    timeLayout.visibility = View.VISIBLE
                    val data = adpter.data
                    //获取第一个可见条目的position
                    val firstVisPos = linearLayoutManager.findFirstVisibleItemPosition()
                    //获取第一个可见条目的position的数据
                    val itemData = data.get(firstVisPos)

                    //格式化时间
                    val ca: Calendar = Calendar.getInstance()
                    ca.timeInMillis = itemData!!.time
                    val dayInt = ca[Calendar.DATE]
                    val day = if (dayInt < 10) "0$dayInt" else dayInt.toString()
                    val monthInt = ca[Calendar.MONTH] + 1
                    val month = monthInt.toString()

                    //时间控件赋值
                    tvDay.text = day
                    tvMonth.text = month
                } else {
                    timeLayout.visibility = View.GONE
                }
            }
        })

        rl_new.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var linearLayoutManager = rl_new.getLayoutManager() as LinearLayoutManager
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //获取第一个可见条目的position
                val firstVisPos = linearLayoutManager.findFirstVisibleItemPosition()
                //下滑情况下   下滑为负值  上滑为正值
                val firstVisiableChildView: View? =
                    linearLayoutManager.findViewByPosition(firstVisPos) //gridLayoutManager布局管理器

                val timeLayout: RelativeLayout? =
                    adpter.getViewByPosition(firstVisPos, R.id.time_layout) as RelativeLayout

                //获取item条目的高度
                val itemHeight = firstVisiableChildView?.getHeight();

                //第一个条目标签滑动瞬间隐藏
                if (firstVisiableChildView!!.getTop() < 0) {
                    //如果显示的时候隐藏
                    if (timeLayout?.visibility == View.VISIBLE) {
                        //设置成INVISIBLE 为了区分没有标签和暂时隐藏
                        timeLayout.visibility = View.INVISIBLE
                    }
                }

                //当条目数量大于2个考虑下个显示情况
                if (linearLayoutManager.itemCount > 1) {
                    //获取第一个可见item的下一个
                    val nextVisPos = firstVisPos + 1
                    val timeLayoutNext: RelativeLayout? =
                        adpter.getViewByPosition(nextVisPos, R.id.time_layout) as RelativeLayout

                    //除第一个之外的包含标签的隐藏   Activity 浮动的标签距离顶部10dp
                    if (itemHeight!! + firstVisiableChildView.getTop() <= ScreenUtil.dip2px(
                            this@MainActivity,
                            10f
                        )
                    ) {
                        //滑倒标签位置隐藏
                        if (timeLayoutNext?.visibility == View.VISIBLE) {
                            timeLayoutNext.visibility = View.INVISIBLE
                        }
                    } else {
                        //滑出标签位置显示
                        if (timeLayoutNext?.visibility == View.INVISIBLE) {
                            timeLayoutNext.visibility = View.VISIBLE
                        }
                    }
                }
            }
        })

        val initData = initData()
        adpter.setNewData(initData)
    }

    /**
     * 模拟不同天的数据
     */
    fun initData(): MutableList<NewsBean?>? {
        val newList: MutableList<NewsBean?> = ArrayList();
        for (index in 1..100) {
            val new: NewsBean;
            when (index / 10) {
                0 -> {
                    new = NewsBean(System.currentTimeMillis(), "新闻标题" + index, "CCTV")
                }
                1 -> {
                    new = NewsBean(System.currentTimeMillis() - 100000000, "新闻标题" + index, "湖南卫视")
                }
                2 -> {
                    new = NewsBean(System.currentTimeMillis() - 100000000 * 2, "新闻标题" + index, "江苏卫视")
                }
                3 -> {
                    new = NewsBean(System.currentTimeMillis() - 100000000 * 3, "新闻标题" + index, "浙江卫视")
                }
                4 -> {
                    new = NewsBean(System.currentTimeMillis() - 100000000 * 4, "新闻标题" + index, "江苏卫视")
                }
                5 -> {
                    new = NewsBean(System.currentTimeMillis() - 100000000 * 5, "新闻标题" + index, "江苏卫视")
                }
                6 -> {
                    new = NewsBean(System.currentTimeMillis() - 100000000 * 6, "新闻标题" + index, "江苏卫视")
                }
                7 -> {
                    new = NewsBean(System.currentTimeMillis() - 100000000 * 7, "新闻标题" + index, "江苏卫视")
                }
                8 -> {
                    new = NewsBean(System.currentTimeMillis() - 100000000 * 8, "新闻标题" + index, "江苏卫视")
                }
                9 -> {
                    new = NewsBean(System.currentTimeMillis() - 100000000 * 9, "新闻标题" + index, "江苏卫视")
                }
                else -> new =
                    NewsBean(System.currentTimeMillis() - 100000000 * 10, "新闻标题" + index, "CCTV")
            }

            newList.add(new)
        }
        return newList
    }
}

