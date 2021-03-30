package com.atritripathi.facade.ui.gallery

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.atritripathi.facade.R
import com.atritripathi.facade.databinding.FragmentGalleryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery) {

    private val viewModel by viewModels<GalleryViewModel>()

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentGalleryBinding.bind(view)

        val photoAdapter = UnsplashPhotoAdapter()

        with(binding.rvImageGallery) {
            adapter = photoAdapter
            setHasFixedSize(true)
        }

        viewModel.photos.observe(viewLifecycleOwner) {
            photoAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}