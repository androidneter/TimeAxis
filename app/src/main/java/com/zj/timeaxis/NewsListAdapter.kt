package com.zj.timeaxis

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import java.text.SimpleDateFormat
import java.util.*

/**
 * 新闻适配器
 *
 * @author zj
 */
class NewsListAdapter(layoutResId: Int) :
    BaseQuickAdapter<NewsBean?, BaseViewHolder>(layoutResId) {
    @SuppressLint("SimpleDateFormat")
    var sdfDay = SimpleDateFormat("yyyy-MM-dd")
    @SuppressLint("SimpleDateFormat")
    var sdfHour = SimpleDateFormat("HH:mm:ss")


    override fun convert(helper: BaseViewHolder, item: NewsBean?) {
        item?.let {
            val position = helper.adapterPosition
            //前个条目时间
            val datePre: Long = data[Math.max(position - 1, 0)]!!.time
            //当前条目
            val date: Long = item.time

            val ca: Calendar = Calendar.getInstance()
            ca.timeInMillis = date

            //当前条目时间
            val caPre: Calendar = Calendar.getInstance()
            caPre.timeInMillis = datePre

            //item绑定数据
            helper.setText(R.id.tv_news_title, item.title)
                .setText(R.id.tv_title_time, sdfHour.format(ca.time))
                .setText(R.id.tv_source, item.source)

            val viewTop: View = helper.getView(R.id.view_top)
            val monthRl: RelativeLayout = helper.getView(R.id.time_layout)
            val tvDay: TextView = helper.getView(R.id.tv_day)
            val tvMonth: TextView = helper.getView(R.id.tv_month)
            val tvTAG: TextView = helper.getView(R.id.tv_tag)
            val titleTime: TextView = helper.getView(R.id.tv_title_time)
            if (0 == position) {
                //设置首个条目上间距
                helper.itemView.setPadding(0, ScreenUtil.dip2px(context, 10f), 0, 0)
                val color = ContextCompat.getColor(context, android.R.color.holo_red_light)
                tvTAG.setTextColor(color)
                tvTAG.text = "推荐"
                tvTAG.visibility = View.VISIBLE
                titleTime.setBackgroundResource(R.drawable.shape_time_bg)
            } else if (1 == position) {
                helper.itemView.setPadding(0, 0, 0, 0)
                val color = ContextCompat.getColor(context, android.R.color.holo_blue_bright)
                tvTAG.setTextColor(color)
                tvTAG.text = "热门"
                tvTAG.visibility = View.VISIBLE
                titleTime.setBackgroundResource(R.drawable.shape_time_bg_grey)
            } else { //防止复用设置间距
                helper.itemView.setPadding(0, 0, 0, 0)
                tvTAG.visibility = View.GONE
                titleTime.setBackgroundResource(R.drawable.shape_time_bg_grey)
            }

            //时间框绑定
            val dayInt = ca[Calendar.DATE]
            val day = if (dayInt < 10) "0$dayInt" else dayInt.toString()
            val monthInt = ca[Calendar.MONTH] + 1
            val month = monthInt.toString()

            if (0 == position) {
                //红点
                helper.setImageResource(R.id.dot_img, R.drawable.ic_hongdian)
                //上画线
                viewTop.visibility = View.INVISIBLE
                monthRl.visibility = View.VISIBLE
                tvDay.text = day
                tvMonth.text = month
            } else {
                viewTop.visibility = View.VISIBLE
                //判断前后时间是否一致
                val condition = TextUtils.equals(sdfDay.format(ca.time), sdfDay.format(caPre.time))
                if (condition) {
                    helper.setImageResource(R.id.dot_img, R.drawable.ic_huidian)
                    //这里为了区分是不是没有还是临时隐藏
                    monthRl.visibility = View.GONE
                } else {
                    helper.setImageResource(R.id.dot_img, R.drawable.ic_hongdian)
                    titleTime.setBackgroundResource(R.drawable.shape_time_bg)
                    monthRl.visibility = View.VISIBLE
                    tvDay.text = day
                    tvMonth.text = month
                }
            }
        }

    }
}
