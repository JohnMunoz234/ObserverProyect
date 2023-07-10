package com.gse.test.observerproyect.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.gse.test.observerproyect.databinding.FragmentSecondBinding
import com.gse.test.observerproyect.model.ECDCBiometric
import com.gse.test.observerproyect.util.Util
import com.gse.test.observerproyect.viewModel.SecondViewModel
import java.util.Timer
import java.util.TimerTask

class SecondFragment : Fragment() {

    private val TAG = SecondFragment::class.java.canonicalName
    private var binding: FragmentSecondBinding? = null

    var countPendingRead: MutableLiveData<Int> = MutableLiveData()
    var errorMain: MutableLiveData<String> = MutableLiveData()
    private var timer = Timer()

    companion object {
        fun newInstance() = SecondFragment()
    }

    private lateinit var viewModel: SecondViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SecondViewModel::class.java)
        if (binding != null) {
            listeners()
        }
    }


    fun listeners() {
        binding!!.buttonActiveRandom.setOnClickListener { view -> setStatus(countPendingRead) }
        binding!!.buttonActiveError.setOnClickListener { view -> randomErrorMessage(errorMain) }
    }


    fun callInitFragment(
        status: MutableLiveData<Int>?,
        error: MutableLiveData<String>?,
        mBiometric: ECDCBiometric?
    ) {
        if (status != null && error != null) {
            countPendingRead = status
            errorMain = error
        }
    }

    //----------------------------------------------------------------------------------------------
    fun setStatus() {
        val number: Int = Util.getRandomNumberInRange(1, 100)
        Log.e(TAG, "setStatus: $number")
        countPendingRead.postValue(number)
    }

    fun setStatus(status: MutableLiveData<Int>) {
        val number: Int = Util.getRandomNumberInRange(1, 100)
        Log.e(TAG, "setStatus: $number")
        status.postValue(number)
    }

    fun stopTimer() {
        Log.d(TAG, "stopTimer: ")
        if (timer != null) timer.cancel()
    }


    fun setTimer(statusMain: MutableLiveData<Int>, time: Int) {
        timer = Timer() // Solved: I needed to create a new Timer object.
        //set timer
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                Log.d(TAG, "run: ")
                setStatus(statusMain)
            }
        }, 0, time.toLong())
    }

    fun getStatusObservable(statusMain: MutableLiveData<Int>) {
        Log.d(TAG, "getStatusObservable: ")
        setTimer(statusMain, 2000)
    }


    fun randomErrorMessage(error: MutableLiveData<String>?) {
        val number: Int = Util.getRandomNumberInRange(0, 4)
        Log.d(TAG, "randomErrorMessage: " + getListError()[number])
        error?.setValue(getListError()[number])
    }

    private fun getListError(): List<String> {
        return java.util.List.of(
            "Exception in thread 'main' java.lang.NullPointerException",
            "No internet connection",
            "Unknown error",
            "Internal problem"
        )
    }


}