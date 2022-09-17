package ru.nikitazar.santehnika_online.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import ru.nikitazar.santehnika_online.R
import ru.nikitazar.santehnika_online.databinding.FragmentCurrentPointBinding
import ru.nikitazar.santehnika_online.viewModel.PointViewModel
import java.lang.String.format

@AndroidEntryPoint
class CurrentPointFragment : Fragment() {

    private val viewModel: PointViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    private val format = "%.3f"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentCurrentPointBinding.inflate(inflater, container, false)

        with(binding) {
            viewModel.data.observe(viewLifecycleOwner) {
                latLayout.isVisible = it != null
                lonLayout.isVisible = it != null
                currentPoint.isVisible = it == null

                it?.let { point ->
                    lat.apply {
                        setText(format(format, point.latitude))
                        isVisible = true
                    }
                    lon.apply {
                        setText(format(format, point.longitude))
                        isVisible = true
                    }
                } ?: run {
                    currentPoint.text = getString(R.string.point_not_set)
                }
            }

            btOnMap.setOnClickListener {
                findNavController().navigate(R.id.action_currentPointFragment_to_mapFragment)
            }

            btRemove.setOnClickListener {
                viewModel.remove()
            }
        }

        return binding.root
    }
}