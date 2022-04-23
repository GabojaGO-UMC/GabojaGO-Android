package org.techtown.gabojago.menu.record

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.techtown.gabojago.databinding.ItemRecordWeekBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Handler

class RecordWeekRVAdapter(private val viewDate: String): RecyclerView.Adapter<RecordWeekRVAdapter.ViewHolder>(){

    private val dates = ArrayList<String>()
    private val dayofweek = arrayListOf("일","월","화","수","목","금","토")
    private val alldays = ArrayList<String>()
    private var oldPosition = -1
    private var selectedPosition = week(viewDate)

    interface MyItemClickListener {
        fun onItemClick(day:String)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }


    //뷰홀더 생성->호출되는 함수->아이템 뷰 객체를 만들어서 뷰홀더에 던져줌
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRecordWeekBinding = ItemRecordWeekBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    //뷰홀더에 데이터를 바인딩해줘야 할 때마다 호출되는 함수 => 엄청나게 많이 호출
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.bind(position)
        if (selectedPosition == position)
            holder.binding.itemWeekToday.visibility = View.VISIBLE
        else
            holder.binding.itemWeekToday.visibility = View.GONE

        holder.itemView.setOnClickListener{
            Log.e("calendar","error나는 부분")
            mItemClickListener?.onItemClick(alldays[position])
            oldPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(oldPosition)
            notifyItemChanged(selectedPosition)
        }
    }

    //뷰홀더
    inner class ViewHolder(val binding: ItemRecordWeekBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
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

    /** * 특정 날짜의 같은 한 주간의 날짜 범위 * @param eventDate ex) 20201010 * */
    fun week(eventDate: String) : Int {
        val year = eventDate.substring(0,4)
        val month = eventDate.substring(4,6)
        val day = eventDate.substring(6)
        val cal = Calendar.getInstance()
        cal [year.toInt(), month.toInt() - 1] = day.toInt()
        // 일주일의 첫날을 일요일로 지정한다
        cal.firstDayOfWeek = Calendar.SUNDAY
        // 시작일과 특정날짜의 차이를 구한다
        val dayOfWeek = cal[Calendar.DAY_OF_WEEK] - cal.firstDayOfWeek+1
        val today = cal[Calendar.DAY_OF_WEEK] -1
        // 해당 주차의 첫째날을 지정한다
        cal.add(Calendar.DAY_OF_MONTH, -dayOfWeek)
        val sf = SimpleDateFormat("dd")
        val sf2 = SimpleDateFormat("yyyyMMdd")

        for(index in 0 until 7) {
            cal.add(Calendar.DAY_OF_MONTH, 1)
            dates.add(sf.format(cal.time))
            alldays.add(sf2.format(cal.time))
        }
        return today
    }

}

