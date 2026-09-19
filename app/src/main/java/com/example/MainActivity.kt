package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.audio.SoundEffectsEngine
import com.example.model.GameCategory
import com.example.ui.components.CategorySelector
import com.example.ui.components.GameTopBar
import com.example.ui.components.ParticleBurstEffect
import com.example.ui.components.PencilSketchDialog
import com.example.ui.screens.*
import com.example.ui.theme.KidBackgroundLight
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

  private lateinit var soundEngine: SoundEffectsEngine

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    soundEngine = SoundEffectsEngine(applicationContext)

    setContent {
      MyApplicationTheme {
        MainKidGameScreen(soundEngine = soundEngine)
      }
    }
  }
}

@Composable
fun MainKidGameScreen(soundEngine: SoundEffectsEngine) {
  var selectedCategory by remember { mutableStateOf(GameCategory.PLAYGROUND) }
  var starsCount by remember { mutableStateOf(5) }
  var isSketchDialogOpen by remember { mutableStateOf(false) }

  // Particle burst state
  var burstTrigger by remember { mutableStateOf(0) }
  var burstX by remember { mutableStateOf(300f) }
  var burstY by remember { mutableStateOf(400f) }
  var burstColor by remember { mutableStateOf(Color(0xFFFFD54F)) }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(KidBackgroundLight)
  ) {
    Scaffold(
      modifier = Modifier.fillMaxSize(),
      containerColor = Color.Transparent,
      topBar = {
        Column(modifier = Modifier.fillMaxWidth()) {
          GameTopBar(
            starsCount = starsCount,
            soundEngine = soundEngine,
            onOpenSketch = { isSketchDialogOpen = true },
            onGiggleClick = {
              starsCount += 2
              burstTrigger++
              burstX = 350f
              burstY = 200f
              burstColor = Color(0xFFFF4081)
            }
          )

          CategorySelector(
            selectedCategory = selectedCategory,
            onSelectCategory = { cat ->
              selectedCategory = cat
              soundEngine.playBoing()
            }
          )
        }
      }
    ) { innerPadding ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
      ) {
        AnimatedContent(
          targetState = selectedCategory,
          transitionSpec = {
            fadeIn() + slideInVertically { it / 6 } togetherWith fadeOut() + slideOutVertically { -it / 6 }
          },
          label = "categoryContent"
        ) { category ->
          when (category) {
            GameCategory.PLAYGROUND -> {
              PlaygroundView(
                soundEngine = soundEngine,
                onAddStar = { starsCount++ },
                onNavigateCategory = { cat -> selectedCategory = cat },
                onTriggerBurst = { x, y, col ->
                  burstTrigger++
                  burstX = x
                  burstY = y
                  burstColor = col
                }
              )
            }
            GameCategory.BALLOONS -> {
              BalloonsView(
                soundEngine = soundEngine,
                onAddStar = { starsCount++ },
                onTriggerBurst = { x, y, col ->
                  burstTrigger++
                  burstX = x
                  burstY = y
                  burstColor = col
                }
              )
            }
            GameCategory.FLOWERS -> {
              FlowersView(
                soundEngine = soundEngine,
                onAddStar = { starsCount++ }
              )
            }
            GameCategory.BIRDS -> {
              BirdsView(
                soundEngine = soundEngine,
                onAddStar = { starsCount++ }
              )
            }
            GameCategory.CARS -> {
              ToyCarsView(
                soundEngine = soundEngine,
                onAddStar = { starsCount++ }
              )
            }
          }
        }
      }
    }

    // Confetti / Particle burst on tap & pops
    ParticleBurstEffect(
      trigger = burstTrigger,
      originX = burstX,
      originY = burstY,
      baseColor = burstColor
    )

    // Pencil Sketch Blueprint dialog
    if (isSketchDialogOpen) {
      PencilSketchDialog(
        onDismiss = { isSketchDialogOpen = false }
      )
    }
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  androidx.compose.material3.Text(text = "Hello $name!", modifier = modifier)
}
