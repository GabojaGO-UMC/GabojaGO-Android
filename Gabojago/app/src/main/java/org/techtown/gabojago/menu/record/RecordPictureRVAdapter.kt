package org.techtown.gabojago.menu.record

import org.techtown.gabojago.databinding.ItempictureBinding

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.techtown.gabojago.databinding.ItemRecordWeekBinding
import java.text.SimpleDateFormat
import java.util.*

class RecordPictureRVAdapter: RecyclerView.Adapter<RecordPictureRVAdapter.ViewHolder>(){

    //뷰홀더 생성->호출되는 함수->아이템 뷰 객체를 만들어서 뷰홀더에 던져줌
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItempictureBinding = ItempictureBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    //뷰홀더에 데이터를 바인딩해줘야 할 때마다 호출되는 함수 => 엄청나게 많이 호출
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    //뷰홀더
    inner class ViewHolder(val binding: ItempictureBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {

        }

    }

    override fun getItemCount(): Int {
        return 7
    }

}

