package com.example.woholi.ui.diary

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.woholi.adapter.AddPhotoAdapter
import com.example.woholi.databinding.FragmentWriteDiaryBinding
import com.example.woholi.db.DiaryViewModel
import com.example.woholi.model.Diary
import com.example.woholi.ui.MainActivity
import java.text.SimpleDateFormat


class WriteDiaryFragment : Fragment() {

    private lateinit var binding: FragmentWriteDiaryBinding

    val diaryVM by viewModels<DiaryViewModel>({ requireActivity() })
    val adapter: AddPhotoAdapter = AddPhotoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWriteDiaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val curDay = arguments?.getString("curDay")
        binding.txSelecteddate.text = "${curDay!!.substring(0 until 4)}.${curDay!!.substring(4 until 6)}.${curDay!!.substring(
            6 until 8
        )}"

        adapter.setItemClickListener(object : AddPhotoAdapter.ItemClickListener {
            override fun onClick(v: View, p: Int) {
                if (p == 0) {
                    requirePermissions(
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                        PERM_CAMEARA
                    )
                }
            }
        })

        binding.recyclerViewPhoto.adapter = adapter
        binding.recyclerViewPhoto.layoutManager = LinearLayoutManager(requireContext()).also { it.orientation = LinearLayoutManager.HORIZONTAL }

        binding.btnBack.setOnClickListener {
            (activity as MainActivity).setFlag(7)
        }

        binding.btnConfirm.setOnClickListener {
            val title = binding.edtTitle.text.toString()
            val contents = binding.edtContents.text.toString()
            if (title == "" || contents == "") {
                Toast.makeText(context, "입력해주세요.", Toast.LENGTH_SHORT)
            } else {
                diaryVM.writeDiary(
                    Diary(
                        curDay,
                        title,
                        contents,
                        arrayListOf("https://i.ytimg.com/vi/IT5Uq2K05C0/default.jpg")
                    )
                )
                binding.btnBack.callOnClick()
            }
        }

    }

    val PERM_STORAGE = 99
    val PERM_CAMEARA = 100
    val REQ_CAMERA = 101
    var realUri : Uri? = null

    fun setViews(){
        requirePermissions(arrayOf(Manifest.permission.CAMERA), PERM_CAMEARA)
    }

    fun openCamera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        createImageUri(newFileName(), "image/jpg")?.let { uri ->
            realUri = uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, realUri)
            startActivityForResult(intent, REQ_CAMERA)
        }
    }

    fun createImageUri(filename: String, mimeType: String): Uri?{
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)
        return activity?.contentResolver?.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )
    }

    fun newFileName(): String{
        val sdf = SimpleDateFormat("yyyymmdd_hhmmss")
        val filename = sdf.format(System.currentTimeMillis())
        return "$filename.jpg"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK){
            when(requestCode){
                REQ_CAMERA -> {
                    realUri?.let { url ->
                        adapter.dataList.add(realUri.toString())
                    }
                    /*
                    if ( data?.extras?.get("data") != null){
                        val url = data?.extras?.get("data") as URL
                        Log.d("WriteDiary", url.toString())
                        adapter.dataList.add(url.toString())
                    }
                     */
                }
            }
        }
    }

    fun permissionGranted(requestCode: Int){
        when(requestCode){
            PERM_CAMEARA -> openCamera()
        }
    }

    fun permissionDenied(requestCode: Int){
        when(requestCode){
            PERM_STORAGE -> {
                Toast.makeText(context, "외부 저장소 권한을 승인해야 합니다.", Toast.LENGTH_SHORT)
            }
            PERM_CAMEARA -> {
                Toast.makeText(context, "카메라 권한을 승인해야 합니다.", Toast.LENGTH_SHORT)
            }
        }
    }



    fun requirePermissions(permissions: Array<String>, requestCode: Int){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            permissionGranted(requestCode)
        }
        else {
            val isAllPermissionsGranted = permissions.all {
                ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
            }
            if (isAllPermissionsGranted) {
                permissionGranted(requestCode)
            }
            else{
                ActivityCompat.requestPermissions(requireActivity(), permissions, requestCode)
            }
        }
    }
}