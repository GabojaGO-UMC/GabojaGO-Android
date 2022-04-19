package org.techtown.gabojago.menu.record.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.techtown.gabojago.R
import org.techtown.gabojago.databinding.ItemFolderresultModifyversionBinding
import org.techtown.gabojago.menu.record.recordRetrofit.InFolderListResult

class DialogModifyMinusRVAdapter(private val resultList: ArrayList<InFolderListResult>): RecyclerView.Adapter<DialogModifyMinusRVAdapter.ViewHolder>() {

    interface MyItemClickListener {
        fun onItemClick(position:Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }


    //뷰홀더 생성->호출되는 함수->아이템 뷰 객체를 만들어서 뷰홀더에 던져줌
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemFolderresultModifyversionBinding =
            ItemFolderresultModifyversionBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    //뷰홀더에 데이터를 바인딩해줘야 할 때마다 호출되는 함수 => 엄청나게 많이 호출
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (resultList[position] != null) {
            holder.bind(resultList[position])
        }
        holder.binding.itemFoldermodifyPecilIv.setOnClickListener {
            mItemClickListener.onItemClick(position)
        }
    }

    //뷰홀더
    inner class ViewHolder(val binding: ItemFolderresultModifyversionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result:InFolderListResult) {
            binding.itemFoldermodifyResultTv.text = result.resultContent
            when (result.resultType) {
                1 -> {
                    binding.itemFoldermodifyTitleIv.setImageResource(R.drawable.dolimpan)
                    binding.itemFoldermodifyCircleIv.setImageResource(R.drawable.resultimage_dolimpan_orange)
                }2 -> {
                    binding.itemFoldermodifyTitleIv.setImageResource(R.drawable.nsibanghiang)
                    binding.itemFoldermodifyCircleIv.setImageResource(R.drawable.resultimage_nsibang_orange)
                }3 -> {
                    binding.itemFoldermodifyTitleIv.setImageResource(R.drawable.colorbox)
                    binding.itemFoldermodifyCircleIv.setImageResource(R.drawable.resultimage_japangi_orange)
                }4 -> {
                    binding.itemFoldermodifyTitleIv.setImageResource(R.drawable.binglebingle)
                    binding.itemFoldermodifyCircleIv.setImageResource(R.drawable.resultimage_random_orange) }
            }
        }
    }
    override fun getItemCount(): Int =resultList.size
}