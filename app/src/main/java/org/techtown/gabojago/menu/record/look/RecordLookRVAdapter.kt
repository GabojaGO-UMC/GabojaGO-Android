package org.techtown.gabojago.menu.record.look

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.techtown.gabojago.databinding.ItemLookPickBinding
import org.techtown.gabojago.menu.record.recordRetrofit.FolderRecordResultList
import java.util.*

class RecordLookRVAdapter(private val pickedList: ArrayList<FolderRecordResultList>): RecyclerView.Adapter<RecordLookRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemLookPickBinding =
            ItemLookPickBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pickedList[position])
    }

    inner class ViewHolder(val binding: ItemLookPickBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pickedItem: FolderRecordResultList) {
            binding.resultPickTimeTv.text = pickedItem.creatAt
            binding.resultPickContentsTv.text = " \""+pickedItem.randomResultContent+"\""
            when (pickedItem.randomResultType) {
                0 -> {
                    binding.resultPickTypeTv.text = ""
                }1 -> {
                    binding.resultPickTypeTv.text = "돌려돌려 돌림판"
                }2 -> {
                    binding.resultPickTypeTv.text = "N시 방향"
                }3 -> {
                    binding.resultPickTypeTv.text = "컬러박스 뽑기"
                }4 -> {
                    binding.resultPickTypeTv.text = "빙글빙글 숫자 뽑기"
                }
            }
        }
    }

    override fun getItemCount(): Int = pickedList.size
}