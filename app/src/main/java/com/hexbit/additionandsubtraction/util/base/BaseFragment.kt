package com.hexbit.additionandsubtraction.util.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

/**
 * BaseFragment for all fragments in app. It contains logic for easier inflating view from file.
 *
 * Klasa bazowa dla fragmentów z ułatwieniem do inflate'owania widoku z pliku.
 */
abstract class BaseFragment : Fragment() {

    @get:LayoutRes
    abstract val layout: Int

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layout, container, false)
    }

}