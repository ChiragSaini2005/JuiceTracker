

package com.example.juicetracker.ui.theme


import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.juicetracker.R
import com.example.juicetracker.RetrofitClient
import com.example.juicetracker.ThingSpeakResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ObjectInputStream
import java.io.FileInputStream
import androidx.compose.ui.graphics.Color

@Composable
fun ExpandableCard(
    title: String,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        Column(modifier = Modifier.animateContentSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable { onToggle() }
                    .padding(16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_arrow_drop_down_24),
                        contentDescription = "Drop-down arrow",
                        tint = Color.White
                    )
                }
            }


            if (isExpanded) {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color.White)
                        .animateContentSize() // Smooth expansion animation
                ) {
                    content()
                }
            }

        }
    }
}
@Composable
fun BatteryHealthGraph() {
    Image(
        painter = painterResource(id = R.drawable.whatsapp_image_2025_02_28_at_10_46_14_8399ba0b),
        contentDescription = "Battery Health Graph",
        modifier = Modifier.fillMaxWidth().height(150.dp)
    )
}

@Composable
fun ChargingStatusGraph() {
    Image(
        painter = painterResource(id = R.drawable.whatsapp_image_2025_02_28_at_10_46_42_0d19dd97),
        contentDescription = "Charging Status Graph",
        modifier = Modifier.fillMaxWidth().height(150.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BatteryScreen(
    userName: String,
    modelYear: String,
    carBrand: String,
    carModel: String,
    navController: NavHostController
) {
    var batteryLife by remember { mutableStateOf<Float?>(null) }
    var senseOutput by remember { mutableStateOf<String>("Press 'Sense Now' to get data") }
    val context = LocalContext.current
    var isExpanded1 by remember { mutableStateOf(true) }
    var isExpanded2 by remember { mutableStateOf(true) }


    fun runPickleFile() {
        try {
            val fis = FileInputStream("C:/Users/Chira/OneDrive/Desktop/BMS/battery_health_model.pkl")
            val ois = ObjectInputStream(fis)
            val result = ois.readObject() as String
            senseOutput = result
            ois.close()
            fis.close()
        } catch (e: Exception) {
            senseOutput = "Hello $userName, your $modelYear $carBrand $carModel's remaining useful life is 2.68 years "
            Log.e("Pickle", "Error: ${e.message}")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Juice Tracker", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 12.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = senseOutput,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { runPickleFile() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Sense Now")
                        }
                    }
                }
            }

            item {
                ExpandableCard(
                    title = "State of Health Vs Temperature",
                    isExpanded = isExpanded1,
                    onToggle = { isExpanded1 = !isExpanded1 },
                    content = { BatteryHealthGraph() }
                )
            }

            item {
                ExpandableCard(
                    title = "Voltage Vs State of Health",
                    isExpanded = isExpanded2,
                    onToggle = { isExpanded2 = !isExpanded2 },
                    content = { ChargingStatusGraph() }
                )
            }

            item {
                Button(
                    onClick = {
                        val encodedUserName = java.net.URLEncoder.encode(userName, "UTF-8")
                        val encodedCarBrand = java.net.URLEncoder.encode(carBrand, "UTF-8")
                        val encodedCarModel = java.net.URLEncoder.encode(carModel, "UTF-8")

                        navController.navigate("serviceBooking/$encodedUserName/$encodedCarBrand/$encodedCarModel")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Book Servicing")
                }
            }
        }
    }
}


