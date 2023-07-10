package com.gse.test.observerproyect.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.gse.test.observerproyect.databinding.FragmentFirstBinding
import com.gse.test.observerproyect.model.ECDCBiometric
import com.gse.test.observerproyect.util.Util
import com.gse.test.observerproyect.viewModel.FirstViewModel
import java.util.Timer
import java.util.TimerTask

class FirstFragment : Fragment() {

    private var binding: FragmentFirstBinding? = null

    private val TAG = FirstFragment::class.java.canonicalName
    var countPendingRead: MutableLiveData<Int> = MutableLiveData()
    var errorMessage: MutableLiveData<String> = MutableLiveData()
    private var timer = Timer()


    companion object {
        fun newInstance() = FirstFragment()
    }

    private lateinit var viewModel: FirstViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FirstViewModel::class.java)
        // TODO: Use the ViewModel
        if (binding != null) {
            Log.e(TAG, "ESTOY EN EL ON VIEW CREATED")
            listener()
        }
    }

    fun listener() {

        // Event click buttonActiveRandom
        binding!!.buttonActiveRandom.setOnClickListener { view -> setStatus(countPendingRead) }

        // Event click buttonActiveError
        binding!!.buttonActiveError.setOnClickListener { view -> randomErrorMessage(errorMessage) }

        // Even click button Activity
        binding!!.buttonActivity.setOnClickListener { view -> getIntentActivity() }
    }

    fun callInitFragment(
        status: MutableLiveData<Int>?,
        error: MutableLiveData<String>?,
        mBiometric: ECDCBiometric?
    ) {
        Log.e(TAG, "Estoy en callInitFragment")
        if (status != null && error != null){
            countPendingRead = status
            errorMessage = error
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
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

    private fun getIntentActivity() {
        val intent = Intent(this.activity, SecondActivity::class.java)
        startActivity(intent)
    }

}