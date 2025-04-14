package com.hadiSormeyli.d3android

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.hadiSormeyli.d3android.model.ColorRange
import com.hadiSormeyli.d3android.model.TreeColorOptions
import com.hadiSormeyli.d3android.model.TreeMapNodeData
import com.hadiSormeyli.d3android.view.StockTreeMapVew
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.floor

class MainActivity : AppCompatActivity() {

    private fun handleBackPress(stockTreeMapVew: StockTreeMapVew) {
        stockTreeMapVew.api.onBackPressed {
            if (!it) {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val stockTreeMapVew = findViewById<StockTreeMapVew>(R.id.webView)

        stockTreeMapVew.subscribeOnChartStateChange {
            Log.d("Tag", "$it")
        }


        findViewById<Button>(R.id.back_button).setOnClickListener {
            handleBackPress(stockTreeMapVew)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackPress(stockTreeMapVew)
            }
        })

        CoroutineScope(Dispatchers.IO).launch {
            val bGroupChildren = mutableListOf<TreeMapNodeData>()
            for (i in 1..10) {
                bGroupChildren.add(
                    TreeMapNodeData(
                        name = "B$i",
                        value = 11f - i,
                        labelValue = floor(Math.random() * 500).toFloat(),
                        colorValue = (floor(Math.random() * 100) - 50).toFloat(),
                    )
                )
            }

            val data = TreeMapNodeData(
                name = "root",
                children = listOf(

                    TreeMapNodeData(
                        name = "B",
                        children = bGroupChildren
                    ),

                )
            )

            withContext(Dispatchers.Main) {
                stockTreeMapVew.api.setData(
                    data, TreeColorOptions()
                )
            }
        }
    }

    fun createTreeMapData(): TreeMapNodeData {
        return TreeMapNodeData(
            name = "نقشه بازار",  // "Market Map"
            children = listOf(
                TreeMapNodeData(
                    name = "فروشگاه الکترونیک",  // "Electronics Store"
                    children = listOf(
                        TreeMapNodeData(
                            name = "تلویزیون",
                            labelValue = 200f,
                            colorValue = 10f,
                            value = 100f
                        ),  // "TV"
                        TreeMapNodeData(
                            name = "گوشی موبایل",
                            labelValue = 100f,
                            colorValue = -10f,
                            value = 100f
                        ),  // "Mobile Phone"
                        TreeMapNodeData(
                            name = "لپ‌تاپ",
                            labelValue = 150f,
                            colorValue = 5f,
                            value = 100f
                        ),  // "Laptop"
                        TreeMapNodeData(
                            name = "دوربین عکاسی",
                            labelValue = 50f,
                            colorValue = -5f,
                            value = 100f
                        ),  // "Camera"
                        TreeMapNodeData(
                            name = "کنسول بازی",
                            labelValue = 50f,
                            colorValue = -5f,
                            value = 100f
                        )  // "Game Console"
                    )
                ),
                TreeMapNodeData(
                    name = "فروشگاه پوشاک",  // "Clothing Store"
                    children = listOf(
                        TreeMapNodeData(
                            name = "پیراهن مردانه",
                            labelValue = 200f,
                            colorValue = 0f,
                            value = 100f
                        ),  // "Men's Shirt"
                        TreeMapNodeData(
                            name = "دستکش زنانه",
                            labelValue = 400f,
                            colorValue = 20f,
                            value = 100f
                        ),  // "Women's Gloves"
                        TreeMapNodeData(
                            name = "کت و شلوار",
                            labelValue = 250f,
                            colorValue = 10f,
                            value = 100f
                        ),  // "Suit"
                        TreeMapNodeData(
                            name = "کفش ورزشی",
                            labelValue = 150f,
                            colorValue = -10f,
                            value = 100f
                        )  // "Sports Shoes"
                    )
                ),
                TreeMapNodeData(
                    name = "فروشگاه لوازم خانگی",  // "Home Appliances Store"
                    children = listOf(
                        TreeMapNodeData(
                            name = "یخچال",
                            labelValue = 300f,
                            colorValue = 15f,
                            value = 100f
                        ),  // "Refrigerator"
                        TreeMapNodeData(
                            name = "ماشین لباسشویی",
                            labelValue = 100f,
                            colorValue = -5f,
                            value = 100f
                        ),  // "Washing Machine"
                        TreeMapNodeData(
                            name = "آسیاب برقی",
                            labelValue = 450f,
                            colorValue = 25f,
                            value = 100f
                        ),  // "Electric Grinder"
                        TreeMapNodeData(
                            name = "چای ساز",
                            labelValue = 50f,
                            colorValue = -15f,
                            value = 100f
                        )  // "Tea Maker"
                    )
                )
            )
        )
    }

    fun distributeDataIntoGroups(): TreeMapNodeData {
        // Step 1: Create 900 random data points
        val totalDataPoints = 900
        val totalGroups = 30
        val dataPoints = mutableListOf<TreeMapNodeData>()

        for (i in 1..totalDataPoints) {
            val value = floor(Math.random() * 500).toFloat()  // Dynamic value
            val colorValue = (floor(Math.random() * 100) - 50).toFloat()  // Dynamic color value
            dataPoints.add(
                TreeMapNodeData(
                    name = "Data$i",
                    labelValue = value,
                    colorValue = colorValue
                )
            )
        }

        // Step 2: Distribute the 900 data points into 30 groups
        val groupSize = totalDataPoints / totalGroups
        val groups = mutableListOf<TreeMapNodeData>()

        for (i in 0 until totalGroups) {
            val groupName = "Group ${i + 1}"
            val groupData = dataPoints.subList(i * groupSize, (i + 1) * groupSize)
            groups.add(TreeMapNodeData(name = groupName, children = groupData))
        }

        // Step 3: Create the root node with 30 groups as children
        return TreeMapNodeData(
            name = "root",
            children = groups
        )


    }
}