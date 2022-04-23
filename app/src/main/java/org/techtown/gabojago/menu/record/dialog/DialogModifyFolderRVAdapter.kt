package org.techtown.gabojago.menu.record.dialog

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.techtown.gabojago.R
import org.techtown.gabojago.databinding.ItemFolderresultModifyversionBinding
import org.techtown.gabojago.menu.record.recordRetrofit.InFolderListResult
val minus = ArrayList<Boolean>()

class DialogModifyFolderRVAdapter(private val resultList: ArrayList<InFolderListResult>): RecyclerView.Adapter<DialogModifyFolderRVAdapter.ViewHolder>() {



    //뷰홀더 생성->호출되는 함수->아이템 뷰 객체를 만들어서 뷰홀더에 던져줌
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemFolderresultModifyversionBinding =
            ItemFolderresultModifyversionBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        minus.clear()
        for (i in 0 until 31) {
            minus.add(false)
        }

        return ViewHolder(binding)
    }

    //뷰홀더에 데이터를 바인딩해줘야 할 때마다 호출되는 함수 => 엄청나게 많이 호출
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(resultList[position])

        holder.itemView.setOnClickListener {
            if(!minus[position]) {
                minus[position]= true
                holder.binding.itemFoldermodifyLayout.setBackgroundColor(Color.parseColor("#ffffff"))
            }else{
                minus[position]= false
                holder.binding.itemFoldermodifyLayout.setBackgroundColor(Color.parseColor("#FFF5F3"))
            }
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