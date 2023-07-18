package com.example.appnextexercise.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

// Defines a type alias for a function that takes a LayoutInflater, ViewGroup, and Boolean,
// and returns an instance of a generic type T
typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

// An abstract base class for Fragments that use Binding
abstract class BaseFragment<VB: ViewBinding>(
    private val inflate: Inflate<VB>) : Fragment() {

    // A nullable instance of the ViewBinding type
    private var _binding: VB? = null
    // A non-null getter for the ViewBinding instance
    val binding get() = _binding!!

    // Inflates the ViewBinding and sets it as the root view of the Fragment
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    // Sets the ViewBinding instance to null when the Fragment's view is destroyed
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}