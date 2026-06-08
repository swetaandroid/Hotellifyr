package com.xongolab.hotellifyr.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.DialogFragment
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.databinding.DialogChooseSourceBinding
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class ImagePicker : DialogFragment {
    private var outputFileUri: Uri? = null
    private  var imgPath: String? = null
    private var mContext: Context? = null

    var listener: ((imagePath: String, uri: Uri) -> Unit)? = null

    private lateinit var binding: DialogChooseSourceBinding

    constructor()

    constructor(context: Context?) {
        this.mContext = context
    }


    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(0, R.style.MaterialDialogSheet)
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setGravity(Gravity.BOTTOM)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogChooseSourceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.llCamera.setOnClickListener { if (this@ImagePicker.chkCameraPermission()) this@ImagePicker.fromCamera() }
        binding.llGallery.setOnClickListener(View.OnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                callGallery()
            } else {
                if (this@ImagePicker.chkStoragePermission()) callGallery()
            }
        })
        binding.tvCancel.setOnClickListener { this@ImagePicker.dismiss() }
    }

    private fun chkStoragePermission(): Boolean {
        var result: Int

        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        val listPermissionsNeeded: MutableList<String> = ArrayList()
        for (p in permissions) {
            result = ContextCompat.checkSelfPermission(requireContext(), p)
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p)
            }
        }

        if (listPermissionsNeeded.isNotEmpty()) {
            requestPermissions(
                listPermissionsNeeded.toTypedArray<String>(),
                STORAGE_PERMISSION_REQUEST
            )
            return false
        } else {
            return true
        }
    }

    private fun chkCameraPermission(): Boolean {
        var result: Int

        val permissions = arrayOf(Manifest.permission.CAMERA)

        val listPermissionsNeeded: MutableList<String> = ArrayList()
        for (p in permissions) {
            result = ContextCompat.checkSelfPermission(requireContext(), p)
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p)
            }
        }
        if (listPermissionsNeeded.isNotEmpty()) {
            requestPermissions(
                listPermissionsNeeded.toTypedArray<String>(),
                CAMERA_PERMISSION_REQUEST
            )
            return false
        } else {
            return true
        }
    }

    private fun callGallery() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_PICK)
        startActivityForResult(Intent.createChooser(intent, "Select Source"), STORAGE_REQUEST)
    }

    private fun fromCamera() {
        outputFileUri = captureImageOutputUri
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (null != outputFileUri) captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
        try {
            startActivityForResult(captureIntent, CAMERA_REQUEST) //ActivityNotFoundException
        } catch (ex: ActivityNotFoundException) {
            ex.printStackTrace()
        }
    }

    private val captureImageOutputUri: Uri?
        @SuppressLint("ObsoleteSdkInt")
        get() {
            val getImage = requireContext().externalCacheDir
            if (getImage != null) {
                outputFileUri =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) FileProvider.getUriForFile(
                        requireContext(), requireContext().packageName + ".fileprovider",
                        File(getImage.path, "pickImageResult.jpeg")
                    ) //IllegalArgumentException
                    else Uri.fromFile(File(getImage.path, "pickImageResult.jpeg"))
            }
            return outputFileUri
        }

    private fun getRealPathFromUri(context: Context?, uri: Uri): File? {
        try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val fileName = getFileName(context, uri)
            val splitName = splitFileName(fileName)
            var tempFile = File.createTempFile(splitName[0], splitName[1])
            tempFile = rename(tempFile, fileName)
            tempFile.deleteOnExit()
            val out = FileOutputStream(tempFile)

            if (inputStream != null) {
                copy(inputStream, out)
                inputStream.close()
            }

            out.close()
            return tempFile
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    private fun splitFileName(fileName: String?): Array<String?> {
        var name = fileName
        var extension: String? = ""
        val i = fileName!!.lastIndexOf(".")
        if (i != -1) {
            name = fileName.substring(0, i)
            extension = fileName.substring(i)
        }
        return arrayOf(name, extension)
    }

    @Throws(IOException::class)
    private fun copy(input: InputStream, output: OutputStream) {
        var n: Int
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        while (EOF != (input.read(buffer).also { n = it })) {
            output.write(buffer, 0, n)
        }
    }

    private fun getFileName(context: Context?, uri: Uri): String? {
        var result: String? = null
        if (uri.scheme != null && uri.scheme == "content") {
            val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result =
                        cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                cursor?.close()
            }
        }

        if (result == null) {
            result = uri.path
            if (result != null) {
                val cut = result.lastIndexOf(File.separator)
                if (cut != -1) {
                    result = result.substring(cut + 1)
                }
            }
        }

        return result
    }

    override fun dismiss() {
        super.dismiss()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.e(
            TAG,
            "onActivityResult: $requestCode==>$resultCode"
        )
        if (resultCode == Activity.RESULT_OK) {
            val uri: Uri?
            if (requestCode == STORAGE_REQUEST) {
                if (data != null) {
                    uri = data.data
                    if (uri != null) {
                        imgPath = getRealPathFromUri(context, uri)!!.absolutePath
                        Log.e(TAG, "Gallery Path: $imgPath")
                        imgPath = compressImage(imgPath)
                        if (listener != null) {
                            listener?.invoke(imgPath!!, uri)
                        }
                        dismiss()
                    } else {
                        Log.e(TAG, "uri null: ")
                    }
                }
            } else if (requestCode == CAMERA_REQUEST) {
                Log.e("CAMERA_REQUEST", "load 1")

                try {
                    if (outputFileUri != null) {
                        imgPath = getRealPathFromUri(context, outputFileUri!!)!!.absolutePath
                        Log.e(TAG, "Camera Path: $imgPath")
                        imgPath = compressImage(imgPath)
                        if (listener != null) {
                            listener?.invoke(imgPath!!, outputFileUri!!)
                        }
                        dismiss()
                    } else {
                        Log.e(TAG, "Uri Null")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    e.localizedMessage
                    Log.e("CAMERA_REQUEST", "Failed to load", e)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            CAMERA_PERMISSION_REQUEST -> {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "Camera permission required", Toast.LENGTH_SHORT).show()
                } else {
                    fromCamera()
                }
            }

            STORAGE_PERMISSION_REQUEST -> {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "Storage permission required", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    callGallery()
                }
            }
        }
    }

    // Start - Compress image
    fun compressImage(filePath: String?): String {
        var scaledBitmap: Bitmap? = null

        val options = BitmapFactory.Options()

        options.inJustDecodeBounds = true
        var bmp = BitmapFactory.decodeFile(filePath, options)

        var actualHeight = options.outHeight
        var actualWidth = options.outWidth

        val maxHeight = 1024.0f
        val maxWidth = 1024.0f
        var imgRatio = (actualWidth / actualHeight).toFloat()
        val maxRatio = maxWidth / maxHeight

        //      width and height values are set maintaining the aspect ratio of the image
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight.toInt()
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth.toInt()
            } else {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()
            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)
        options.inJustDecodeBounds = false
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)

        try {
            bmp = BitmapFactory.decodeFile(filePath, options)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
        } catch (exception: Exception) {
            exception.printStackTrace()
        }

        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f

        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)

        val canvas = Canvas(scaledBitmap!!)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(
            bmp,
            middleX - bmp.width / 2,
            middleY - bmp.height / 2,
            Paint(Paint.FILTER_BITMAP_FLAG)
        )

        //      check the rotation of the image and display it properly
        val exif: ExifInterface
        try {
            exif = ExifInterface(filePath!!)

            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, 0
            )
            Log.d("EXIF", "Exif: $orientation")
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 3) {
                matrix.postRotate(180f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 8) {
                matrix.postRotate(270f)
                Log.d("EXIF", "Exif: $orientation")
            }
            scaledBitmap = Bitmap.createBitmap(
                scaledBitmap, 0, 0,
                scaledBitmap.width, scaledBitmap.height, matrix,
                true
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val out: FileOutputStream
        val filename = filename
        try {
            out = FileOutputStream(filename)

            //          write the compressed bitmap at the destination specified by filename.
            scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 70, out)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        return filename
    }

    val filename: String
        get() {
            val dir = File(requireContext().getExternalFilesDir("Output").toString())
            if (!dir.exists()) {
                dir.mkdirs()
            }

            val dest = File(dir.path + File.separator + "Output" + System.currentTimeMillis() + ".jpg")

            return dest.absolutePath
        }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        val totalPixels = (width * height).toFloat()
        val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }

        return inSampleSize
    }

    interface OnUpdate {
        fun set(imagePath: String?)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {
        private const val CAMERA_REQUEST = 1
        private const val STORAGE_REQUEST = 2
        private const val CAMERA_PERMISSION_REQUEST = 101
        private const val STORAGE_PERMISSION_REQUEST = 102
        private const val TAG = "ImagePicker"
        private const val EOF = -1
        private const val DEFAULT_BUFFER_SIZE = 2048 * 4

        private fun rename(file: File, newName: String?): File {
            val newFile = File(file.parent, newName)
            if (newFile != file) {
                if (newFile.exists() && newFile.delete()) {
                    Log.d("FileUtil", "Delete old $newName file")
                }
                if (file.renameTo(newFile)) {
                    Log.d("FileUtil", "Rename file to $newName")
                }
            }
            return newFile
        }
    }
}
