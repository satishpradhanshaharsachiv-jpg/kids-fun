package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

/**
 * PencilSketchDialog fulfills the user's explicit prompt:
 * "दिसणारी स्क्रीन: गेम कसा दिसेल याचे साधे चित्र कागदावर पेन्सिपने काढून ठेवायचे."
 * Displays a charming hand-drawn style design blueprint on paper texture with pencil notes!
 */
@Composable
fun PencilSketchDialog(
  onDismiss: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(
        containerColor = Color(0xFFFAF7EE) // Warm textured sketch paper color
      ),
      elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .border(2.dp, Color(0xFF6D6875), RoundedCornerShape(16.dp))
          .padding(16.dp)
          .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "📝 कागदावरील पेन्सिल स्केच",
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF2B2D42),
              fontFamily = FontFamily.SansSerif
            )
            Text(
              text = "Pencil Wireframe Blueprint",
              fontSize = 12.sp,
              color = Color(0xFF6D6875)
            )
          }
          IconButton(onClick = onDismiss) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = Color(0xFF2B2D42)
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Sketch Paper Box with hand-drawn style wireframe
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(290.dp)
            .background(Color(0xFFFFFDF8), RoundedCornerShape(12.dp))
            .border(
              width = 1.5.dp,
              color = Color(0xFF555B6E),
              shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
        ) {
          // Lined paper sketch canvas
          Canvas(modifier = Modifier.fillMaxSize()) {
            val dashEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

            // Grid / notebook lines
            for (y in 40..size.height.toInt() step 45) {
              drawLine(
                color = Color(0x22555B6E),
                start = Offset(0f, y.toFloat()),
                end = Offset(size.width, y.toFloat()),
                strokeWidth = 1f
              )
            }

            // Divider between sky and ground
            drawLine(
              color = Color(0xFF6D6875),
              start = Offset(0f, size.height * 0.65f),
              end = Offset(size.width, size.height * 0.65f),
              strokeWidth = 2f,
              pathEffect = dashEffect
            )

            // Road line
            drawLine(
              color = Color(0xFF6D6875),
              start = Offset(0f, size.height * 0.88f),
              end = Offset(size.width, size.height * 0.88f),
              strokeWidth = 2.5f
            )
          }

          // Content of the sketch
          Column(modifier = Modifier.fillMaxSize()) {
            // Header wireframe
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFF6D6875), RoundedCornerShape(6.dp))
                .padding(vertical = 4.dp, horizontal = 8.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "📱 [ शीर्ष पट्टी: बाल खेळ | 🎵 हसण्याचे आवाज | ⭐ तारे ]",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF333333)
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Categories row sketch
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceEvenly
            ) {
              SketchPill("🎈 फुगे")
              SketchPill("🌸 फुले")
              SketchPill("🐦 पक्षी")
              SketchPill("🚗 गाड्या")
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Main Sky sketch area
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
              horizontalArrangement = Arrangement.SpaceAround,
              verticalAlignment = Alignment.CenterVertically
            ) {
              SketchItemDoodle("🎈", "फुगे वर तरंगतात\n(टॅप करताच फुटतात)")
              SketchItemDoodle("🐦", "पक्षी झाडावर\n(चिवचिव आवाज)")
              SketchItemDoodle("☀️", "हसरा सूर्य\n(संगीत सूर)")
            }

            // Ground & Road area
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
              horizontalArrangement = Arrangement.SpaceAround,
              verticalAlignment = Alignment.CenterVertically
            ) {
              SketchItemDoodle("🌸", "फुले उमलतात\n(सा रे ग म)")
              SketchItemDoodle("🚗", "गाड्या धावतात\n(बीप-बीप हॉर्न)")
              SketchItemDoodle("😄", "लहान मुलांचे हसू\n(खिलखिलाट)")
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Notes explanation
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFF0EFEB)),
          shape = RoundedCornerShape(10.dp)
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Text(
              text = "📌 डिझाईन वैशिष्ट्ये (Features):",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF2B2D42)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "1. रंगीबेरंगी चित्रे: लहान मुलांसाठी फुगे, फुले, पक्षी आणि गाड्यांची सुंदर चित्रे.",
              fontSize = 12.sp,
              color = Color(0xFF4A4E69)
            )
            Text(
              text = "2. गमतीदार आवाज: बटण दाबल्यावर वाजणारे छान संगीत, टॅपचे आवाज आणि बाळांचे हसू.",
              fontSize = 12.sp,
              color = Color(0xFF4A4E69)
            )
            Text(
              text = "3. पेन्सिल स्केच: कागदावर रेखाटल्यासारखा गेमचा आराखडा.",
              fontSize = 12.sp,
              color = Color(0xFF4A4E69)
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
          onClick = onDismiss,
          modifier = Modifier.fillMaxWidth(),
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A4E69))
        ) {
          Text(text = "खेळ सुरू करा (Play Game)", color = Color.White)
        }
      }
    }
  }
}

@Composable
private fun SketchPill(text: String) {
  Box(
    modifier = Modifier
      .border(1.dp, Color(0xFF6D6875), RoundedCornerShape(12.dp))
      .padding(horizontal = 6.dp, vertical = 2.dp)
  ) {
    Text(
      text = text,
      fontSize = 10.sp,
      fontWeight = FontWeight.Medium,
      color = Color(0xFF2B2D42)
    )
  }
}

@Composable
private fun SketchItemDoodle(emoji: String, note: String) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(text = emoji, fontSize = 24.sp)
    Text(
      text = note,
      fontSize = 9.sp,
      lineHeight = 11.sp,
      textAlign = TextAlign.Center,
      color = Color(0xFF4A4E69)
    )
  }
}
