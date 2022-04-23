package org.techtown.gabojago.menu.record.dialog

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.techtown.gabojago.R
import org.techtown.gabojago.databinding.ItemRecordFoldernameBinding
import org.techtown.gabojago.menu.record.recordRetrofit.FolderResultList

val isFolderSelectList = ArrayList<Boolean>()
class DialogDeleteRVAdapter(private val folderList: ArrayList<FolderResultList>) : RecyclerView.Adapter<DialogDeleteRVAdapter.ViewHolder>() {


    //뷰홀더 생성->호출되는 함수->아이템 뷰 객체를 만들어서 뷰홀더에 던져줌
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRecordFoldernameBinding =
            ItemRecordFoldernameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        isFolderSelectList.clear()
        for (i in 0 until 31) {
            isFolderSelectList.add(false)
        }
        return ViewHolder(binding)
    }

    //뷰홀더에 데이터를 바인딩해줘야 할 때마다 호출되는 함수 => 엄청나게 많이 호출
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(folderList[position])
        holder.itemView.setOnClickListener {
            if(!isFolderSelectList[position]) {
                isFolderSelectList[position]= true
                holder.binding.folderRecordResultLayout.setBackgroundResource(R.drawable.folderresultbox_selectorange)
            }else{
                isFolderSelectList[position]= false
                holder.binding.folderRecordResultLayout.setBackgroundResource(R.drawable.folderresultbox_orange)
            }
        }

    }

    //뷰홀더
    inner class ViewHolder(val binding: ItemRecordFoldernameBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(folderList: FolderResultList) {
            if (folderList.randomResultListResult != null) {
                val dialogFolderResultRVAdapter =
                    DialogDeleteResultRVAdapter(true,
                        folderList.randomResultListResult)
                binding.itemRecordResultRecyclerview.adapter = dialogFolderResultRVAdapter
            }
            binding.folderRecordResultLayout.setBackgroundResource(R.drawable.folderresultbox_orange)
            binding.folderRecordFolderIv.setImageResource(R.drawable.folder_orange)
            binding.folderRecordPecilIv.setImageResource(R.drawable.memo_pencil_orange)
            binding.folderRecordTitleTv.setTextColor(Color.parseColor("#fc8f77"))
            if (folderList.folderTitle != null) {
                binding.folderRecordTitleTv.setText(folderList.folderTitle)
            }

        }

    }

    override fun getItemCount(): Int = folderList.size

}
