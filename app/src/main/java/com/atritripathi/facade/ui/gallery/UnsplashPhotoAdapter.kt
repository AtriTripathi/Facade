package com.atritripathi.facade.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.atritripathi.facade.R
import com.atritripathi.facade.data.UnsplashPhoto
import com.atritripathi.facade.databinding.ItemUnsplashPhotoBinding
import com.atritripathi.facade.ui.gallery.UnsplashPhotoAdapter.PhotoViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class UnsplashPhotoAdapter : PagingDataAdapter<UnsplashPhoto, PhotoViewHolder>(PHOTO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemUnsplashPhotoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val currentItem = getItem(position)
        currentItem?.let {
            holder.bind(currentItem)
        }
    }

    class PhotoViewHolder(private val binding: ItemUnsplashPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: UnsplashPhoto) {
            with(binding) {
                Glide.with(itemView)
                    .load(photo.urls.regular)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(ivPhoto)
                tvUsername.text = photo.user.username
            }
        }
    }

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<UnsplashPhoto>() {
            override fun areItemsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto) =
                oldItem == newItem
        }
    }
}