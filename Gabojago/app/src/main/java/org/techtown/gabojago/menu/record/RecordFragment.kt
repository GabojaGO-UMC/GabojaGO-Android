package org.techtown.gabojago.menu.record

import HorizontalItemDecorator
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import org.techtown.gabojago.main.MainActivity
import org.techtown.gabojago.R
import org.techtown.gabojago.databinding.FragmentRecordBinding
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.Point
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.animation.OvershootInterpolator
import org.techtown.gabojago.menu.record.calendar.CalendarActivity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import kotlinx.coroutines.NonDisposableHandle
import org.techtown.gabojago.databinding.ItemRecordFoldernameBinding
import org.techtown.gabojago.main.getJwt
import org.techtown.gabojago.menu.record.dialog.DialogFolderDelete
import org.techtown.gabojago.menu.record.dialog.DialogFolderModify
import org.techtown.gabojago.menu.record.dialog.DialogFolderSelect
import org.techtown.gabojago.menu.record.look.RecordLookFragment
import org.techtown.gabojago.menu.record.recordRetrofit.*



class RecordFragment : Fragment(), RecordCountView, SingleResultListView, FolderResultListView ,FolderBreakView{

    lateinit var binding: FragmentRecordBinding
    lateinit var binding2: ItemRecordFoldernameBinding

    var records = ArrayList<SingleResultListResult>()
    var folders = ArrayList<FolderResultList>()
    var add: Boolean = true
    val recordService = RecordService()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentRecordBinding.inflate(inflater, container, false)
        binding2 = ItemRecordFoldernameBinding.inflate(inflater, container, false)


        recordService.setRecordCountView(this@RecordFragment)
        recordService.setSingleResultListView(this@RecordFragment)
        recordService.setFolderResultListView(this@RecordFragment)

        val userJwt = getJwt(requireContext(), "userJwt")

