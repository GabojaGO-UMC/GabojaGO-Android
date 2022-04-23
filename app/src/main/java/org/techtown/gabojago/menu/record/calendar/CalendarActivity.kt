

package org.techtown.gabojago.menu.record.calendar
import HorizontalItemDecorator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.techtown.gabojago.R
import org.techtown.gabojago.databinding.ActivityCalendarBinding
import org.techtown.gabojago.main.MainActivity
import org.techtown.gabojago.main.MyToast
import org.techtown.gabojago.main.getJwt
import org.techtown.gabojago.menu.record.RecordFragment
import java.text.SimpleDateFormat
import java.util.*

class CalendarActivity : Fragment(), NicknameAdventureView, AdventureTimeView {
    lateinit var binding: ActivityCalendarBinding
    private lateinit var callback: OnBackPressedCallback
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
    lateinit var  userJwt :String
    private val calendarService = CalendarService()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
        ): View? {
        binding = ActivityCalendarBinding.inflate(inflater, container, false)
        userJwt = getJwt(requireContext(), "userJwt")

        init()
        setMonth()

        //달력 레이아웃 매니저
        val gridLayoutManager = GridLayoutManager(requireContext(), 7)
        binding.calendarGridview.layoutManager = gridLayoutManager

        val display = requireActivity().windowManager.defaultDisplay // in case of Fragment
        val size = Point()
        display.getSize(size)
        val width = size.x
        val height = size.y
        binding.calendarGridview.addItemDecoration(HorizontalItemDecorator(width/55,height/1000))

        binding.calendarEmpty1.layoutParams.height = height/15
        binding.calendarEmpty3.layoutParams.height = height/100
        binding.calendarCalendarIv.layoutParams.height = height/2
        binding.calendarCalendarIv.layoutParams.width = width*4/5

        viewDate = initDate().substring(0,6)

        calendarService.setNicknameAdventureView(this@CalendarActivity)
        calendarService.setAdventureTimeView(this@CalendarActivity)
        calendarService.getNicknameAdventure(userJwt)

        val cal = Calendar.getInstance()
        cal.set(initDate().substring(0,4).toInt(), initDate().substring(4,6).toInt() - 1, 1)
        val date = Date(cal.timeInMillis)
        val dateFormat2 = SimpleDateFormat("yyyyMMdd", Locale("ko", "KR"))
        viewDate = dateFormat2.format(date)
        calendarService.getAdventureTime(userJwt, viewDate.substring(0,6))
        binding.calendarNextTv.setOnClickListener {
            Log.e("이후달",(cal.get(Calendar.MONTH )+1).toString())
            if (cal.get(Calendar.YEAR) > initDate().substring(0,4).toInt() || (cal.get(Calendar.YEAR) == initDate().substring(0,4).toInt() && cal.get(
                    Calendar.MONTH)+1 >= initDate().substring(4,6).toInt())
            ) {
                MyToast.createToast(
                    requireContext(), "기록이 없는 달이야!", 90, false
                ).show()
            } else {
                cal.add(Calendar.MONTH, +1)
                binding.calendarDateTv.text = cal.get(Calendar.YEAR).toString() + ", " + (cal.get(Calendar.MONTH) + 1).toString() + "월"
                val date = Date(cal.timeInMillis)
                val dateFormat2 = SimpleDateFormat("yyyyMMdd", Locale("ko", "KR"))
                viewDate = dateFormat2.format(date)
                val dateFormat = SimpleDateFormat("yyyyMM", Locale("ko", "KR"))
                val stringDate = dateFormat.format(date)
                calendarService.getAdventureTime(userJwt, stringDate)
            }
        }
        binding.calendarPreviewTv.setOnClickListener {
            if ((cal.get(Calendar.YEAR) > registerDateArray[0].toInt() && cal.get(Calendar.MONTH) == 11) || (cal.get(
                    Calendar.YEAR) == registerDateArray[0].toInt() && cal.get(Calendar.MONTH) + 1 <= registerDateArray[1].toInt())
            ) {
                MyToast.createToast(
                    requireContext(), "기록이 없는 달이야!", 90, false
                ).show()
            } else {
                cal.add(Calendar.MONTH, -1)
                Log.e("이전달",(cal.get(Calendar.MONTH) + 1).toString())
                binding.calendarDateTv.text = cal.get(Calendar.YEAR)
                    .toString() + ", " + (cal.get(Calendar.MONTH) + 1).toString() + "월"
                val date = Date(cal.timeInMillis)
                val dateFormat = SimpleDateFormat("yyyyMM", Locale("ko", "KR"))
                val dateFormat2 = SimpleDateFormat("yyyyMMdd", Locale("ko", "KR"))
                viewDate = dateFormat2.format(date)
                val stringDate = dateFormat.format(date)
                calendarService.getAdventureTime(userJwt, stringDate)
                Log.e("이전달2",(cal.get(Calendar.MONTH )+1).toString())
            }
        }
        return binding.root
    }

    private fun init() {
        binding.calendarDateTv.text = setMonth()
        binding.recordLookBackBtn.setOnClickListener{
            activity?.supportFragmentManager!!.beginTransaction()
                .replace(R.id.main_frm, RecordFragment())
                .commit()
        }
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
        val date = Date(now)
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale("ko", "KR"))
        val stringDate = dateFormat.format(date)
        Log.e("날짜",stringDate)
        return stringDate
    }

    private fun setRegisterDate(registerDate: String): Array<String> {
        val dateArray = registerDate.split("-").toTypedArray()
        return dateArray
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.e("back","backpress")
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, RecordFragment())
                    .commitAllowingStateLoss()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    override fun onNicknameAdventureSuccess(userNicknameAdventure: NicknameAdventureResult) {
        binding.calendarNameTv.text = userNicknameAdventure.userNicknameAdventure
        binding.calendarBlurView.visibility = View.GONE
        binding.calendarLoadingPb.visibility=View.GONE
    }

    override fun onNicknameAdventureLoading() {
        binding.calendarLoadingPb.visibility=View.VISIBLE
        binding.calendarBlurView.visibility = View.VISIBLE
    }

    override fun onNicknameAdventureFailure(code: Int, message: String) {
        MyToast.createToast(
            requireContext(), message, 90, true
        ).show()
    }

    override fun onAdventureTimeSuccess(adventureTime: AdventureTimeResult) {
        //모험날짜배열 초기화
        randomresultdateList.clear()
        for (i in 0 until 32) {
            randomresultdateList.add(i,-1)
        }
        binding.calendarLoadingPb.visibility = View.GONE
        binding.calendarBlurView.visibility = View.GONE
        userJoinDate = adventureTime.userJoinDate
        Log.e("user", adventureTime.userJoinDate)
        registerDateArray = setRegisterDate(userJoinDate)
        binding.calendarTotalNumberTv.text = (adventureTime.monthlyAdventureTimes).toString()

        for (i in 0 until adventureTime.randomresultdateList.size) {
            randomresultdateList.set(adventureTime.randomresultdateList[i].day,(adventureTime.randomresultdateList[i].day))
        }
        val calendarAdapter = CalendarAdapter(viewDate,randomresultdateList)
        binding.calendarGridview.adapter = calendarAdapter
        calendarAdapter.setMyItemClickListener(object :
            CalendarAdapter.MyItemClickListener {
            override fun onItemClick(date: String) {
                var recordFragment = RecordFragment()
                var bundle = Bundle()
                bundle.putString("changeDate", date)
                Log.e("date",date)
                recordFragment.arguments = bundle
                activity?.supportFragmentManager!!.beginTransaction()
                    .replace(R.id.main_frm, recordFragment)
                    .commit()
            }
        }
        )
    }

    override fun onAdventureTimeLoading() {
        binding.calendarLoadingPb.visibility = View.VISIBLE
        binding.calendarBlurView.visibility = View.VISIBLE
    }

    override fun onAdventureTimeFailure(code: Int, message: String) {
        binding.calendarLoadingPb.visibility = View.GONE
        binding.calendarBlurView.visibility = View.GONE

        MyToast.createToast(
            requireContext(), message, 90, true
        ).show()
    }
}