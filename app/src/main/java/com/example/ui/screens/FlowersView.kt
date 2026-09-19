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
import androidx.compose.ui.draw.rotate
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
import com.example.model.FlowerItem
import com.example.model.KidGameData
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FlowersView(
  soundEngine: SoundEffectsEngine,
  onAddStar: () -> Unit
) {
  val scope = rememberCoroutineScope()
  var lastPlayedFlower by remember { mutableStateOf<FlowerItem?>(null) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          listOf(Color(0xFFE8F5E9), Color(0xFFFFFDE7))
        )
      )
      .padding(horizontal = 12.dp)
  ) {
    // Musical Garden Banner
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp),
      shape = RoundedCornerShape(18.dp),
      color = Color.White.copy(alpha = 0.92f),
      shadowElevation = 3.dp,
      border = BorderStroke(1.5.dp, GrassGreen)
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
            text = "🌸 संगीतमय फुले (Musical Garden)",
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraBold,
            color = GrassGreenDark
          )
          Text(
            text = if (lastPlayedFlower != null)
              "${lastPlayedFlower!!.nameMr}: सूर ${lastPlayedFlower!!.musicalNoteMr} 🎵"
            else
              "फुलांवर टॅप करा आणि संगीत सूर ऐका! 🌸",
            fontSize = 12.sp,
            color = Color(0xFF546E7A),
            fontWeight = FontWeight.Medium
          )
        }

        // Play all melody button
        Button(
          onClick = {
            scope.launch {
              for (i in 0..7) {
                soundEngine.playNote(i)
                delay(140)
              }
              soundEngine.playFlowerBloom()
              onAddStar()
            }
          },
          shape = RoundedCornerShape(14.dp),
          colors = ButtonDefaults.buttonColors(containerColor = GrassGreen),
          contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
        ) {
          Text(text = "वाद्यवृंद 🎶", fontSize = 11.sp, fontWeight = FontWeight.Bold)
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
              painter = painterResource(id = R.drawable.img_flowers),
              contentDescription = "Flowers",
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
                text = "प्रत्येक फुलाचा वेगळा गोड संगीत सूर आहे! 🌸🎼",
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

      items(KidGameData.flowers) { flower ->
        FlowerCard(
          flower = flower,
          onBloom = {
            lastPlayedFlower = flower
            soundEngine.playNote(flower.noteIndex)
            soundEngine.playFlowerBloom()
            onAddStar()
          }
        )
      }
    }
  }
}

@Composable
fun FlowerCard(
  flower: FlowerItem,
  onBloom: () -> Unit
) {
  var isBlooming by remember { mutableStateOf(false) }

  val rotation by animateFloatAsState(
    targetValue = if (isBlooming) 45f else 0f,
    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
    label = "flowerRotate"
  )

  val scale by animateFloatAsState(
    targetValue = if (isBlooming) 1.25f else 1f,
    animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy),
    label = "flowerScale"
  )

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .scale(scale)
      .clickable {
        isBlooming = true
        onBloom()
      }
      .testTag("flower_${flower.id}"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    border = BorderStroke(2.dp, flower.color.copy(alpha = 0.5f))
  ) {
    LaunchedEffect(isBlooming) {
      if (isBlooming) {
        delay(220)
        isBlooming = false
      }
    }

    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Flower Icon Box
      Box(
        modifier = Modifier
          .size(72.dp)
          .clip(CircleShape)
          .background(flower.color.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = flower.emoji,
          fontSize = 38.sp,
          modifier = Modifier.rotate(rotation)
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = flower.nameMr,
        fontSize = 14.sp,
        fontWeight = FontWeight.ExtraBold,
        color = KidOnSurface,
        textAlign = TextAlign.Center
      )

      Text(
        text = flower.nameEn,
        fontSize = 11.sp,
        color = Color(0xFF78909C),
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(6.dp))

      // Musical note indicator (सा, रे, ग, म / C, D, E, F)
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = flower.color.copy(alpha = 0.2f)
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "🎵", fontSize = 11.sp)
          Spacer(modifier = Modifier.width(3.dp))
          Text(
            text = "${flower.musicalNoteMr} (${flower.musicalNoteName})",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = flower.color
          )
        }
      }
    }
  }
}