        val now: Long = System.currentTimeMillis()
        val date = Date(now)
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale("ko", "KR"))
        val stringDate = dateFormat.format(date)

        recordService.getSingleResultList(userJwt,stringDate)
        recordService.getFolderResultList(userJwt,stringDate)

        recordService.recordCount(userJwt)



        binding.recordFolderresultRecyclerview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.recordResultRecyclerview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.recordWeekRecyclerview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val recordWeekRVAdapter = RecordWeekRVAdapter()
        binding.recordWeekRecyclerview.adapter = recordWeekRVAdapter



        val width  = getScreenSize(this)
        binding.recordWeekRecyclerview.addItemDecoration(HorizontalItemDecorator(width/42))


        clickevent()
        init()

        return binding.root
    }
    private fun clickevent() {
        binding.recordMonthTv.setOnClickListener {
            startActivity(Intent(activity, CalendarActivity::class.java))
        }

        binding.recordMoreIv.setOnClickListener {
            binding.recordBlurView.visibility = View.VISIBLE
            popupMenu()
        }

        binding.recordCloseIv.setOnClickListener {
            binding.recordBlurView.visibility = View.GONE
            popupMenu()
        }

        binding.recordBlurView.setOnClickListener {
            binding.recordBlurView.visibility = View.GONE
            popupMenu()
        }

    }

    private fun init() {
        binding.recordDateTv.setText(setdate())
        binding.recordMonthTv.setText(setMonth())
    }

    private fun changeSingleRecordFragment(recordIdx: Int) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, SingleRecordFragment(recordIdx).apply {
                arguments = Bundle().apply {
                }
            })
            .addToBackStack(null)
            .commitAllowingStateLoss()

    }

    private fun changeFolderRecordFragment(folderIdx:Int, resultList:ArrayList<InFolderListResult>) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, FolderRecordFragment(folderIdx,resultList).apply {
                arguments = Bundle().apply {
                }
            })
            .addToBackStack(null)
            .commitAllowingStateLoss()

    }

    private fun changeRecordFragment(folderIdx: Int){
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, RecordLookFragment(folderIdx).apply {
                arguments = Bundle().apply {
                }
            })
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    private fun setdate(): String {
        val now: Long = System.currentTimeMillis()
        val date = Date(now)
        val dateFormat = SimpleDateFormat("yyyy년MM월dd일", Locale("ko", "KR"))
        val stringDate = dateFormat.format(date)

        return stringDate
    }

    private fun setMonth(): String {
        val now: Long = System.currentTimeMillis()
        val month = Date(now)
        val monthFormat = SimpleDateFormat("MM", Locale("ko", "KR"))
        val stringMonth = "< "+(monthFormat.format(month).toInt()).toString()+"월"

        return stringMonth
    }

    fun getScreenSize(fragment : Fragment): Int {
        val display = requireActivity().windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x
        return width
    }

    private fun popupMenu() {

        var px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60f, this.resources.displayMetrics)

        if (add) {

            var writeAnimator = ObjectAnimator.ofFloat(binding.recordFolderplusIv, "translationY", 0f, -px)
            writeAnimator.duration = 400
            writeAnimator.interpolator = OvershootInterpolator()
            writeAnimator.target = binding.recordFolderplusIv
            writeAnimator.start()

            var photoAnimator = ObjectAnimator.ofFloat(binding.recordTrashIv, "translationY", 0f, -px*2)
            photoAnimator.duration = 500
            photoAnimator.interpolator = OvershootInterpolator()
            photoAnimator.target = binding.recordTrashIv
            photoAnimator.start()

            binding.recordMoreIv.visibility = View.GONE
            binding.recordCloseIv.visibility = View.VISIBLE
            add = !add
        } else {

            var writeAnimator = ObjectAnimator.ofFloat(binding.recordFolderplusIv, "translationY", -px, 0f)
            writeAnimator.duration = 400
            writeAnimator.interpolator = OvershootInterpolator()
            writeAnimator.target = binding.recordFolderplusIv
            writeAnimator.start()

            var photoAnimator = ObjectAnimator.ofFloat(binding.recordTrashIv, "translationY", -px*2, 0f)
            photoAnimator.duration = 500
            photoAnimator.interpolator = OvershootInterpolator()
            photoAnimator.target = binding.recordTrashIv
            photoAnimator.start()

            binding.recordMoreIv.visibility = View.VISIBLE
            binding.recordCloseIv.visibility = View.GONE

            add = !add
        }
    }

    override fun onRecordCountLoading() {
        binding.recordCountTv.text = "0"
    }

    override fun onRecordCountSuccess(result: Int) {
        binding.recordCountTv.text = result.toString()
    }

    override fun onRecordCountFailure(code: Int, message: String) {
        Toast.makeText(
            activity, message, Toast.LENGTH_SHORT
        ).show()
    }

    override fun onSingleResultListSuccess(result: ArrayList<SingleResultListResult>) {

        records = result
        val recordResultRVAdapter = RecordResultRVAdapter(records)
        binding.recordResultRecyclerview.adapter = recordResultRVAdapter


        binding.recordFolderplusIv.setOnClickListener{
            DialogFolderSelect(records).show((context as MainActivity).supportFragmentManager,"dialog")
        }

        binding.recordTrashIv.setOnClickListener{
            DialogFolderDelete(records,folders).show((context as MainActivity).supportFragmentManager,"dialog")
        }



        recordResultRVAdapter.setMyItemClickListener(object :
            RecordResultRVAdapter.MyItemClickListener {
            override fun onItemClick(recordIdx :Int) {
                changeSingleRecordFragment(recordIdx)
            }
            override fun onItemView() {
                changeRecordFragment(1)
            }
        })


    }

    override fun onSingleResultListFailure(code: Int, message: String) {
        Log.e("기록하자개별메인api",message)
    }

    override fun onFolderResultListSuccess(result: ArrayList<FolderResultList>) {
        folders = result

        val recordFolderResultNameRVAdapter = RecordFolderResultNameRVAdapter(folders)
        binding.recordFolderresultRecyclerview.adapter = recordFolderResultNameRVAdapter

        binding.recordTrashIv.setOnClickListener{
            DialogFolderDelete(records,folders).show((context as MainActivity).supportFragmentManager,"dialog")
        }

        recordFolderResultNameRVAdapter.setMyItemClickListener(object :
            RecordFolderResultNameRVAdapter.MyItemClickListener {
            override fun onItemClickPencil(folderIdx:Int,resultList:ArrayList<InFolderListResult>) {
                changeFolderRecordFragment(folderIdx,resultList)
            }

            override fun onItemView(folderIdx:Int) {
                changeRecordFragment(folderIdx)
            }

            override fun onModifyClick(folder: FolderResultList) {
                DialogFolderModify(folder).show((context as MainActivity).supportFragmentManager,"dialog")

            }

            override fun onBreakUpClick(folderIdx: Int) {

                recordService.setFolderBreakView(this@RecordFragment)
                val userJwt = getJwt(requireContext(), "userJwt")
                recordService.putBreakFolderIdx(userJwt,folderIdx)

            }
        })
    }

    override fun onFolderResultListFailure(code: Int, message: String) {
        Log.e("기록하자폴더메인api",message)
    }

    override fun onFolderBreakSuccess() {
        recordService.setSingleResultListView(this@RecordFragment)
        recordService.setFolderResultListView(this@RecordFragment)

        val userJwt = getJwt(requireContext(), "userJwt")

        val now: Long = System.currentTimeMillis()
        val date = Date(now)
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale("ko", "KR"))
        val stringDate = dateFormat.format(date)


        recordService.getSingleResultList(userJwt,stringDate)
        recordService.getFolderResultList(userJwt,stringDate)

        var ft: FragmentTransaction = requireFragmentManager().beginTransaction()
            ft.detach(this).attach(this).commit()
    }

    override fun onFolderBreakFailure(code: Int, message: String) {
        Toast.makeText(
            activity, message, Toast.LENGTH_SHORT
        ).show()
    }


}

