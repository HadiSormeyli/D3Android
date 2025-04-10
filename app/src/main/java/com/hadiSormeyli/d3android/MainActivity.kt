package com.hadiSormeyli.d3android

import android.os.Bundle
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
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


        findViewById<Button>(R.id.back_button).setOnClickListener {
            handleBackPress(stockTreeMapVew)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackPress(stockTreeMapVew)
            }
        })

        CoroutineScope(Dispatchers.IO).launch {
//            val bGroupChildren = mutableListOf<TreeMapNodeData>()
//            for (i in 1..200) {
//                bGroupChildren.add(
//                    TreeMapNodeData(
//                        name = "B$i",
//                        value = floor(Math.random() * 500).toFloat(), // Dynamic value
//                        colorValue = (floor(Math.random() * 100) - 50).toFloat()// Dynamic color value
//                    )
//                )
//            }
//
//            val data = TreeMapNodeData(
//                name = "root",
//                children = listOf(
//                    TreeMapNodeData(
//                        name = "A",
//                        children = listOf(
//                            TreeMapNodeData(name = "A3-1", value = 200f, colorValue = 10f),
//                            TreeMapNodeData(name = "A3-2", value = 100f, colorValue = -10f),
//                            TreeMapNodeData(name = "A3-3", value = 150f, colorValue = 5f),
//                            TreeMapNodeData(name = "A3-4", value = 50f, colorValue = -5f)
//                        )
//                    ),
//                    TreeMapNodeData(
//                        name = "D",
//                        children = listOf(
//                            TreeMapNodeData(name = "D3-1", value = 200f, colorValue = 10f),
//                            TreeMapNodeData(name = "D3-2", value = 100f, colorValue = -10f),
//                            TreeMapNodeData(name = "D3-3", value = 150f, colorValue = 5f),
//                            TreeMapNodeData(name = "D3-4", value = 50f, colorValue = -5f),
//                            TreeMapNodeData(name = "D3-4", value = 50f, colorValue = -5f),
//                            TreeMapNodeData(name = "D3-4", value = 50f, colorValue = -5f),
//                            TreeMapNodeData(name = "D3-4", value = 50f, colorValue = -5f),
//                            TreeMapNodeData(name = "D3-4", value = 50f, colorValue = -5f),
//                            TreeMapNodeData(name = "D3-4", value = 50f, colorValue = -5f),
//                            TreeMapNodeData(name = "D3-4", value = 50f, colorValue = -5f),
//                            TreeMapNodeData(name = "D3-4", value = 50f, colorValue = -5f)
//                        )
//                    ),
//                    TreeMapNodeData(
//                        name = "B",
//                        children = bGroupChildren
//                    ),
//                    TreeMapNodeData(
//                        name = "C",
//                        children = listOf(
//                            TreeMapNodeData(name = "C1", value = 300f, colorValue = 15f),
//                            TreeMapNodeData(name = "C2", value = 100f, colorValue = -5f),
//                            TreeMapNodeData(name = "C3", value = 450f, colorValue = 25f),
//                            TreeMapNodeData(name = "C4", value = 50f, colorValue = -15f)
//                        )
//                    )
//                )
//            )

            val data = distributeDataIntoGroups()


            withContext(Dispatchers.Main) {
                stockTreeMapVew.api.setData(data)
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
                        TreeMapNodeData(name = "تلویزیون", value = 200f, colorValue = 10f),  // "TV"
                        TreeMapNodeData(name = "گوشی موبایل", value = 100f, colorValue = -10f),  // "Mobile Phone"
                        TreeMapNodeData(name = "لپ‌تاپ", value = 150f, colorValue = 5f),  // "Laptop"
                        TreeMapNodeData(name = "دوربین عکاسی", value = 50f, colorValue = -5f),  // "Camera"
                        TreeMapNodeData(name = "کنسول بازی", value = 50f, colorValue = -5f)  // "Game Console"
                    )
                ),
                TreeMapNodeData(
                    name = "فروشگاه پوشاک",  // "Clothing Store"
                    children = listOf(
                        TreeMapNodeData(name = "پیراهن مردانه", value = 200f, colorValue = 0f),  // "Men's Shirt"
                        TreeMapNodeData(name = "دستکش زنانه", value = 400f, colorValue = 20f),  // "Women's Gloves"
                        TreeMapNodeData(name = "کت و شلوار", value = 250f, colorValue = 10f),  // "Suit"
                        TreeMapNodeData(name = "کفش ورزشی", value = 150f, colorValue = -10f)  // "Sports Shoes"
                    )
                ),
                TreeMapNodeData(
                    name = "فروشگاه لوازم خانگی",  // "Home Appliances Store"
                    children = listOf(
                        TreeMapNodeData(name = "یخچال", value = 300f, colorValue = 15f),  // "Refrigerator"
                        TreeMapNodeData(name = "ماشین لباسشویی", value = 100f, colorValue = -5f),  // "Washing Machine"
                        TreeMapNodeData(name = "آسیاب برقی", value = 450f, colorValue = 25f),  // "Electric Grinder"
                        TreeMapNodeData(name = "چای ساز", value = 50f, colorValue = -15f)  // "Tea Maker"
                    )
                )
            )
        )
    }

    fun distributeDataIntoGroups() : TreeMapNodeData {
            // Step 1: Create 900 random data points
            val totalDataPoints = 900
            val totalGroups = 30
            val dataPoints = mutableListOf<TreeMapNodeData>()

            for (i in 1..totalDataPoints) {
                val value = floor(Math.random() * 500).toFloat()  // Dynamic value
                val colorValue = (floor(Math.random() * 100) - 50).toFloat()  // Dynamic color value
                dataPoints.add(TreeMapNodeData(name = "Data$i", value = value, colorValue = colorValue))
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