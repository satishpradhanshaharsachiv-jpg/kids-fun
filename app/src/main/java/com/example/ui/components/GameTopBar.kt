package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.SoundEffectsEngine
import com.example.ui.theme.*

@Composable
fun GameTopBar(
  starsCount: Int,
  soundEngine: SoundEffectsEngine,
  onOpenSketch: () -> Unit,
  onGiggleClick: () -> Unit
) {
  var isMuted by remember { mutableStateOf(!soundEngine.isSoundEnabled) }

  // Gentle pulse for the giggle button
  val infiniteTransition = rememberInfiniteTransition(label = "gigglePulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 1f,
    targetValue = 1.08f,
    animationSpec = infiniteRepeatable(
      animation = tween(800, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse"
  )

  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .statusBarsPadding()
      .padding(horizontal = 12.dp, vertical = 6.dp),
    shape = RoundedCornerShape(22.dp),
    color = Color.White.copy(alpha = 0.95f),
    shadowElevation = 6.dp,
    border = androidx.compose.foundation.BorderStroke(2.dp, SunnyYellow)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      // App Title with playful badge
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.weight(1f)
      ) {
        Box(
          modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(
              Brush.linearGradient(listOf(SunnyYellow, TangerineOrange))
            ),
          contentAlignment = Alignment.Center
        ) {
          Text(text = "🎈", fontSize = 22.sp)
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column {
          Text(
            text = "बाल खेळ",
            fontSize = 17.sp,
            fontWeight = FontWeight.ExtraBold,
            color = KidOnSurface
          )
          Text(
            text = "Kids Fun Play",
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF78909C)
          )
        }
      }

      // Action buttons: Stars, Giggle Button, Sketch Blueprint, Sound Toggle
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        // Star Counter Badge
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = SunnyYellowSoft,
          border = androidx.compose.foundation.BorderStroke(1.dp, SunnyYellowDark)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = "⭐", fontSize = 14.sp)
            Spacer(modifier = Modifier.width(3.dp))
            Text(
              text = "$starsCount",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFFE65100)
            )
          }
        }

        // Laugh / Giggle Button (हसण्याचा आवाज)
        Button(
          onClick = {
            soundEngine.playGiggle()
            onGiggleClick()
          },
          modifier = Modifier
            .scale(pulseScale)
            .height(38.dp)
            .testTag("giggle_button"),
          shape = RoundedCornerShape(18.dp),
          colors = ButtonDefaults.buttonColors(containerColor = BubblePink),
          contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "😄", fontSize = 16.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "हसू!",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          }
        }

        // Pencil Sketch Blueprint Dialog Button (कागदावरील स्केच)
        IconButton(
          onClick = onOpenSketch,
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(SkyBlueLight)
            .testTag("pencil_sketch_button")
        ) {
          Icon(
            imageVector = Icons.Default.Create,
            contentDescription = "Pencil Sketch",
            tint = SkyBlueDark,
            modifier = Modifier.size(18.dp)
          )
        }

        // Sound Mute/Unmute Toggle
        IconButton(
          onClick = {
            soundEngine.isSoundEnabled = !soundEngine.isSoundEnabled
            isMuted = !soundEngine.isSoundEnabled
          },
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(GrassGreenLight)
            .testTag("sound_toggle_button")
        ) {
          Icon(
            imageVector = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
            contentDescription = if (isMuted) "Unmute" else "Mute",
            tint = if (isMuted) Color.Gray else GrassGreenDark,
            modifier = Modifier.size(18.dp)
          )
        }
      }
    }
  }
}
