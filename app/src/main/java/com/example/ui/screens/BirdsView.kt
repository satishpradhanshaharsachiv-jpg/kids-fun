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
import com.example.model.BirdItem
import com.example.model.KidGameData
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun BirdsView(
  soundEngine: SoundEffectsEngine,
  onAddStar: () -> Unit
) {
  var activeBirdCall by remember { mutableStateOf("पक्ष्यांवर टॅप करा आणि चिवचिव ऐका! 🐦🎶") }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          listOf(Color(0xFFE1F5FE), Color(0xFFF1F8E9))
        )
      )
      .padding(horizontal = 12.dp)
  ) {
    // Header Banner
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp),
      shape = RoundedCornerShape(18.dp),
      color = Color.White.copy(alpha = 0.92f),
      shadowElevation = 3.dp,
      border = BorderStroke(1.5.dp, SkyBlue)
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
            text = "🐦 गाणारे पक्षी (Singing Birds)",
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraBold,
            color = SkyBlueDark
          )
          Text(
            text = activeBirdCall,
            fontSize = 12.sp,
            color = Color(0xFF546E7A),
            fontWeight = FontWeight.Medium
          )
        }

        IconButton(
          onClick = {
            soundEngine.playBirdChirp()
            soundEngine.playGiggle()
            activeBirdCall = "सगळे पक्षी एकत्र गात आहेत! चिव चिव! 🐦❤️"
            onAddStar()
          },
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(SkyBlueLight)
        ) {
          Text(text = "🎶", fontSize = 18.sp)
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
              painter = painterResource(id = R.drawable.img_birds),
              contentDescription = "Birds",
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
                text = "सुंदर छोटे पक्षी फांदीवर बसून गाणी गातात! 🐦🌳",
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

      items(KidGameData.birds) { bird ->
        BirdCard(
          bird = bird,
          onTap = {
            soundEngine.playBirdChirp()
            soundEngine.playNote(bird.noteIndex)
            soundEngine.playGiggle()
            activeBirdCall = "${bird.nameMr}: ${bird.callSoundMr} 😄"
            onAddStar()
          }
        )
      }
    }
  }
}

@Composable
fun BirdCard(
  bird: BirdItem,
  onTap: () -> Unit
) {
  var isFlapping by remember { mutableStateOf(false) }

  val rotation by animateFloatAsState(
    targetValue = if (isFlapping) -18f else 0f,
    animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy),
    label = "birdFlap"
  )

  val scale by animateFloatAsState(
    targetValue = if (isFlapping) 1.22f else 1f,
    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
    label = "birdScale"
  )

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .scale(scale)
      .clickable {
        isFlapping = true
        onTap()
      }
      .testTag("bird_${bird.id}"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    border = BorderStroke(2.dp, bird.color.copy(alpha = 0.5f))
  ) {
    LaunchedEffect(isFlapping) {
      if (isFlapping) {
        delay(250)
        isFlapping = false
      }
    }

    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Bird Icon in Circle
      Box(
        modifier = Modifier
          .size(72.dp)
          .clip(CircleShape)
          .background(bird.color.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = bird.emoji,
          fontSize = 38.sp,
          modifier = Modifier.rotate(rotation)
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = bird.nameMr,
        fontSize = 14.sp,
        fontWeight = FontWeight.ExtraBold,
        color = KidOnSurface,
        textAlign = TextAlign.Center
      )

      Text(
        text = bird.nameEn,
        fontSize = 11.sp,
        color = Color(0xFF78909C),
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(6.dp))

      // Sound call badge ("चिव चिव!", "मीठू मीठू!")
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = bird.color.copy(alpha = 0.18f)
      ) {
        Text(
          text = bird.callSoundMr,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = bird.color,
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
      }
    }
  }
}
