package com.example.photoexam_1.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.photoexam_1.data.model.PhotoEssay
import com.example.photoexam_1.data.remote.response.DataItem
import com.example.photoexam_1.databinding.MainItemBinding
import com.example.photoexam_1.ui.detail.DetailActivity
import com.example.photoexam_1.utils.formattedDate


class MainAdapter: ListAdapter<DataItem, MainAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(private val view: MainItemBinding): RecyclerView.ViewHolder(view.root) {
        fun bind(itemPhoto: DataItem) {
            view.txtStudentName.text = itemPhoto.studentName
            view.txtDescFile.text = itemPhoto.description
            view.txtFileDate.text = itemPhoto.createdAt?.formattedDate()

            Glide.with(itemView.context)
                .load(itemPhoto.storageUrl)
                .into(view.ivPhotoEssay)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = MainItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val listPhoto = getItem(position)
        holder.bind(listPhoto)

        holder.itemView.setOnClickListener {
            val date = listPhoto?.createdAt ?: ""
            val answerKey = listPhoto?.answerKey ?: ""
            val score = listPhoto?.score
            val fileName = listPhoto?.fileName ?: ""
            val studentName = listPhoto?.studentName ?: ""
            val description = listPhoto?.description ?: ""
            val studentAnswer = listPhoto?.studentAnswer ?: ""
            val storageUrl = listPhoto?.storageUrl ?: ""
            val fileId = listPhoto?.fileId ?: ""

            val passingData = PhotoEssay(date, answerKey, score!!, fileName, studentName, description, studentAnswer, storageUrl, fileId)
            val moveToDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            moveToDetail.putExtra(DetailActivity.EXTRA_DETAIL_STORY, passingData)
            holder.itemView.context.startActivity(moveToDetail)
        }
    }

    companion object {
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}