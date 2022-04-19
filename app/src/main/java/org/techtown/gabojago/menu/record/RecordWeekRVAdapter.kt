package org.techtown.gabojago.menu.record

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.techtown.gabojago.databinding.ItemRecordWeekBinding
import java.text.SimpleDateFormat
import java.util.*

class RecordWeekRVAdapter: RecyclerView.Adapter<RecordWeekRVAdapter.ViewHolder>(){

    private val dates = ArrayList<String>()
    private val dayofweek = arrayListOf("일","월","화","수","목","금","토")

    //뷰홀더 생성->호출되는 함수->아이템 뷰 객체를 만들어서 뷰홀더에 던져줌
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRecordWeekBinding = ItemRecordWeekBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    //뷰홀더에 데이터를 바인딩해줘야 할 때마다 호출되는 함수 => 엄청나게 많이 호출
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    //뷰홀더
    inner class ViewHolder(val binding: ItemRecordWeekBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val today = week(setDate())
            if(today == position){
                binding.itemWeekToday.visibility = View.VISIBLE
            }else {
                binding.itemWeekToday.visibility = View.GONE
            }
            binding.itemDateTv.text = dates[position]
            binding.itemWeekTv.text = dayofweek[position]
            when(position){
                0->binding.itemWeekTv.setTextColor(Color.parseColor("#fc8f77"))
                6->binding.itemWeekTv.setTextColor(Color.parseColor("#7EA6F4"))
                else->binding.itemWeekTv.setTextColor(Color.parseColor("#929292"))
            }
            binding.itemWeekTv.text = dayofweek[position]


        }

    }

    override fun getItemCount(): Int {
        return 7
    }

    /** * 특정 날짜의 같은 한 주간의 날짜 범위 * @param eventDate ex) 2020-10-10 * */
    fun week(eventDate: String) : Int {
        val dateArray = eventDate.split("-").toTypedArray()
        val cal = Calendar.getInstance()
        cal [dateArray[0].toInt(), dateArray[1].toInt() - 1] = dateArray[2].toInt()
        // 일주일의 첫날을 일요일로 지정한다
        cal.firstDayOfWeek = Calendar.SUNDAY
        // 시작일과 특정날짜의 차이를 구한다
        val dayOfWeek = cal[Calendar.DAY_OF_WEEK] - cal.firstDayOfWeek+1
        val today = cal[Calendar.DAY_OF_WEEK] -1
        // 해당 주차의 첫째날을 지정한다
        cal.add(Calendar.DAY_OF_MONTH, -dayOfWeek)
        val sf = SimpleDateFormat("dd")

        for(index in 0 until 7) {
            cal.add(Calendar.DAY_OF_MONTH, 1)
            dates.add(sf.format(cal.time))
        }
        return today
    }

    fun setDate() : String{
        val now: Long = System.currentTimeMillis()
        val date = Date(now)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("ko", "KR"))
        val stringDate = dateFormat.format(date)

        return stringDate
    }

}

