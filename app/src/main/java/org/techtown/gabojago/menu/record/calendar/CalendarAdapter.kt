package org.techtown.gabojago.menu.record.calendar

import android.content.ContentValues.TAG
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.NonDisposableHandle.parent
import org.techtown.gabojago.databinding.ItemCalendarGridviewBinding
import org.techtown.gabojago.main.getJwt
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ofPattern
import java.util.*

class CalendarAdapter(private val viewDate: String,private val randomresultdateList :ArrayList<Int>) : RecyclerView.Adapter <CalendarAdapter.ViewHolder>() {

    private val days = ArrayList<String>()
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
        setEmptyDate(viewDate)
        return ViewHolder(binding)
    }


    //뷰홀더에 데이터를 바인딩해줘야 할 때마다 호출되는 함수 => 엄청나게 많이 호출
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)

    }

    //뷰홀더
    inner class ViewHolder(val binding: ItemCalendarGridviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.itemGridviewTv.text = days[position]

            if (days[position] != "") {
                today()
                if (todayYear == year.toInt() && todayMonth == month.toInt() && todayDate == days[position].toInt()) {
                    binding.itemGridviewTodayIv.visibility = View.VISIBLE
                } else {
                    binding.itemGridviewTodayIv.visibility = View.GONE
                }
                Log.e("randomresult2", (randomresultdateList.size).toString())



                if ((position - daynum + 2) == randomresultdateList[position - daynum+2]) {
                    binding.itemGridviewRecordIv.visibility = View.VISIBLE
                    Log.e("randomresult2", (position - daynum + 1).toString())
                    Log.e("randomresult2",
                        (randomresultdateList[position - daynum + 1]).toString())
                }
            }
        }
    }


    override fun getItemCount(): Int = setEmptyDate(viewDate)

    fun addAll(randomresultdateList:ArrayList<Int>){

    }


    private fun setEmptyDate(eventDate: String): Int {
        val dateArray = eventDate.split("-").toTypedArray()
        val cal = Calendar.getInstance()
        cal.set(dateArray[0].toInt(), dateArray[1].toInt() - 1, 1)
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



