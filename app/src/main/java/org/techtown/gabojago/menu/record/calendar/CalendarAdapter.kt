package org.techtown.gabojago.menu.record.calendar

import android.graphics.Point
import android.util.Log
import android.util.TypedValue.COMPLEX_UNIT_PX
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.techtown.gabojago.databinding.ItemCalendarGridviewBinding
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

//달력 날짜 그리드뷰-어댑터
class CalendarAdapter(private val viewDate: String,private val randomresultdateList :ArrayList<Int>) : RecyclerView.Adapter <CalendarAdapter.ViewHolder>() {

    //클릭리스너 인터페이스
    interface MyItemClickListener {
        fun onItemClick(date:String)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    private val days = ArrayList<String>()
    private var specialDate = ""
    var daynum = 0
    var month = ""
    var year = ""
    var todayYear = 0
    var todayMonth = 0
    var todayDate = 0


    //뷰홀더 생성->호출되는 함수->아이템 뷰 객체를 만들어서 뷰홀더에 던져줌
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCalendarGridviewBinding =
            ItemCalendarGridviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        //실제 화면 size 받아오기
        val display = parent.display
        val size = Point()
        display.getSize(size)
        val width = size.x
        val height = size.y

        //받아온 화면 size에 따라 조정
        binding.itemLayout.layoutParams.height = height/15
        binding.itemEmpty.layoutParams.height = height/160
        binding.itemGridviewTodayIv.layoutParams.height = height/140
        binding.itemGridviewTodayIv.layoutParams.width = height/140

        //달력 날짜 초기 설정
        setEmptyDate(viewDate)
        return ViewHolder(binding)
    }


    //뷰홀더에 데이터를 바인딩해줘야 할 때마다 호출되는 함수 => 엄청나게 많이 호출
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)

        holder.binding.itemGridviewTv.setOnClickListener{
            //달력 날짜 형식 두자리수로 변경
            val df = DecimalFormat("00")
            var date = df.format(days[position].toInt())
            mItemClickListener.onItemClick(specialDate+date)

        }
    }

    //뷰홀더
    inner class ViewHolder(val binding: ItemCalendarGridviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.itemGridviewTv.text = days[position]

            if (days[position] != "") {
                today()
                //오늘날짜에 점 표시
                if (todayYear == year.toInt() && todayMonth == month.toInt() && todayDate == days[position].toInt()) {
                    binding.itemGridviewTodayIv.visibility = View.VISIBLE
                } else {
                    binding.itemGridviewTodayIv.visibility = View.GONE
                }

                //모험한 날짜에 색표시
                if ((position - daynum + 2) == randomresultdateList[position - daynum+2]) {
                    binding.itemGridviewRecordIv.visibility = View.VISIBLE
                }
            }
        }
    }


    override fun getItemCount(): Int = setEmptyDate(viewDate)

    //받아온 날짜에 따른 달력 세팅 함수
    private fun setEmptyDate(eventDate: String): Int {
        val cal = Calendar.getInstance()
        cal.set(eventDate.substring(0,4).toInt(), eventDate.substring(4,6).toInt() - 1, 1)
        specialDate = eventDate.substring(0,6)
        val dayNum: Int = cal.get(Calendar.DAY_OF_WEEK)
        //1일 - 요일 매칭 시키기 위해 공백 add
        for (i in 1 until dayNum) {
            days.add("")
        }
        val daySize = setCalendarDate(cal.get(Calendar.MONTH) + 1)
        month = (cal.get(Calendar.MONTH) + 1).toString()
        year = cal.get(Calendar.YEAR).toString()
        daynum = dayNum
        return (dayNum + daySize)
    }

    private fun setCalendarDate(month: Int): Int {
        val cal = Calendar.getInstance()
        cal.set(Calendar.MONTH, month - 1)
        for (i in 0 until cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            days.add("" + (i + 1))
        }
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    //오늘 날짜 세팅
    private fun today() {
        val now: Long = System.currentTimeMillis()
        val date = Date(now)
        val yearFormat = SimpleDateFormat("yyyy", Locale("ko", "KR"))
        val monthFormat = SimpleDateFormat("MM", Locale("ko", "KR"))
        val dateFormat = SimpleDateFormat("dd", Locale("ko", "KR"))
        todayYear = yearFormat.format(date).toInt()
        todayMonth = monthFormat.format(date).toInt()
        todayDate = dateFormat.format(date).toInt()
    }
}



