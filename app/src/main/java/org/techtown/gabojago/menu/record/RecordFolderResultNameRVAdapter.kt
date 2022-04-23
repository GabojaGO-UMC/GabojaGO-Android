package org.techtown.gabojago.menu.record

import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.recyclerview.widget.RecyclerView
import org.techtown.gabojago.databinding.ItemRecordFoldernameBinding
import org.techtown.gabojago.menu.record.recordRetrofit.FolderResultList
import org.techtown.gabojago.menu.record.recordRetrofit.InFolderListResult


import android.widget.TextView
import org.techtown.gabojago.R

//폴더 겉부분 리사이클러뷰 어댑터
class RecordFolderResultNameRVAdapter(private val folderList: ArrayList<FolderResultList>) :
    RecyclerView.Adapter<RecordFolderResultNameRVAdapter.ViewHolder>() {

    //클릭 인터페이스
    interface MyItemClickListener {
        fun onItemClickPencil(
            hasRecording: Boolean,
            folderIdx: Int,
            resultList: ArrayList<InFolderListResult>
        )

        fun onItemView(hasRecording: Boolean,folderIdx: Int)
        fun onModifyClick(folder: FolderResultList)
        fun onBreakUpClick(folderIdx: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    //뷰홀더 생성->호출되는 함수->아이템 뷰 객체를 만들어서 뷰홀더에 던져줌
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRecordFoldernameBinding =
            ItemRecordFoldernameBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    //뷰홀더에 데이터를 바인딩해줘야 할 때마다 호출되는 함수 => 엄청나게 많이 호출
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(folderList[position])
        if(folderList[position].hasRecording) {
            //아이템 자체 클릭
            holder.itemView.setOnClickListener {
                mItemClickListener.onItemView(folderList[position].hasRecording,
                    folderList[position].folderIdx)
            }
        }
        //연필이미지 클릭
        holder.binding.folderRecordPecilIv.setOnClickListener {
            mItemClickListener.onItemClickPencil(folderList[position].hasRecording,
                folderList[position].folderIdx,
                folderList[position].randomResultListResult)
        }
        //폴더이미지 클릭
        holder.binding.folderRecordFolderIv.setOnClickListener {
            //팝업창 띄우기
            val inflater = LayoutInflater.from(holder.binding.folderRecordFolderIv.context)
            val popupView = inflater.inflate(R.layout.popup_menu, null, false)
            //팝업창 크기조절
            val popup = PopupWindow(popupView,
                310,
                240,
                true)

            //팝업창 화면 내에서 터치한 부분에 위치시키기
            val location = IntArray(2)
            holder.binding.folderRecordFolderIv.getLocationOnScreen(location)
            popup.showAtLocation(holder.binding.folderRecordFolderIv,
                Gravity.NO_GRAVITY,
                location[0] - 120,
                location[1] + 100)
            popup.isTouchable = true

            //팝업창 내부에서 폴더수정 text 클릭 이벤트
            val modify = popupView.findViewById(R.id.popup_modify_tv) as TextView
            modify.setOnClickListener {
                Log.e("popup", folderList[position].toString())
                mItemClickListener.onModifyClick(folderList[position])
                popup.dismiss()
            }

            //팝업창 내부에서 폴더해체 text 클릭 이벤트
            val breakup = popupView.findViewById(R.id.popup_folderdelete_tv) as TextView
            breakup.setOnClickListener {
                Log.e("popup", folderList[position].toString())
                mItemClickListener.onBreakUpClick(folderList[position].folderIdx)
                popup.dismiss()
            }
        }
    }

    //뷰홀더
    inner class ViewHolder(val binding: ItemRecordFoldernameBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(folderList: FolderResultList) {
            if (folderList.randomResultListResult != null) {
                //폴더 내부 기록 리사이클러뷰 어댑터
                val recordFolderResultRVAdapter =
                    RecordFolderResultRVAdapter(folderList.hasRecording,
                        folderList.randomResultListResult)
                binding.itemRecordResultRecyclerview.adapter = recordFolderResultRVAdapter
            }
            //기록여부에 따라 색 및 이미지 조정
            if (folderList.hasRecording) {
                binding.folderRecordResultLayout.setBackgroundResource(R.drawable.folderresultbox_orange)
                binding.folderRecordFolderIv.setImageResource(R.drawable.folder_orange)
                binding.folderRecordPecilIv.setImageResource(R.drawable.memo_pencil_orange)
                binding.folderRecordTitleTv.setTextColor(Color.parseColor("#ff6745"))
                if (folderList.folderTitle != null) {
                    binding.folderRecordTitleTv.setText(folderList.folderTitle)
                }
            } else {
                binding.folderRecordResultLayout.setBackgroundResource(R.drawable.folderresultbox)
                binding.folderRecordFolderIv.setImageResource(R.drawable.folder_gray)
                binding.folderRecordPecilIv.setImageResource(R.drawable.memo_pencil_bluegray)
            }
        }
    }
    override fun getItemCount(): Int = folderList.size
}