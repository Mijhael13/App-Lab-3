package com.example.app_lab_3.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_lab_3.ui.components.FitzoneHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen() {
    val monthName = "NOVIEMBRE"
    val yearLabel = "Noviembre 2026"
    val daysInMonth = 30
    val firstDayOffset = 6

    val checkedDays = remember { 
        mutableStateListOf(2, 3, 5, 6, 9, 10, 12, 13, 16, 17, 19, 20) 
    }
    
    var weeklyGoal by remember { mutableIntStateOf(4) }
    val monthlyGoal = weeklyGoal * 4
    val trainedDaysCount = checkedDays.size
    val progressPercent = if (monthlyGoal > 0) ((trainedDaysCount.toFloat() / monthlyGoal) * 100).toInt() else 0
    val progress = if (monthlyGoal > 0) (trainedDaysCount.toFloat() / monthlyGoal).coerceIn(0f, 1f) else 0f

    var showStats by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9F8))
            .verticalScroll(rememberScrollState())
    ) {
        // Pass yearLabel as subtitle to FitzoneHeader
        FitzoneHeader(screenName = "Mi Calendario", subtitle = yearLabel)
        
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            // Card del Calendario
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = monthName,
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = null, modifier = Modifier.size(24.dp), tint = Color.Gray)
                            Spacer(modifier = Modifier.width(16.dp))
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, modifier = Modifier.size(24.dp), tint = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        listOf("L", "M", "M", "J", "V", "S", "D").forEach {
                            Text(
                                text = it,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                fontSize = 12.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    val totalItems = firstDayOffset + daysInMonth
                    val rows = (totalItems + 6) / 7
                    
                    Column {
                        for (row in 0 until rows) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                for (col in 0 until 7) {
                                    val index = row * 7 + col
                                    val day = index - firstDayOffset + 1
                                    
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f)
                                            .padding(4.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (day in 1..daysInMonth) {
                                            val isChecked = checkedDays.contains(day)
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .clip(RoundedCornerShape(10.dp))
                                                    .background(if (isChecked) MaterialTheme.colorScheme.primary else Color.Transparent)
                                                    .clickable {
                                                        if (isChecked) checkedDays.remove(day) else checkedDays.add(day)
                                                    },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = day.toString(),
                                                    fontSize = 14.sp,
                                                    fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Medium,
                                                    color = if (isChecked) Color.White else Color.Black
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showStats = !showStats },
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "$trainedDaysCount días entrenados",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Meta: $monthlyGoal días  ·  $progressPercent% completado",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            AnimatedVisibility(visible = showStats) {
                Column {
                    Spacer(modifier = Modifier.height(48.dp))

                    Text(
                        text = "Mis estadísticas",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Tu constancia es tu superpoder",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    StatCard {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Text(
                                text = trainedDaysCount.toString(),
                                fontSize = 64.sp,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.primary,
                                lineHeight = 64.sp
                            )
                            Text(
                                text = "DÍAS ENTRENADOS",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Color.Gray,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "$progressPercent% de tu meta mensual",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.DarkGray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    StatCard {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Text(text = "Progreso mensual", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(24.dp))
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(12.dp)
                                    .clip(RoundedCornerShape(6.dp)),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.primaryContainer
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "$trainedDaysCount / $monthlyGoal entrenamientos",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        content()
    }
}
