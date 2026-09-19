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
import com.example.model.BalloonItem
import com.example.model.KidGameData
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun BalloonsView(
  soundEngine: SoundEffectsEngine,
  onAddStar: () -> Unit,
  onTriggerBurst: (Float, Float, Color) -> Unit
) {
  var poppedCount by remember { mutableStateOf(0) }
  var activeMessage by remember { mutableStateOf("फुग्यावर टॅप करा आणि फोडा! 🎈💥") }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          listOf(Color(0xFFE1F5FE), Color(0xFFFFFDE7))
        )
      )
      .padding(horizontal = 12.dp)
  ) {
    // Score & Pop banner
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp),
      shape = RoundedCornerShape(18.dp),
      color = Color.White.copy(alpha = 0.92f),
      shadowElevation = 3.dp,
      border = BorderStroke(1.5.dp, BubblePink)
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
            text = "🎈 फुगे फोडणे (Balloon Pop)",
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraBold,
            color = BubblePink
          )
          Text(
            text = activeMessage,
            fontSize = 12.sp,
            color = Color(0xFF546E7A),
            fontWeight = FontWeight.Medium
          )
        }

        Surface(
          shape = RoundedCornerShape(14.dp),
          color = BubblePinkLight
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = "💥", fontSize = 16.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "$poppedCount",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = BubblePink
            )
          }
        }
      }
    }

    // Interactive Balloons Grid
    LazyVerticalGrid(
      columns = GridCells.Fixed(2),
      contentPadding = PaddingValues(bottom = 24.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp),
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      modifier = Modifier.fillMaxSize()
    ) {
      // Top Hero Image Banner
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
              painter = painterResource(id = R.drawable.img_balloons),
              contentDescription = "Balloons",
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
                text = "रंगीबेरंगी फुगे - टॅप करा आणि हसण्याचा आवाज ऐका!",
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

      items(KidGameData.balloons) { balloon ->
        BalloonCard(
          balloon = balloon,
          onPop = {
            soundEngine.playBalloonPop()
            soundEngine.playNote(balloon.soundNoteIndex)
            // Extra giggle chance
            if (poppedCount % 2 == 0) {
              soundEngine.playGiggle()
              activeMessage = "${balloon.nameMr} फुटला! ही-ही-हा-हा! 😄"
            } else {
              activeMessage = "${balloon.nameMr} फुटला! पॉप! 🎈"
            }
            poppedCount++
            onAddStar()
            onTriggerBurst(200f, 400f, balloon.color)
          }
        )
      }
    }
  }
}

@Composable
fun BalloonCard(
  balloon: BalloonItem,
  onPop: () -> Unit
) {
  var isPopping by remember { mutableStateOf(false) }

  // Gentle floating animation
  val infiniteTransition = rememberInfiniteTransition(label = "balloonFloat")
  val offsetY by infiniteTransition.animateFloat(
    initialValue = -5f,
    targetValue = 5f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200 + (balloon.id * 150), easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "floatY"
  )

  val scale by animateFloatAsState(
    targetValue = if (isPopping) 1.25f else 1f,
    animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy),
    label = "popScale"
  )

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .offset(y = offsetY.dp)
      .scale(scale)
      .clickable {
        isPopping = true
        onPop()
      }
      .testTag("balloon_${balloon.id}"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    border = BorderStroke(2.dp, balloon.color.copy(alpha = 0.5f))
  ) {
    LaunchedEffect(isPopping) {
      if (isPopping) {
        delay(200)
        isPopping = false
      }
    }

    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Balloon Circle Visual
      Box(
        modifier = Modifier
          .size(72.dp)
          .clip(CircleShape)
          .background(
            Brush.radialGradient(
              colors = listOf(
                balloon.color.copy(alpha = 0.65f),
                balloon.color
              )
            )
          ),
        contentAlignment = Alignment.Center
      ) {
        Text(text = "🎈", fontSize = 36.sp)
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = balloon.nameMr,
        fontSize = 14.sp,
        fontWeight = FontWeight.ExtraBold,
        color = KidOnSurface,
        textAlign = TextAlign.Center
      )

      Text(
        text = balloon.nameEn,
        fontSize = 11.sp,
        color = Color(0xFF78909C),
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(4.dp))

      Surface(
        shape = RoundedCornerShape(10.dp),
        color = balloon.color.copy(alpha = 0.15f)
      ) {
        Text(
          text = "टॅप करा 💥",
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          color = balloon.color,
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
      }
    }
  }
}
