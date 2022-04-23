package org.techtown.gabojago.menu.record

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import org.techtown.gabojago.databinding.FragmentSinglerecordBinding

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.storage.FirebaseStorage
import org.techtown.gabojago.main.MainActivity
import org.techtown.gabojago.R
import org.techtown.gabojago.main.MyToast
import org.techtown.gabojago.main.getJwt
import org.techtown.gabojago.menu.record.dialog.DialogRealFolderrecordDelete
import org.techtown.gabojago.menu.record.dialog.DialogRealRecordDelete
import org.techtown.gabojago.menu.record.recordRetrofit.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class SingleRecordFragment(private  val hasRecording:Boolean,private  val recordIdx:Int,private val result:RandomResultListResult,private val day:String) : Fragment() ,SingleRecordingView, SingleLookView, SingleModifyView{
    lateinit var binding: FragmentSinglerecordBinding
    private lateinit var callback: OnBackPressedCallback
    val imgFileName = java.util.ArrayList<String>()
    val uriList = java.util.ArrayList<Uri?>()
    val urlList = java.util.ArrayList<String>()
    val imageList = java.util.ArrayList<String>()
    var fbStorage : FirebaseStorage? = null
    val recordService = RecordService()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        uriList.clear()
        binding = FragmentSinglerecordBinding.inflate(inflater, container, false)

        recordService.setSingleLookView(this@SingleRecordFragment)
        val userJwt = getJwt(requireContext(), "userJwt")
        fbStorage = FirebaseStorage.getInstance()

        binding.singleRecordTrash.setOnClickListener {
            val dialogRealRecordDelete =  DialogRealRecordDelete(recordIdx)
            dialogRealRecordDelete.show((context as MainActivity).supportFragmentManager,"dialog")
            binding.singleRecordBlurView.visibility = View.VISIBLE
            binding.singleRecordBlurView2.visibility = View.VISIBLE
            dialogRealRecordDelete.setOnDismissClickListener(object :
                DialogRealRecordDelete.onDismissListener{
                override fun onDismiss(dialogFragment: DialogFragment) {
                    binding.singleRecordBlurView.visibility = View.GONE
                    binding.singleRecordBlurView2.visibility = View.GONE
                }
            })
        }
        var uriList = ArrayList<Uri?>()
        val recordPictureRVAdapter = RecordPictureRVAdapter(uriList,imageList)
        binding.singleRecordPictureRecyclerview.adapter = recordPictureRVAdapter

        binding.singleRecordWriteEt.addTextChangedListener(object: TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.singleRecordCharactersnumTv.text = "0 / 1000"
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var userinput = binding.singleRecordWriteEt.text.toString()
                binding.singleRecordCharactersnumTv.text = userinput.length.toString() + " / 1000"
            }

            override fun afterTextChanged(s: Editable?) {
                var userinput = binding.singleRecordWriteEt.text.toString()
                binding.singleRecordCharactersnumTv.text = userinput.length.toString() + " / 1000"
            }

        })

        binding.singleRecordResultTv.text = result.randomResultContent
        when (result.randomResultType) {
            1 -> {
                binding.singleRecordTitleIv.setImageResource(R.drawable.dolimpan)
                binding.singleRecordCircleIv.setImageResource(R.drawable.resultimage_dolimpan_orange)
            }2 -> {
                binding.singleRecordTitleIv.setImageResource(R.drawable.nsibanghiang)
                binding.singleRecordCircleIv.setImageResource(R.drawable.resultimage_nsibang_orange)
            }3 -> {
                binding.singleRecordTitleIv.setImageResource(R.drawable.colorbox)
                binding.singleRecordCircleIv.setImageResource(R.drawable.resultimage_japangi_orange)
            }4 -> {
                binding.singleRecordTitleIv.setImageResource(R.drawable.binglebingle)
                binding.singleRecordCircleIv.setImageResource(R.drawable.resultimage_random_orange)
            }
        }

        if(!hasRecording){
            //기록없을때 카메라 버튼 누를시
            binding.singleRecordPictureIv.setOnClickListener{
                fromGallery()
            }

            binding.singleRecordCompleteView.setOnClickListener {
                val recordService = RecordService()
                recordService.setSingleRecordingView(this@SingleRecordFragment)
                val userJwt = getJwt(requireContext(), "userJwt")
                recordService.putSingleRecord(userJwt, recordIdx, SingleRecordingRequest(
                    binding.singleRecordStarscore.rating.toDouble(),
                    binding.singleRecordWriteEt.text.toString(),
                    binding.singleRecordTitleEt.text.toString(),
                    urlList
                ))

            }

            binding.singleRecordCompleteTv.setOnClickListener {
                val recordService = RecordService()
                recordService.setSingleRecordingView(this@SingleRecordFragment)
                val userJwt = getJwt(requireContext(), "userJwt")
                recordService.putSingleRecord(userJwt, recordIdx, SingleRecordingRequest(
                    binding.singleRecordStarscore.rating.toDouble(),
                    binding.singleRecordWriteEt.text.toString(),
                    binding.singleRecordTitleEt.text.toString(),
                    urlList
                ))

            }

        }else{
            recordService.getSingleLook(userJwt, recordIdx)
            //기록있을때 카메라 버튼 누를시
            binding.singleRecordPictureIv.setOnClickListener{
                imageList.clear()
                funImageDelete()
                fromGallery()
            }

            binding.singleRecordCompleteView.setOnClickListener {
                val recordService = RecordService()
                recordService.setSingleModifyView(this@SingleRecordFragment)
                val userJwt = getJwt(requireContext(), "userJwt")
                recordService.putSingleModify(userJwt, SingleModifyRequest(
                    binding.singleRecordStarscore.rating.toDouble(),
                    binding.singleRecordWriteEt.text.toString(),
                    binding.singleRecordTitleEt.text.toString(),
                    urlList
                ),recordIdx)

            }

            binding.singleRecordCompleteTv.setOnClickListener {
                val recordService = RecordService()
                recordService.setSingleModifyView(this@SingleRecordFragment)
                val userJwt = getJwt(requireContext(), "userJwt")
                recordService.putSingleModify(userJwt, SingleModifyRequest(
                    binding.singleRecordStarscore.rating.toDouble(),
                    binding.singleRecordWriteEt.text.toString(),
                    binding.singleRecordTitleEt.text.toString(),
                    urlList
                ),recordIdx)

            }
        }


        clickevent()
        init()

        return binding.root
    }

    private fun clickevent(){
        binding.singleRecordBackarrow.setOnClickListener{
            var recordFragment = RecordFragment()
            var bundle = Bundle()
            bundle.putString("pickDate", day)
            Log.e("date",day)
            recordFragment.arguments = bundle
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, recordFragment)
                .commitAllowingStateLoss()
            }
    }

    private fun init() {
        binding.singleRecordBlurView2.setOnTouchListener(View.OnTouchListener { v, event -> true })
        binding.singleRecordBlurView.setOnTouchListener(View.OnTouchListener { v, event -> true })
        hideBottomNavigation(true)
    }

    private fun fromGallery() {
        val intent  = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        startActivityForResult(intent,101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                101 -> {
                    if (ContextCompat.checkSelfPermission(requireContext(),
                            android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    ) {
                        if (data == null) {   // 어떤 이미지도 선택하지 않은 경우
                            Toast.makeText(requireContext(), "이미지를 선택해줘!", Toast.LENGTH_LONG)
                                .show()
                        } else {   // 이미지를 하나라도 선택한 경우
                            if (data.clipData == null) {     // 이미지를 하나만 선택한 경우
                                Log.e("single choice: ", data?.data.toString())
                                val imageUri = data?.data
                                uriList.add(imageUri)
                                imgFileName.add("IMAGE_" + timeStamp +"_0"+ "_.png")
                                funImageUpload()
                                binding.singleRecordPicturenumTv.text = uriList.size.toString()+"/10"
                                val recordPictureRVAdapter = RecordPictureRVAdapter(uriList,imageList)
                                binding.singleRecordPictureRecyclerview.adapter =
                                    recordPictureRVAdapter
                            } else {      // 이미지를 여러장 선택한 경우
                                var clipData = data?.clipData
                                Log.e("clipData", clipData?.itemCount.toString())

                                if (clipData?.itemCount != null && clipData?.itemCount > 10) {   // 선택한 이미지가 11장 이상인 경우
                                    Toast.makeText(requireContext(),
                                        "사진은 10장까지 선택 가능해!", Toast.LENGTH_LONG).show()
                                } else {   // 선택한 이미지가 1장 이상 10장 이하인 경우
                                    Log.e("1장이상10장이하", "multiple choice")
                                    for (i in 0 until clipData?.itemCount!!) {
                                        var imageUri =
                                            clipData.getItemAt(i).uri // 선택한 이미지들의 uri를 가져온다.
                                        try {
                                            uriList.add(imageUri)  //uri를 list에 담는다.
                                            imgFileName.add("IMAGE_" + timeStamp +"_"+i.toString()+ "_.png")
                                        } catch (e: Exception) {
                                            Log.e("선택에러", "File select error", e)
                                        }
                                    }
                                    funImageUpload()
                                    binding.singleRecordPicturenumTv.text = uriList.size.toString()+"/10"
                                    val recordPictureRVAdapter = RecordPictureRVAdapter(uriList,imageList)
                                    binding.singleRecordPictureRecyclerview.adapter =
                                        recordPictureRVAdapter
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun funImageUpload() {
        for(i in 0 until uriList.size){
            urlList.add(i,"")
        }
        if (uriList.isNotEmpty()) {
            for(i in 0 until uriList.size) {
                fbStorage?.reference?.child("images")?.child(imgFileName[i])?.putFile(uriList[i]!!)
                    ?.addOnSuccessListener {
                        it.storage.downloadUrl.addOnCompleteListener {
                            urlList[i] = it.result.toString()
                            Log.e("url", urlList.toString())
                            if(i==uriList.size-1) {
                                binding.singleRecordBlurView2.visibility=View.GONE
                                binding.singleRecordBlurView.visibility=View.GONE
                                binding.singleRecordLoadingPb.visibility=View.GONE
                                MyToast.createToast(
                                    requireContext(), "이미지가 업로드 됐어!",90,false
                                ).show()
                            }
                        }
                    }
                    ?.addOnProgressListener {
                        binding.singleRecordBlurView2.visibility=View.VISIBLE
                        binding.singleRecordBlurView.visibility=View.VISIBLE
                        binding.singleRecordLoadingPb.visibility=View.VISIBLE
                    }
            }
        }
    }

    private fun funImageDelete() {
        if (imageList.isNotEmpty()) {
            for(i in 0 until imageList.size) {
                if(imageList[i]!=null) {
                    fbStorage?.getReferenceFromUrl(imageList[i])?.delete()
                        ?.addOnSuccessListener {
                           Log.e("수정할시 삭제","이미지삭제완료")
                        }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideBottomNavigation(false)
    }

    fun hideBottomNavigation(bool: Boolean) {
        val bottomNavigation: BottomNavigationView = requireActivity().findViewById(R.id.main_bnv)
        if (bool == true)
            bottomNavigation.visibility = View.GONE
        else
            bottomNavigation.visibility = View.VISIBLE
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.e("back","backpress")
                var recordFragment = RecordFragment()
                var bundle = Bundle()
                bundle.putString("pickDate", day)
                Log.e("date",day)
                recordFragment.arguments = bundle
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, recordFragment)
                    .commitAllowingStateLoss()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    override fun onSingleRecordingSuccess() {
        binding.singleRecordBlurView.visibility = View.GONE
        binding.singleRecordBlurView2.visibility = View.GONE
        binding.singleRecordLoadingPb.visibility = View.GONE
        Log.e("개별기록","성공")

        var recordFragment = RecordFragment()
        var bundle = Bundle()
        bundle.putString("pickDate", day)
        Log.e("date",day)
        recordFragment.arguments = bundle
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, recordFragment)
            .commitAllowingStateLoss()
    }

    override fun onSingleRecordingFailure(code: Int, message: String) {
        binding.singleRecordBlurView.visibility = View.GONE
        binding.singleRecordBlurView2.visibility = View.GONE
        binding.singleRecordLoadingPb.visibility = View.GONE
        MyToast.createToast(
            requireContext(), message, 90, true
        ).show()
    }

    override fun onSingleLookLoading() {
        binding.singleRecordBlurView.visibility = View.VISIBLE
        binding.singleRecordBlurView2.visibility = View.VISIBLE
        binding.singleRecordLoadingPb.visibility = View.VISIBLE
    }

    override fun onSingleLookSuccess(result: SingleLookResult) {
        try {
            binding.singleRecordBlurView.visibility = View.GONE
            binding.singleRecordBlurView2.visibility = View.GONE
            binding.singleRecordLoadingPb.visibility = View.GONE
            binding.singleRecordTitleEt.setText(result.eachContentResult.recordingTitle)
            binding.singleRecordStarscore.rating = result.eachContentResult.recordingStar.toFloat()
            binding.singleRecordWriteEt.setText(result.eachContentResult.recordingContent)

            for(i in 0 until result.eachImgListResult.size){
                imageList.add(i,result.eachImgListResult[i].recordingImgUrl)

            }
            binding.singleRecordPicturenumTv.text = result.eachImgListResult.size.toString()+"/10"

            val recordPictureRVAdapter = RecordPictureRVAdapter(uriList,imageList)
            binding.singleRecordPictureRecyclerview.adapter =
                recordPictureRVAdapter


        } catch (e: NullPointerException) {
            binding.singleRecordBlurView.visibility = View.GONE
            binding.singleRecordBlurView2.visibility = View.GONE
            binding.singleRecordLoadingPb.visibility = View.GONE
            binding.singleRecordTitleEt.setText("제목을 입력해줘!")
            binding.singleRecordStarscore.rating = 3.5F
            binding.singleRecordWriteEt.setText("내용을 입력해줘!")

        }
    }

    override fun onSingleLookFailure(code: Int, message: String) {
        binding.singleRecordBlurView.visibility = View.GONE
        binding.singleRecordBlurView2.visibility = View.GONE
        binding.singleRecordLoadingPb.visibility = View.GONE
        MyToast.createToast(
            requireContext(), message, 90, true
        ).show()
    }

    override fun onSingleModifySuccess() {
        binding.singleRecordBlurView.visibility = View.GONE
        binding.singleRecordBlurView2.visibility = View.GONE
        binding.singleRecordLoadingPb.visibility = View.GONE
        Log.e("개별기록","성공")

        var recordFragment = RecordFragment()
        var bundle = Bundle()
        bundle.putString("pickDate", day)
        Log.e("date",day)
        recordFragment.arguments = bundle
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, recordFragment)
            .commitAllowingStateLoss()
    }

    override fun onSingleModifyFailure(code: Int, message: String) {
        binding.singleRecordBlurView.visibility = View.GONE
        binding.singleRecordBlurView2.visibility = View.GONE
        binding.singleRecordLoadingPb.visibility = View.GONE
        MyToast.createToast(
            requireContext(), message, 90, true
        ).show()
    }

}