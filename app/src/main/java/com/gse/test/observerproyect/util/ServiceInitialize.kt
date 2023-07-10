package co.com.gse.poc_observable.util

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.gse.test.observerproyect.model.UserInfo
import com.gse.test.observerproyect.model.ECDCBiometric
import com.gse.test.observerproyect.model.callback.CustomCallback
import com.gse.test.observerproyect.view.FirstFragment
import com.gse.test.observerproyect.view.SecondFragment

class ServiceInitialize(callback: CustomCallback, biometric: ECDCBiometric) {
    /**
     * Service initialization constructor method
     * @param callback (CustomCallBack)
     * @param biometric (ECDCBiometric)
     */
    init {
        initSdk(callback, biometric)
    }

    companion object {
        private val TAG = ServiceInitialize::class.java.canonicalName
        /**
         * Method that initializes the SDK
         * @param callback (CustomCallBack)
         * @param biometric (ECDCBiometric)
         */
        fun initSdk(callback: CustomCallback, biometric: ECDCBiometric) {
            if (biometric === ECDCBiometric.NONE && biometric === ECDCBiometric.PASSWORD) {
                callback.onError("inactive")
            } else {
                callback.onSuccess(true)
            }
        }

        /**
         * Method that starts the service, performing validation before calling the fragment.
         * @param activity (Activity)
         * @param fragment (Fragment)
         * @param biometric (ECDCBiometric)
         * @param statusFragment (SingleLiveEvent)
         * @param errorFragment (SingleLiveEvent)
         * @param userInfo (UserInfoDummy)
         */
        fun initServices(
            activity: Activity?,
            fragment: Fragment,
            biometric: ECDCBiometric?,
            statusFragment: MutableLiveData<Int>?,
            errorFragment: MutableLiveData<String>?,
            userInfo: UserInfo?
        ) {
            if (statusFragment != null) {
                getPendingRead(activity, userInfo, object : CustomCallback {
                    override fun onSuccess(`object`: Any?) {
                        listenerFinishNotificationAndPendingObservable(
                            fragment,
                            statusFragment,
                            errorFragment,
                            biometric
                        )
                    }

                    override fun onError(error: String?) {
                        TODO("Not yet implemented")
                    }

                }, biometric!!)
            } else {
                Toast.makeText(activity, "This observable is null", Toast.LENGTH_SHORT).show()
            }
        }

        fun getPendingRead(
            context: Activity?,
            userInfo: UserInfo?,
            callback: CustomCallback,
            ecdcBiometric: ECDCBiometric
        ) {
            validateUser(context, userInfo, callback)
            initSdk(object : CustomCallback {
                override fun onSuccess(`object`: Any?) {
                    Log.e(TAG, "SDK ACTIVE...")
                }

                override fun onError(error: String?) {
                    Log.e(TAG, "FAIL SDK...")
                }
            }, ecdcBiometric)
        }

        /**
         * Method that obtains the instance of the fragment with which the fragment method is called to start observables.
         * @param fragment (Fragment)
         * @param countPendingRead (SingleLiveEvent)
         * @param errorMessage (SingleLiveEvent)
         * @param biometric (ECDCBiometric)
         */
        fun listenerFinishNotificationAndPendingObservable(
            fragment: Fragment,
            countPendingRead: MutableLiveData<Int>?,
            errorMessage: MutableLiveData<String>?,
            biometric: ECDCBiometric?
        ) {
            if (fragment is FirstFragment) {
                val notificationInboxFragment: FirstFragment = fragment as FirstFragment
                notificationInboxFragment.callInitFragment(
                    countPendingRead,
                    errorMessage,
                    biometric
                )
            } else if (fragment is SecondFragment) {
                val notificationDocumentFragment: SecondFragment = fragment as SecondFragment
                notificationDocumentFragment.callInitFragment(
                    countPendingRead,
                    errorMessage,
                    biometric
                )
            }
        }

        /**
         * Method used to validate user information
         * @param context  (Activity)
         * @param userInfo (UserInfo)
         * @param callback (CallBack)
         */
        fun validateUser(context: Activity?, userInfo: UserInfo?, callback: CustomCallback) {
            val isValidateUserInfo = validateInfo(userInfo)
            if (context != null) {
                if (isValidateUserInfo) {
                    Log.e(TAG, "user validated successfully")
                    callback.onSuccess(userInfo)
                } else {
                    callback.onError("User incomplete")
                }
            }
        }

        /**
         * Method for validating each user field
         * @param userInfo (UserInfoDummy)
         * @return returns true or false as indicated by the validation
         */
        private fun validateInfo(userInfo: UserInfo?): Boolean {
            return userInfo != null
                    && userInfo.documentType != null
                    && userInfo.documentNumber != null
                    && userInfo.firstName != null
                    && userInfo.surname != null
                    && userInfo.email != null
                    && userInfo.address != null
                    && userInfo.department != null
                    && userInfo.location != null
                    && userInfo.phone != null
                    && userInfo.expeditionDate != null
        }
    }
}