

package org.techtown.gabojago.menu.record.calendar
import HorizontalItemDecorator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import org.techtown.gabojago.databinding.ActivityCalendarBinding
import org.techtown.gabojago.main.getJwt
import java.text.SimpleDateFormat
import java.util.*

class CalendarActivity :AppCompatActivity(), NicknameAdventureView, AdventureTimeView {
    lateinit var binding: ActivityCalendarBinding
    //보여줄 년, 월
    private var viewDate = ""
    //받아온 유저가입날짜
    private var userJoinDate = ""
    //서버로 보내는 보여줄 년, 월
    private var yearMonth = ""
    //유저가입날짜 배열
    private var registerDateArray = arrayOf<String>("", "", "")
    //모험한 날짜 배열
    private val randomresultdateList = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        //모험날짜배열 초기화
        for (i in 0 until 31) {
            randomresultdateList.add(-1)
        }

        init()
        setMonth()
        monthClick()

        //달력 레이아웃 매니저
        val gridLayoutManager = GridLayoutManager(this, 7)
        binding.calendarGridview.layoutManager = gridLayoutManager

        binding.calendarGridview.addItemDecoration(HorizontalItemDecorator(28))

        val userJwt = getJwt(this, "userJwt")

        val dateFormat2 = SimpleDateFormat("yyyyMM", Locale("ko", "KR"))

        yearMonth = dateFormat2.format(monthClick())

        val calendarService = CalendarService()

        calendarService.setNicknameAdventureView(this@CalendarActivity)
        calendarService.setAdventureTimeView(this@CalendarActivity)

        calendarService.getNicknameAdventure(userJwt)
        calendarService.getAdventureTime(userJwt, yearMonth)
    }

    private fun init() {
        binding.calendarDateTv.text = setMonth()
    }

    private fun monthClick(): Long {
        val cal = Calendar.getInstance()
        val dateArray = initDate().split("-").toTypedArray()
        cal.set(dateArray[0].toInt(), dateArray[1].toInt() - 1, 1)
        viewDate = cal.get(Calendar.YEAR)
            .toString() + "-" + (cal.get(Calendar.MONTH) + 1).toString() + "-" + "01"
        binding.calendarNextTv.setOnClickListener {
            if (cal.get(Calendar.YEAR) > dateArray[0].toInt() || (cal.get(Calendar.YEAR) == dateArray[0].toInt() && cal.get(
                    Calendar.MONTH + 1) > dateArray[1].toInt())
            ) {
                Toast.makeText(
                    this, "기록이 없는 달이야!", Toast.LENGTH_SHORT
                ).show()
            } else {
                cal.add(Calendar.MONTH, +1)
            }
            binding.calendarDateTv.text = cal.get(Calendar.YEAR).toString() + ", " + (cal.get(Calendar.MONTH) + 1).toString() + "월"
            viewDate = cal.get(Calendar.YEAR)
                .toString() + "-" + (cal.get(Calendar.MONTH) + 1).toString() + "-" + "01"
            val calendarAdapter = CalendarAdapter(viewDate,randomresultdateList)
            binding.calendarGridview.adapter = calendarAdapter
        }

        binding.calendarPreviewTv.setOnClickListener {
            if ((cal.get(Calendar.YEAR) > registerDateArray[0].toInt() && cal.get(Calendar.MONTH) == 11) || (cal.get(
                    Calendar.YEAR) == registerDateArray[0].toInt() && cal.get(Calendar.MONTH) + 1 <= registerDateArray[1].toInt())
            ) {
                Toast.makeText(
                    this, "기록이 없는 달이야!", Toast.LENGTH_SHORT
                ).show()
            } else {
                cal.add(Calendar.MONTH, -1)
            }
            binding.calendarDateTv.text = cal.get(Calendar.YEAR)
                .toString() + ", " + (cal.get(Calendar.MONTH) + 1).toString() + "월"
            viewDate = cal.get(Calendar.YEAR)
                .toString() + "-" + (cal.get(Calendar.MONTH) + 1).toString() + "-" + "01"
            val calendarAdapter = CalendarAdapter(viewDate,randomresultdateList)
            binding.calendarGridview.adapter = calendarAdapter
        }
        return cal.timeInMillis
    }

    private fun setMonth(): String {
        val now: Long = System.currentTimeMillis()
        val date = Date(now)
        val dateFormat = SimpleDateFormat("yyyy", Locale("ko", "KR"))
        val dateFormat2 = SimpleDateFormat("MM", Locale("ko", "KR"))
        val stringDate =
            dateFormat.format(date) + ", " + (dateFormat2.format(date).toInt()).toString() + "월"
        return stringDate
    }

    fun initDate(): String {
        val now: Long = System.currentTimeMillis()
        Log.e("long타입1", now.toString())
        val date = Date(now)
        Log.e("long타입2", date.toString())
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("ko", "KR"))
        val stringDate = dateFormat.format(date)
        return stringDate
    }

    private fun setRegisterDate(registerDate: String): Array<String> {
        val dateArray = registerDate.split("-").toTypedArray()
        return dateArray
    }

    override fun onNicknameAdventureSuccess(userNicknameAdventure: NicknameAdventureResult) {
        binding.calendarNameTv.text = userNicknameAdventure.userNicknameAdventure
    }

    override fun onNicknameAdventureFailure(code: Int, message: String) {
        Toast.makeText(
            this, message, Toast.LENGTH_SHORT
        ).show()
    }

    override fun onAdventureTimeSuccess(adventureTime: AdventureTimeResult) {
        binding.calendarLoadingPb.visibility = View.GONE
        userJoinDate = adventureTime.userJoinDate
        Log.e("user", adventureTime.userJoinDate)
        registerDateArray = setRegisterDate(userJoinDate)
        binding.calendarTotalNumberTv.text = (adventureTime.monthlyAdventureTimes).toString()

        for (i in 0 until adventureTime.randomresultdateList.size) {
            randomresultdateList.set(adventureTime.randomresultdateList[i].day,(adventureTime.randomresultdateList[i].day))
        }
        Log.e("day", adventureTime.randomresultdateList.toString())
        val calendarAdapter = CalendarAdapter(viewDate,randomresultdateList)
        binding.calendarGridview.adapter = calendarAdapter

    }

    override fun onAdventureTimeLoading() {
        binding.calendarLoadingPb.visibility = View.VISIBLE
    }

    override fun onAdventureTimeFailure(code: Int, message: String) {
        binding.calendarLoadingPb.visibility = View.GONE

        Toast.makeText(
            this, message, Toast.LENGTH_SHORT
        ).show()
    }
}