package com.example.securedocs

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.hardware.biometrics.BiometricPrompt
import android.os.Bundle
import android.util.Base64
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.*
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class FileActivity : AppCompatActivity() {

    private lateinit var btnUpload: Button
    private lateinit var lvFiles: ListView
    private lateinit var tvContent: TextView
    private var pin: String = ""

    private val PICK_FILE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file)

        pin = intent.getStringExtra("PIN") ?: ""

        btnUpload = findViewById(R.id.btnUpload)
        lvFiles = findViewById(R.id.lvFiles)
        tvContent = findViewById(R.id.tvContent)

        btnUpload.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(intent, PICK_FILE)
        }

        lvFiles.setOnItemClickListener { _, _, position, _ ->
            val fileName = lvFiles.getItemAtPosition(position) as String
            showPinPrompt(fileName)
        }

        lvFiles.setOnItemLongClickListener { _, _, position, _ ->
            val fileName = lvFiles.getItemAtPosition(position) as String
            AlertDialog.Builder(this)
                .setTitle("Delete file?")
                .setMessage("Are you sure you want to delete '$fileName'?")
                .setPositiveButton("Delete") { _, _ ->
                    deleteFile(fileName)
                    loadFileList()
                    tvContent.text = ""
                    Toast.makeText(this, "File deleted", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel", null)
                .show()
            true
        }

        loadFileList()
    }

    private fun showPinPrompt(fileName: String) {
        val input = EditText(this)
        input.inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD

        AlertDialog.Builder(this)
            .setTitle("Enter PIN to decrypt")
            .setView(input)
            .setPositiveButton("OK") { _, _ ->
                val enteredPin = input.text.toString()
                val encryptedData = openFileInput(fileName).readBytes()
                try {
                    val decrypted = decrypt(encryptedData, enteredPin)
                    val tempFile = File.createTempFile("decrypted_", fileName.substringAfterLast('.'), cacheDir)
                    tempFile.writeText(decrypted)

                    // Create intent to open file
                    val uri = androidx.core.content.FileProvider.getUriForFile(
                        this,
                        "$packageName.fileprovider",
                        tempFile
                    )
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(uri, getMimeType(fileName))
                    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

                    startActivity(Intent.createChooser(intent, "Open with..."))

                } catch (e: Exception) {
                    Toast.makeText(this, "Incorrect PIN or corrupted file", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    private fun showFileDialog(fileName: String, content: String) {
        AlertDialog.Builder(this)
            .setTitle("Viewing: $fileName")
            .setMessage(content)
            .setPositiveButton("Close", null)
            .show()
    }

    private fun loadFileList() {
        val files = fileList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, files.toList())
        lvFiles.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_FILE && resultCode == Activity.RESULT_OK) {
            val uri = data?.data ?: return
            val inputStream = contentResolver.openInputStream(uri) ?: return
            val fileName = uri.lastPathSegment?.substringAfterLast('/') ?: "file.enc"
            val content = inputStream.bufferedReader().readText()

            val encrypted = encrypt(content, pin)
            openFileOutput(fileName, MODE_PRIVATE).write(encrypted)

            Toast.makeText(this, "Encrypted and saved", Toast.LENGTH_SHORT).show()
            loadFileList()
        }
    }

    private fun encrypt(data: String, pin: String): ByteArray {
        val key = generateKey(pin)
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        return cipher.doFinal(data.toByteArray(Charsets.UTF_8))
    }

    private fun decrypt(data: ByteArray, pin: String): String {
        val key = generateKey(pin)
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, key)
        val decrypted = cipher.doFinal(data)
        return String(decrypted)
    }

    private fun generateKey(password: String): SecretKeySpec {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = password.toByteArray(Charsets.UTF_8)
        digest.update(bytes, 0, bytes.size)
        return SecretKeySpec(digest.digest(), "AES")
    }
}

private fun getMimeType(fileName: String): String {
    val ext = fileName.substringAfterLast('.', "").lowercase()
    return when (ext) {
        "pdf" -> "application/pdf"
        "jpg", "jpeg" -> "image/jpeg"
        "png" -> "image/png"
        "txt" -> "text/plain"
        else -> "*/*"
    }
}

