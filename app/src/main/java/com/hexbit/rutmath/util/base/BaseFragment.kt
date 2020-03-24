package com.hexbit.rutmath.util.base

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.hexbit.rutmath.R

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

    protected fun showSimpleDialog(title: String? = null, message: String? = null) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context!!)
        title?.let {
            builder.setTitle(it)
        }
        message?.let {
            builder.setMessage(it)
        }
        builder.setPositiveButton(
            getString(R.string.ok)
        ) { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
}