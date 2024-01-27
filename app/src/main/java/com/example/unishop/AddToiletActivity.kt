package com.example.unishop

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.example.unishop.data.Toilet
import com.example.unishop.data.ToiletCategory
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream


class AddToiletActivity : AppCompatActivity() {
    private lateinit var priceEditText: EditText
    private lateinit var lifespanEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var StandartCheckBox: CheckBox
    private lateinit var EliteCheckBox: CheckBox
    private lateinit var SmartCheckBox: CheckBox
    private lateinit var VillageCheckBox: CheckBox
    private lateinit var EcologicCheckBox: CheckBox
    private lateinit var NotStandartCheckBox: CheckBox
    private lateinit var imageButton: Button
    private lateinit var saveButton: Button
    private lateinit var image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_toilet)

        // Инициализация полей и кнопки
        priceEditText = findViewById(R.id.editTextPrice)
        lifespanEditText = findViewById(R.id.editTextLifespan)
        descriptionEditText = findViewById(R.id.editTextDescription)
        imageButton = findViewById(R.id.buttonImage)
        saveButton = findViewById(R.id.buttonSave)
        image = findViewById(R.id.imageView)
        StandartCheckBox = findViewById(R.id.checkBoxStandart)
        EliteCheckBox = findViewById(R.id.checkBoxElite)
        SmartCheckBox = findViewById(R.id.checkBoxSmart)
        VillageCheckBox = findViewById(R.id.checkBoxVillage)
        EcologicCheckBox = findViewById(R.id.checkBoxEcologic)
        NotStandartCheckBox = findViewById(R.id.checkBoxNotStandart)

        imageButton.setOnClickListener {
            getImage()
        }
        // Обработчик нажатия на кнопку "Сохранить"
        saveButton.setOnClickListener {
            saveToiletData()
        }
    }

    fun getImage() {
        val intentChooser = Intent()
        intentChooser.type = "image/*"
        intentChooser.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(intentChooser)
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            if (data != null && data.data != null){
                image.setImageURI(data.data)
            }
        }
    }

    fun uploadImage(id: String) {
        val bitmap = image.drawable.toBitmap()
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val byteArray = baos.toByteArray()
        val storageRef = FirebaseStorage.getInstance().getReference("ImageDB")
        val newStorageRef = storageRef.child(System.currentTimeMillis().toString() + "image")
        newStorageRef.putBytes(byteArray).addOnSuccessListener { taskSnapshot ->
            newStorageRef.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()
                val database = FirebaseDatabase.getInstance()
                val ref = database.getReference("Toilets").child(id).child("imageUrl")
                ref.setValue(imageUrl)
            }
        }
    }


    private fun saveToiletData() {
        // Получение значений из полей ввода
        val price = priceEditText.text.toString().toInt()
        val lifespan = lifespanEditText.text.toString().toInt()
        val description = descriptionEditText.text.toString()

        // Получение ссылки на базу данных
        val database = FirebaseDatabase.getInstance()
        val toiletRef = database.getReference("Toilets")

        // Создание объекта унитаза
        val toilet = Toilet(null, price, lifespan, null, description)
        val newToiletRef = toiletRef.push()
        toilet.id = newToiletRef.key
        newToiletRef.setValue(toilet)
        toilet.id?.let { uploadImage(it) }
        val ToiletCategoryRef = database.getReference("ToiletCategories")
        if (StandartCheckBox.isChecked){
            val toiletcategory = ToiletCategory(toilet.id, "стандартные")
            val newToiletCategoryRef = ToiletCategoryRef.push()
            newToiletCategoryRef.setValue(toiletcategory)
        }
        if (EliteCheckBox.isChecked){
            val toiletcategory = ToiletCategory(toilet.id, "элитарные")
            val newToiletCategoryRef = ToiletCategoryRef.push()
            newToiletCategoryRef.setValue(toiletcategory)
        }
        if (SmartCheckBox.isChecked){
            val toiletcategory = ToiletCategory(toilet.id, "смарт")
            val newToiletCategoryRef = ToiletCategoryRef.push()
            newToiletCategoryRef.setValue(toiletcategory)
        }
        if (VillageCheckBox.isChecked){
            val toiletcategory = ToiletCategory(toilet.id, "деревенские")
            val newToiletCategoryRef = ToiletCategoryRef.push()
            newToiletCategoryRef.setValue(toiletcategory)
        }
        if (EcologicCheckBox.isChecked){
            val toiletcategory = ToiletCategory(toilet.id, "экологичные")
            val newToiletCategoryRef = ToiletCategoryRef.push()
            newToiletCategoryRef.setValue(toiletcategory)
        }
        if (NotStandartCheckBox.isChecked){
            val toiletcategory = ToiletCategory(toilet.id, "нестандартные")
            val newToiletCategoryRef = ToiletCategoryRef.push()
            newToiletCategoryRef.setValue(toiletcategory)
        }

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}