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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.woholi.R
import com.example.woholi.adapter.AddPhotoAdapter
import com.example.woholi.databinding.FragmentWriteDiaryBinding
import com.example.woholi.db.DiaryViewModel
import com.example.woholi.model.Diary
import com.example.woholi.ui.MainActivity
import java.net.URL
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
                    setDialog()
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
                        adapter.dataList
                    )
                )
                binding.btnBack.callOnClick()
            }
        }

    }

    val PERM_GALLERY = 99
    val PERM_CAMEARA = 100
    val REQ_CAMERA = 101
    val REQ_GALLERY = 102
    var realUri : Uri? = null


    fun setDialog(){
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_select_photo, null)
        val builder = AlertDialog.Builder(requireContext()).setView(dialogView)
        val dialog = builder.show()

        val cameraBtn = dialogView.findViewById<Button>(R.id.btn_camera)
        val galleryBtn = dialogView.findViewById<Button>(R.id.btn_gallery)

        cameraBtn.setOnClickListener{
            requirePermissions(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ),
                PERM_CAMEARA
            )
            dialog.hide()
        }
        galleryBtn.setOnClickListener{
            requirePermissions(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ),
                PERM_GALLERY
            )
            dialog.hide()
        }
    }

    fun openCamera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        createImageUri(newFileName(), "image/jpg")?.let { uri ->
            realUri = uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, realUri)
            startActivityForResult(intent, REQ_CAMERA)
        }
    }

    fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE

        createImageUri(newFileName(), "image/jpg")?.let { uri ->
            realUri = uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, realUri)
            startActivityForResult(intent, REQ_GALLERY)
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

                        realUri = null
                    }
                    /*
                    if ( data?.extras?.get("data") != null) {
                        val url = data?.extras?.get("data") as URL
                        adapter.dataList.add(url.toString())
                    }
                     */
                }
                REQ_GALLERY -> {
                    data?.data?.let{ uri->
                        adapter.dataList.add(uri.toString())
                    }
                }
            }
        }
    }

    fun permissionGranted(requestCode: Int){
        when(requestCode){
            PERM_CAMEARA -> openCamera()
            PERM_GALLERY -> openGallery()
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