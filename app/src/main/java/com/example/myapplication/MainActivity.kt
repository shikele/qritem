package com.example.myapplication

import android.content.ClipData
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.budiyev.android.codescanner.*
import com.example.myapplication.adaptors.RecycleViewAdaptor
import com.example.myapplication.dataClass.ItemClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson


private const val CAMERA_REQUEST_CODE = 101


class MainActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner
    private lateinit var codeScannerView: CodeScannerView
    private lateinit var textBoxView: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var signOutButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var recycleViewAdapter: RecycleViewAdaptor

    private val recycleViewArr: ArrayList<ItemClass> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpPermissions()
        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()



        codeScannerView = findViewById(R.id.scanner_view)
        textBoxView = findViewById(R.id.show_text_view)
        codeScanner = CodeScanner(this, codeScannerView)
        signOutButton = findViewById(R.id.sign_out_button)

        recyclerView = findViewById(R.id.recycle_view_db_data)
        recycleViewAdapter = RecycleViewAdaptor(recycleViewArr)


        signOutButton.setOnClickListener(){
            auth.signOut()
            startActivity(Intent(this, SignInActivity::class.java))
        }

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        checkCurrentUser()
        codeScanner()

    }



    private fun codeScanner(){


        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false



            decodeCallback = DecodeCallback {


                Log.d("debug", "callback")
                runOnUiThread {
                    textBoxView.text = it.text
                }
                loadDbData(it.text)
                Thread.sleep(2000);



            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("main", "Camera initialization error: ${it.message}")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkCurrentUser()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setUpPermissions(){
        val permission = ContextCompat.checkSelfPermission(
            this, android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest (){
        ActivityCompat.requestPermissions(this, arrayOf(
            android.Manifest.permission.CAMERA),CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "You need the camera Permission"
                        , Toast.LENGTH_SHORT).show()
                }else {
                    //success
                }
            }
        }
    }

    private fun checkCurrentUser() {
        val currentUser = auth.currentUser
        if(currentUser == null){
            startActivity(Intent(this,SignInActivity::class.java))
        }
    }

    private fun loadDbData(decode:String) {
        recycleViewArr.clear()
        db.collection(decode)
            .get()
            .addOnSuccessListener { result->
                for (document in result) {
                    recycleViewArr.add(document.toObject(ItemClass::class.java))
                }
                Log.d("debug", "?$recycleViewArr")
                recycleViewAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w("debug", "Error getting documents.", exception)
            }
    }
}