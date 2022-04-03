package org.techtown.gabojago.menu.record.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.techtown.gabojago.R
import org.techtown.gabojago.databinding.ItemRecordFolderBinding
import org.techtown.gabojago.menu.record.recordRetrofit.InFolderListResult

class DialogDeleteResultRVAdapter(private val hasRecording:Boolean ,private val resultList: ArrayList<InFolderListResult>): RecyclerView.Adapter<DialogDeleteResultRVAdapter.ViewHolder>() {

    //뷰홀더 생성->호출되는 함수->아이템 뷰 객체를 만들어서 뷰홀더에 던져줌
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRecordFolderBinding =
            ItemRecordFolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    //뷰홀더에 데이터를 바인딩해줘야 할 때마다 호출되는 함수 => 엄청나게 많이 호출
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(resultList[position]!=null) {
            holder.bind(resultList[position])
        }
    }

    //뷰홀더
    inner class ViewHolder(val binding: ItemRecordFolderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result:InFolderListResult) {
            binding.itemFolderrecordResultTv.text = result.resultContent
            if(hasRecording){
                binding.itemFolderrecordLineIv.setBackgroundResource(R.drawable.line_18)
                when (result.resultType) {
                    1 -> {
                        binding.itemFolderrecordTitleIv.setImageResource(R.drawable.dolimpan)
                        binding.itemFolderrecordCircleIv.setImageResource(R.drawable.resultimage_dolimpan_orange)
                    }2 -> {
                        binding.itemFolderrecordTitleIv.setImageResource(R.drawable.nsibanghiang)
                        binding.itemFolderrecordCircleIv.setImageResource(R.drawable.resultimage_nsibang_orange)
                    }3 -> {
                        binding.itemFolderrecordTitleIv.setImageResource(R.drawable.colorbox)
                        binding.itemFolderrecordCircleIv.setImageResource(R.drawable.resultimage_japangi_orange)
                    }4 -> {
                        binding.itemFolderrecordTitleIv.setImageResource(R.drawable.binglebingle)
                        binding.itemFolderrecordCircleIv.setImageResource(R.drawable.resultimage_random_orange)
                    }
                }

            } else {
                binding.itemFolderrecordLineIv.setBackgroundResource(R.drawable.line_gray)
                when (result.resultType) {
                    1 -> {
                        binding.itemFolderrecordTitleIv.setImageResource(R.drawable.dolimpan)
                        binding.itemFolderrecordCircleIv.setImageResource(R.drawable.resultimage_dolimpan_gray)
                    }2 -> {
                        binding.itemFolderrecordTitleIv.setImageResource(R.drawable.nsibanghiang)
                        binding.itemFolderrecordCircleIv.setImageResource(R.drawable.resultimage_nsibang)
                    }3 -> {
                        binding.itemFolderrecordTitleIv.setImageResource(R.drawable.colorbox)
                        binding.itemFolderrecordCircleIv.setImageResource(R.drawable.resultimage_japangi)
                    }4 -> {
                        binding.itemFolderrecordTitleIv.setImageResource(R.drawable.binglebingle)
                        binding.itemFolderrecordCircleIv.setImageResource(R.drawable.resultimage_random_gray)
                    }
                }
            }
        }

    }

    override fun getItemCount(): Int = resultList.size

}