package com.gse.test.observerproyect


import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import co.com.gse.poc_observable.util.ServiceInitialize
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.gse.test.observerproyect.databinding.ActivityMainBinding
import com.gse.test.observerproyect.model.ECDCBiometric
import com.gse.test.observerproyect.model.UserInfo
import com.gse.test.observerproyect.view.PrincipalFragment




class MainActivity : AppCompatActivity() {

    private var appBarConfiguration: AppBarConfiguration? = null
    private var binding: ActivityMainBinding? = null
    private var mViewModel: MainActivityViewModel? = null
    private val TAG = MainActivity::class.java.simpleName
    private var mContext: Activity? = null
    private var navController: NavController? = null
    private val BIOMETRIC_TYPE: ECDCBiometric = ECDCBiometric.FACE_MATCH

    private val anchor = R.id.bottom_main_bar

    var statusMain: MutableLiveData<Int>? = MutableLiveData()

    fun getStatusMain(): LiveData<Int>? {
        if (statusMain == null) {
            statusMain = MutableLiveData()
        }
        return statusMain
    }

    private var errorMain: MutableLiveData<String>? = MutableLiveData()

    fun getErrorMain(): LiveData<String>? {
        if (errorMain == null) {
            errorMain = MutableLiveData()
        }
        return errorMain
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        //set viewModel
        mViewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        mContext = this

        subscribeEvent()
        back()
        // TODO IMPLEMENT IDEMIA
        ServiceInitialize.initServices(
            activity = this,
            fragment = getForegroundFragment()!!,
            biometric = BIOMETRIC_TYPE,
            statusFragment =  statusMain,
            errorFragment =  errorMain!!,
            userInfo = generateUserInfo()
        )
    }


    //----------------------------------------------------------------------------------------------
    /**
     * Dummy method to add a user
     * @return returns a UserInfoDummy object
     */
    private fun generateUserInfo(): UserInfo? {
        return UserInfo(
            documentType = "C.C",
            documentNumber =  "202020202",
            firstName = "MARIA",
            secondName = "DANIELA",
            surname = "MARTÍNEZ",
            secondSurname = "GARCÍA",
            email = "email@gmail.com",
            address = "address",
            department = "169081",
            location = "169099524",
            phone = "254789154",
            expeditionDate = "2021-03-04T14:00:00.000Z"
        )
    }


    /**
     * Method that configures the bottomNavigation navigation with the navigation graphic
     */
    private fun setupNavigation() {
        try {
            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_main_bar)
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment?
            setupWithNavController(
                bottomNavigationView,
                navHostFragment!!.navController
            )

            // TODO IMPLEMENT IDEMIA
            navHostFragment.childFragmentManager.addFragmentOnAttachListener { fragmentManager, fragment ->
                ServiceInitialize.initServices(
                    activity = fragment.activity,
                    fragment = fragment,
                    biometric = BIOMETRIC_TYPE,
                   statusFragment =  statusMain,
                    errorFragment =  errorMain,
                    userInfo = generateUserInfo()
                )
            }
        } catch (e: java.lang.Exception) {
            e.stackTrace
        }
    }

    //Navigation Android X
    fun getForegroundFragment(): Fragment? {
        // TODO Remplazar por metodo que obtenga la instancia del fragmento
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)
        return navHostFragment?.childFragmentManager?.fragments?.get(0)
    }

    private fun subscribeEvent() {
        getStatusMain()?.observe(this) { integer: Int? -> this.getStatusFragmentChild(integer) }
        getErrorMain()?.observe(this) { error: String? -> this.getErrorFragment(error) }
    }

    private fun getStatusFragmentChild(integer: Int?) {
        Log.d(TAG, "getStatusFragmentChild: $integer")
        Snackbar.make(
            binding!!.root, "Status: $integer",
            BaseTransientBottomBar.LENGTH_LONG
        )
            .setAnchorView(anchor)
            .show()
    }

    private fun getErrorFragment(error: String?) {
        if (error != null) {
            Log.e(TAG, "Error: $error")
            Snackbar.make(
                binding!!.root, "Error: $error",
                BaseTransientBottomBar.LENGTH_SHORT
            )
                .setAnchorView(anchor)
                .show()
        } else {
            Toast.makeText(mContext, "undetermined error", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(this, R.id.nav_host_fragment_content_main)
        return (navigateUp(navController, appBarConfiguration!!)
                || super.onSupportNavigateUp())
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "ENTRE EN EN EL ON RESUME...")
        Log.d(TAG, "FRAGMENTO ACTIVO: " + getForegroundFragment())
        setupNavigation()
    }

    private fun back() {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    Log.e(TAG, "Se dio al boton de atras")
                }
            }
        this.onBackPressedDispatcher.addCallback(this, callback)
    }
}