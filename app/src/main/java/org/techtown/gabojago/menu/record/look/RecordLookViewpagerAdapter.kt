package org.techtown.gabojago.menu.record.look

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_look_vp.view.*
import kotlinx.android.synthetic.main.fragment_record_look.view.*
import org.techtown.gabojago.R
import java.util.*

class RecordLookViewpagerAdapter(ImageArr: ArrayList<String>)
    : RecyclerView.Adapter<RecordLookViewpagerAdapter.LookViewHolder>() {
    var item = ImageArr
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ) = LookViewHolder(parent)

    override fun onBindViewHolder(
        holder: RecordLookViewpagerAdapter.LookViewHolder,
        position: Int,
    ) {
        Glide.with(holder.lookImg.context).load(item[position]).into(holder.lookImg)
    }

    override fun getItemCount(): Int = item.size

    inner class LookViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
        (LayoutInflater.from(parent.context).inflate(R.layout.fragment_look_vp, parent, false)){
            val lookImg = itemView.banner_image_iv!!
        }
}