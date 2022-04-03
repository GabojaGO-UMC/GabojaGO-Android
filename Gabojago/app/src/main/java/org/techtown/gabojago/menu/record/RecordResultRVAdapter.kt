package org.techtown.gabojago.menu.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.techtown.gabojago.R
import org.techtown.gabojago.databinding.ItemRecordResultBinding
import org.techtown.gabojago.menu.record.recordRetrofit.SingleResultListResult

class RecordResultRVAdapter(private val recordList: ArrayList<SingleResultListResult>): RecyclerView.Adapter<RecordResultRVAdapter.ViewHolder>() {

    //클릭 인터페이스
    interface MyItemClickListener {
        fun onItemClick(recordIdx:Int)
        fun onItemView()
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    //뷰홀더 생성->호출되는 함수->아이템 뷰 객체를 만들어서 뷰홀더에 던져줌
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRecordResultBinding =
            ItemRecordResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    //뷰홀더에 데이터를 바인딩해줘야 할 때마다 호출되는 함수 => 엄청나게 많이 호출
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recordList[position])
        holder.itemView.setOnClickListener{
            mItemClickListener.onItemView()
        }
        holder.binding.itemRecordPecilIv.setOnClickListener {
            mItemClickListener.onItemClick(position)
        }
    }

    //뷰홀더
    inner class ViewHolder(val binding: ItemRecordResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recordList: SingleResultListResult) {
            binding.itemRecordResultTv.text = recordList.randomResultListResult.randomResultContent
            binding.itemRecordClockTv.text = recordList.randomResultListResult.createAt
            if (recordList.hasRecording) {
                binding.itemRecordRectangleIv.setBackgroundResource(R.drawable.rectangle_orange)
                binding.itemRecordPecilIv.setImageResource(R.drawable.memo_pencil_orange)
                when (recordList.randomResultListResult.randomResultType) {
                    1 -> {
                        binding.itemRecordTitleIv.setImageResource(R.drawable.dolimpan)
                        binding.itemRecordCircleIv.setImageResource(R.drawable.resultimage_dolimpan_orange)
                    }2 -> {
                        binding.itemRecordTitleIv.setImageResource(R.drawable.nsibanghiang)
                        binding.itemRecordCircleIv.setImageResource(R.drawable.resultimage_nsibang_orange)
                    }3 -> {
                        binding.itemRecordTitleIv.setImageResource(R.drawable.colorbox)
                        binding.itemRecordCircleIv.setImageResource(R.drawable.resultimage_japangi_orange)
                    }4 -> {
                        binding.itemRecordTitleIv.setImageResource(R.drawable.binglebingle)
                        binding.itemRecordCircleIv.setImageResource(R.drawable.resultimage_random_orange)
                    }
                }

            } else {
                binding.itemRecordRectangleIv.setBackgroundResource(R.drawable.rectangle_gray)
                binding.itemRecordPecilIv.setImageResource(R.drawable.memo_pencil_bluegray)
                when (recordList.randomResultListResult.randomResultType) {
                    1 -> {
                        binding.itemRecordTitleIv.setImageResource(R.drawable.dolimpan)
                        binding.itemRecordCircleIv.setImageResource(R.drawable.resultimage_dolimpan_gray)
                    }2 -> {
                        binding.itemRecordTitleIv.setImageResource(R.drawable.nsibanghiang)
                        binding.itemRecordCircleIv.setImageResource(R.drawable.resultimage_nsibang)
                    }3 -> {
                        binding.itemRecordTitleIv.setImageResource(R.drawable.colorbox)
                        binding.itemRecordCircleIv.setImageResource(R.drawable.resultimage_japangi)
                    }4 -> {
                        binding.itemRecordTitleIv.setImageResource(R.drawable.binglebingle)
                        binding.itemRecordCircleIv.setImageResource(R.drawable.resultimage_random_gray)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int =recordList.size

}