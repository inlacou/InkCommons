package com.inlacou.lib.inkcomposeextensions.dialogs

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inlacou.inker.Inker
import java.util.Calendar

@Composable
fun YearMonthPickerDialog(
    current: Calendar = Calendar.getInstance(),
    onConfirm: (year: Int, month: Int) -> Unit,
    onCancel: () -> Unit
) = YearMonthPickerDialog(
    currentYear = current.get(Calendar.YEAR),
    currentMonth = current.get(Calendar.MONTH),
    onConfirm = onConfirm,
    onCancel = onCancel,
)

@Composable
fun YearMonthPickerDialog(
    currentYear: Int,
    currentMonth: Int,
    onConfirm: (year: Int, month: Int) -> Unit,
    onCancel: () -> Unit
) {
    Inker.d { "value: $currentYear/${currentMonth.toString().padStart(2, '0')}" }
    val months = listOf(
        "JAN",
        "FEB",
        "MAR",
        "APR",
        "MAY",
        "JUN",
        "JUL",
        "AUG",
        "SEP",
        "OCT",
        "NOV",
        "DEC"
    )

    var month by remember {
        mutableStateOf(months[currentMonth])
    }

    var year by remember {
        mutableIntStateOf(currentYear)
    }

    val interactionSource = remember {
        MutableInteractionSource()
    }

    @Composable
    fun monthsRow(months: List<String>) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            months.forEach {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clickable(
                            indication = null,
                            interactionSource = interactionSource,
                            onClick = {
                                month = it
                            }
                        )
                        .background(
                            color = Color.Transparent
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    val animatedSize by animateDpAsState(
                        targetValue = if (month == it) 60.dp else 0.dp,
                        animationSpec = tween(
                            durationMillis = 500,
                            easing = LinearOutSlowInEasing
                        )
                    )
                    Box(
                        modifier = Modifier
                            .size(animatedSize)
                            .background(
                                color = if (month == it) Color.Blue else Color.Transparent,
                                shape = CircleShape
                            )
                    )
                    Text(
                        text = it,
                        color = if (month == it) Color.White else Color.Black,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }

    AlertDialog(
        containerColor = Color.White,
        shape = RoundedCornerShape(10),
        onDismissRequest = {},
        title = {},
        text = {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .size(35.dp)
                            .rotate(90f)
                            .clickable(
                                indication = null,
                                interactionSource = interactionSource,
                                onClick = {
                                    year--
                                }
                            ),
                        imageVector = Icons.Rounded.KeyboardArrowDown,
                        contentDescription = null
                    )
                    Text(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        text = year.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        modifier = Modifier
                            .size(35.dp)
                            .rotate(-90f)
                            .clickable(
                                indication = null,
                                interactionSource = interactionSource,
                                onClick = {
                                    year++
                                }
                            ),
                        imageVector = Icons.Rounded.KeyboardArrowDown,
                        contentDescription = null
                    )
                }

                Card(
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .fillMaxWidth(),
                ) {
                    monthsRow(months.take(4))
                    monthsRow(months.drop(4).take(4))
                    monthsRow(months.drop(8).take(4))
                }
            }
        },
        confirmButton = {
            OutlinedButton(
                modifier = Modifier.padding(end = 20.dp),
                onClick = {
                    onConfirm(
                        year,
                        months.indexOf(month),
                    )
                },
                shape = CircleShape,
                border = BorderStroke(1.dp, color = Color.Blue),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent)
            ) {
                Text(
                    text = "OK",
                    color = Color.Blue,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                modifier = Modifier.padding(end = 20.dp),
                onClick = {
                    onCancel()
                },
                shape = CircleShape,
                border = BorderStroke(1.dp, color = Color.Transparent),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent)
            ) {
                Text(
                    text = "Cancel",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        },
    )
}