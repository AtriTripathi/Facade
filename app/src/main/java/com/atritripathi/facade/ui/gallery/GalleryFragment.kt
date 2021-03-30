package com.atritripathi.facade.ui.gallery

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.LoadState.*
import com.atritripathi.facade.R
import com.atritripathi.facade.data.UnsplashPhoto
import com.atritripathi.facade.databinding.FragmentGalleryBinding
import com.atritripathi.facade.ui.gallery.UnsplashPhotoAdapter.OnItemClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery), OnItemClickListener {

    private val viewModel by viewModels<GalleryViewModel>()

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentGalleryBinding.bind(view)

        val photoAdapter = UnsplashPhotoAdapter(this)

        with(binding) {
            rvImageGallery.apply {
                adapter = photoAdapter.withLoadStateHeaderAndFooter(
                    header = UnsplashPhotoLoadStateAdapter { photoAdapter.retry() },
                    footer = UnsplashPhotoLoadStateAdapter { photoAdapter.retry() }
                )
                itemAnimator = null
                setHasFixedSize(true)
            }
            btnRetry.setOnClickListener { photoAdapter.retry() }
        }

        viewModel.photos.observe(viewLifecycleOwner) {
            photoAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        photoAdapter.addLoadStateListener { loadState ->
            with(binding) {
                progressBar.isVisible = loadState.source.refresh is Loading
                rvImageGallery.isVisible = loadState.source.refresh is NotLoading
                btnRetry.isVisible = loadState.source.refresh is Error
                tvError.isVisible = loadState.source.refresh is Error

                // When there are no results
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    photoAdapter.itemCount < 1
                ) {
                    rvImageGallery.isVisible = false
                    tvEmpty.isVisible = true
                } else {
                    tvEmpty.isVisible = false
                }
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onItemClick(photo: UnsplashPhoto) {
        val action = GalleryFragmentDirections.actionGalleryFragmentToDetailsFragment(photo)
        findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_gallery, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    binding.rvImageGallery.scrollToPosition(0)
                    viewModel.searchPhotos(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?) = true
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}