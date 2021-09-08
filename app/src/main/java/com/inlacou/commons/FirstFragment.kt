package com.inlacou.commons

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.inlacou.inkandroidextensions.toast
import com.inlacou.inkbetterandroidviews.extensions.itemClicks
import com.inlacou.commons.databinding.FragmentFirstBinding
import timber.log.Timber

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
	
	private var _binding: FragmentFirstBinding? = null

	val items = listOf("A", "B", "C", "D", "E", "F", "G")

	// This property is only valid between onCreateView and
	// onDestroyView.
	private val binding get() = _binding!!
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		
		_binding = FragmentFirstBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.betterSpinner.setSimpleAdapter(items)
		binding.betterSpinner.itemClicks().subscribe({ activity?.toast("selected ${items[it]}") },{ Timber.e(it) })
		binding.buttonFirst.setOnClickListener {
			findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
		}
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}