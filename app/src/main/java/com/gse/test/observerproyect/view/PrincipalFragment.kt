package com.gse.test.observerproyect.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gse.test.observerproyect.R
import com.gse.test.observerproyect.viewModel.PrincipalViewModel

class PrincipalFragment : Fragment() {

    companion object {
        fun newInstance() = PrincipalFragment()
    }

    private lateinit var viewModel: PrincipalViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_principal, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PrincipalViewModel::class.java)
        // TODO: Use the ViewModel
    }

}