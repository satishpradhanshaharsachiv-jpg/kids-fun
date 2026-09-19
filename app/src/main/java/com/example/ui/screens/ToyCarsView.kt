package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.audio.SoundEffectsEngine
import com.example.model.KidGameData
import com.example.model.ToyCarItem
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun ToyCarsView(
  soundEngine: SoundEffectsEngine,
  onAddStar: () -> Unit
) {
  var activeCarMessage by remember { mutableStateOf("गाड्यांवर टॅप करा आणि हॉर्न वाजवा! 🚗💨") }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          listOf(Color(0xFFFFF3E0), Color(0xFFECEFF1))
        )
      )
      .padding(horizontal = 12.dp)
  ) {
    // Top banner
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp),
      shape = RoundedCornerShape(18.dp),
      color = Color.White.copy(alpha = 0.92f),
      shadowElevation = 3.dp,
      border = BorderStroke(1.5.dp, TangerineOrange)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "🚗 छोट्या गाड्या (Toy Cars Track)",
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TangerineOrange
          )
          Text(
            text = activeCarMessage,
            fontSize = 12.sp,
            color = Color(0xFF546E7A),
            fontWeight = FontWeight.Medium
          )
        }

        IconButton(
          onClick = {
            soundEngine.playCarHorn()
            soundEngine.playGiggle()
            activeCarMessage = "सगळ्या गाड्यांचा हॉर्न: पीप पीप! 🚗🚌🚒"
            onAddStar()
          },
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(OrangeLight)
        ) {
          Text(text = "📢", fontSize = 18.sp)
        }
      }
    }

    LazyVerticalGrid(
      columns = GridCells.Fixed(2),
      contentPadding = PaddingValues(bottom = 24.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp),
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      modifier = Modifier.fillMaxSize()
    ) {
      // Top Hero Banner
      item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .height(130.dp),
          shape = RoundedCornerShape(18.dp),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
          Box(modifier = Modifier.fillMaxSize()) {
            Image(
              painter = painterResource(id = R.drawable.img_toy_cars),
              contentDescription = "Toy Cars",
              modifier = Modifier.fillMaxSize(),
              contentScale = ContentScale.Crop
            )
            Surface(
              modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
              color = Color.Black.copy(alpha = 0.35f)
            ) {
              Text(
                text = "मस्त छोट्या गाड्या धावतात - पीप पीप हॉर्न वाजवा! 🚗💨",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(6.dp)
              )
            }
          }
        }
      }

      items(KidGameData.toyCars) { car ->
        CarCard(
          car = car,
          onTap = {
            soundEngine.playCarHorn()
            soundEngine.playNote(car.noteIndex)
            soundEngine.playGiggle()
            activeCarMessage = "${car.nameMr}: ${car.hornTextMr} 💨"
            onAddStar()
          }
        )
      }
    }
  }
}

@Composable
fun CarCard(
  car: ToyCarItem,
  onTap: () -> Unit
) {
  var isDriving by remember { mutableStateOf(false) }

  val offsetX by animateFloatAsState(
    targetValue = if (isDriving) 15f else 0f,
    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
    label = "carDrive"
  )

  val scale by animateFloatAsState(
    targetValue = if (isDriving) 1.2f else 1f,
    animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy),
    label = "carScale"
  )

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .scale(scale)
      .offset(x = offsetX.dp)
      .clickable {
        isDriving = true
        onTap()
      }
      .testTag("car_${car.id}"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    border = BorderStroke(2.dp, car.color.copy(alpha = 0.5f))
  ) {
    LaunchedEffect(isDriving) {
      if (isDriving) {
        delay(220)
        isDriving = false
      }
    }

    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Car Icon Circle
      Box(
        modifier = Modifier
          .size(72.dp)
          .clip(CircleShape)
          .background(car.color.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
      ) {
        Text(text = car.emoji, fontSize = 38.sp)
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = car.nameMr,
        fontSize = 14.sp,
        fontWeight = FontWeight.ExtraBold,
        color = KidOnSurface,
        textAlign = TextAlign.Center
      )

      Text(
        text = car.nameEn,
        fontSize = 11.sp,
        color = Color(0xFF78909C),
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(6.dp))

      // Horn badge
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = car.color.copy(alpha = 0.18f)
      ) {
        Text(
          text = car.hornTextMr,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = car.color,
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
      }
    }
  }
}
