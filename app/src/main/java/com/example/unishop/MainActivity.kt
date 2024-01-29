package com.example.unishop

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.unishop.data.Toilet
import com.example.unishop.data.ToiletCategory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import android.graphics.Typeface

class MainActivity : AppCompatActivity() {

    private lateinit var spinner: Spinner
    private lateinit var layout: LinearLayout
    private lateinit var button: Button
    private lateinit var buttonaddtoilet: FloatingActionButton
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        spinner = findViewById(R.id.spinner)
        layout = findViewById(R.id.linearLayout)
        button = findViewById(R.id.button)
        buttonaddtoilet = findViewById(R.id.floatingActionButton)
        progressBar = findViewById(R.id.progressBar3)

        var items = listOf("все", "стандартные", "элитарные", "смарт", "деревенские", "экологичные", "нестандартные") // Замените данными своего списка

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter

        button.setOnClickListener {
            layout.removeAllViews()
            progressBar.visibility = android.view.View.VISIBLE

            if (spinner.selectedItem.toString() == "все"){
                getalltoilets()
            }
            else{
                gettoiletsbycategory(spinner.selectedItem.toString())
            }
        }

        buttonaddtoilet.setOnClickListener {
            openSecondActivity()
        }
    }

    fun getToiletById(id: String) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("Toilets").child(id)

        ref.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val newToilet = task.result.getValue(Toilet::class.java)
                if (newToilet != null) {
                    val toilets = listOf(newToilet).toMutableList()
                    generatetoilets(toilets)
                }
            }
        }
    }


    fun gettoiletsbycategory(category : String){
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("ToiletCategories")
        ref.get().addOnCompleteListener{
            for (child in it.result.children){
                val toiletcategory = child.getValue(ToiletCategory::class.java)
                if (toiletcategory != null) {
                    if (toiletcategory.categoryname == category) {
                        val id = toiletcategory.toiletId
                        if (id != null) {
                            getToiletById(id)
                        }
                    }
                }
            }
        }
    }

    fun getalltoilets(){
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("Toilets")
        ref.get().addOnCompleteListener{
            val toilets = listOf<Toilet>().toMutableList()
            for (child in it.result.children){
                val toilet = child.getValue(Toilet::class.java)
                if (toilet != null) {
                    toilets.add(toilet)
                }
            }
            generatetoilets(toilets)
        }
    }

    fun generatetoilets(toilets: List<Toilet>) {
        progressBar.visibility = android.view.View.INVISIBLE
        for (t in toilets){
            val horizontalLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                setBackgroundResource(R.drawable.border) // Добавлено: задаем границу
                setPadding(10, 10, 10, 10) // Добавлено: задаем отступы
            }

            val imgView = ImageView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    0, // Изменено на 0
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f // Добавлено: задаем вес
                )
                adjustViewBounds = true // Добавлено: сохраняем пропорции изображения
                scaleType = ImageView.ScaleType.CENTER_CROP // Добавлено: задаем тип масштабирования изображения
                setPadding(10, 10, 10, 10) // Добавлено: задаем отступы
            }

            val verticalLayout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(
                    0, // Изменено на 0
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    2f // Добавлено: задаем вес
                )
                setPadding(10, 10, 10, 10) // Добавлено: задаем отступы
            }

            val textViewPrice = TextView(this).apply {
                text = "${t.price} ₽" // Замените на поле с ценой унитаза в вашем классе Toilet
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                textSize = 20f // Добавлено: увеличиваем размер текста
                setTypeface(null, Typeface.BOLD) // Добавлено: делаем текст жирным
                setPadding(10, 10, 10, 10) // Добавлено: задаем отступы
            }

            val textViewDescription = TextView(this).apply {
                text = t.description // Замените на поле с описанием унитаза в вашем классе Toilet
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                setPadding(10, 10, 10, 10) // Добавлено: задаем отступы
            }

            Picasso.get().load(t.imageUrl).into(imgView)

            verticalLayout.addView(textViewPrice) // Добавлено: добавляем textViewPrice в вертикальный layout
            verticalLayout.addView(textViewDescription) // Добавлено: добавляем textViewDescription в вертикальный layout
            horizontalLayout.addView(imgView)
            horizontalLayout.addView(verticalLayout) // Изменено: добавляем вертикальный layout в горизонтальный layout
            layout.addView(horizontalLayout)
        }
    }


    fun openSecondActivity() {
        val intent = Intent(this, AddToiletActivity::class.java)
        startActivity(intent)
    }
}
