package ru.nikitazar.santehnika_online.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PlacemarkMapObject
import dagger.hilt.android.AndroidEntryPoint
import ru.nikitazar.santehnika_online.R
import ru.nikitazar.santehnika_online.databinding.FragmentMapBinding
import ru.nikitazar.santehnika_online.utils.*
import ru.nikitazar.santehnika_online.viewModel.PointViewModel
import java.lang.String.format

@AndroidEntryPoint
class MapFragment : Fragment() {

    private var lastMapObject: PlacemarkMapObject? = null

    private val viewModel: PointViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    private val format = "%.3f"
    private lateinit var mapKit: MapKit
    private var point: Point? = null
    private lateinit var mapObjects: MapObjectCollection
    private val inputListener = object : InputListener {
        override fun onMapTap(map: Map, point: Point) {
            //nothing to do
        }

        override fun onMapLongTap(map: Map, point: Point) {
            lastMapObject?.let {
                mapObjects.remove(it)
            }
            lastMapObject = drawPlacemark(point, mapObjects, Color.GREEN)
            viewModel.edit(point)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentMapBinding.inflate(inflater, container, false)

        val mapView = binding.mapview.apply {
            attachToLifecycle(viewLifecycleOwner)
            map.addInputListener(inputListener)
            mapObjects = map.mapObjects.addCollection()
        }

        viewModel.data.observe(viewLifecycleOwner) {
            it?.let { point ->
                drawPlacemark(point, mapObjects, Color.BLUE)
                moveToLocation(mapView, point)
            }
        }

        binding.fabDone.setOnClickListener {
            if (lastMapObject != null) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(context?.getString(R.string.confirm_point))
                    .setMessage(
                        "${context?.getString(R.string.latitude)}: ${format(format, viewModel.edited.value.latitude)}\n" +
                                "${context?.getString(R.string.longitude)}: ${format(format, viewModel.edited.value.longitude)}"
                    )
                    .setNegativeButton(context?.getString(R.string.cancel)) { _, _ ->
                        lastMapObject?.let {
                            mapObjects.remove(it)
                        }
                    }
                    .setPositiveButton(context?.getString(R.string.save)) { _, _ ->
                        viewModel.save()
                        findNavController().navigateUp()
                    }.show()
            }
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(context)
        mapKit = MapKitFactory.getInstance()
    }

    override fun onStart() {
        mapKit.onStart()
        super.onStart()
    }

    override fun onStop() {
        mapKit.onStop()
        super.onStop()
    }
}