package org.techtown.gabojago.menu.record.dialog

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.techtown.gabojago.R

import org.techtown.gabojago.databinding.ItemRecordresultModifyversionBinding
import org.techtown.gabojago.menu.record.recordRetrofit.SingleResultListResult

val plus = ArrayList<Boolean>()

class DialogModifySingleRVAdapter(private val resultList: ArrayList<SingleResultListResult>): RecyclerView.Adapter<DialogModifySingleRVAdapter.ViewHolder>() {


    //뷰홀더 생성->호출되는 함수->아이템 뷰 객체를 만들어서 뷰홀더에 던져줌
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRecordresultModifyversionBinding =
            ItemRecordresultModifyversionBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false)
        plus.clear()
        for (i in 0 until 31) {
            plus.add(false)
        }
        return ViewHolder(binding)
    }

    //뷰홀더에 데이터를 바인딩해줘야 할 때마다 호출되는 함수 => 엄청나게 많이 호출
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(resultList[position])
        holder.itemView.setOnClickListener {
            if (!plus[position]) {
                plus[position] = true
                holder.binding.itemModifyRectangleIv.setBackgroundResource(R.drawable.single_select_rectangle)
            } else {
                plus[position] = false
                holder.binding.itemModifyRectangleIv.setBackgroundResource(R.drawable.rectangle_orange)
            }
        }
    }

    //뷰홀더
    inner class ViewHolder(val binding: ItemRecordresultModifyversionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: SingleResultListResult) {
            binding.itemModifyResultTv.text = result.randomResultListResult.randomResultContent
            when (result.randomResultListResult.randomResultType) {
                1 -> {
                    binding.itemModifyTitleIv.setImageResource(R.drawable.dolimpan)
                    binding.itemModifyCircleIv.setImageResource(R.drawable.resultimage_dolimpan_orange)
                }
                2 -> {
                    binding.itemModifyTitleIv.setImageResource(R.drawable.nsibanghiang)
                    binding.itemModifyCircleIv.setImageResource(R.drawable.resultimage_nsibang_orange)
                }
                3 -> {
                    binding.itemModifyTitleIv.setImageResource(R.drawable.colorbox)
                    binding.itemModifyCircleIv.setImageResource(R.drawable.resultimage_japangi_orange)
                }
                4 -> {
                    binding.itemModifyTitleIv.setImageResource(R.drawable.binglebingle)
                    binding.itemModifyCircleIv.setImageResource(R.drawable.resultimage_random_orange)
                }
            }
        }

    }

    override fun getItemCount(): Int = resultList.size
}