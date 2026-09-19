package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import com.example.model.GameCategory
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PlaygroundView(
  soundEngine: SoundEffectsEngine,
  onAddStar: () -> Unit,
  onNavigateCategory: (GameCategory) -> Unit,
  onTriggerBurst: (Float, Float, Color) -> Unit
) {
  val coroutineScope = rememberCoroutineScope()
  var subtitleMessage by remember { mutableStateOf("खेळण्यांवर टॅप करा आणि मजा बघा! 🎈🌸🐦🚗") }

  // Animated bouncing sun
  val infiniteTransition = rememberInfiniteTransition(label = "sunAnim")
  val sunRotation by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(
      animation = tween(20000, easing = LinearEasing)
    ),
    label = "sunRot"
  )

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(bottom = 24.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Dynamic Feedback Banner for Kids
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 14.dp, vertical = 6.dp),
      shape = RoundedCornerShape(16.dp),
      color = SunnyYellowSoft,
      border = BorderStroke(1.5.dp, SunnyYellowDark)
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(text = "✨", fontSize = 18.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = subtitleMessage,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF5D4037)
        )
      }
    }

    // 4 Visual Hero Cards (The 4 core themes from the user prompt)
    Text(
      text = "🎨 रंगीबेरंगी चित्रे (Tap to Explore)",
      fontSize = 16.sp,
      fontWeight = FontWeight.ExtraBold,
      color = Color(0xFF37474F),
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
    )

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      // Balloon Hero Card
      PlaygroundHeroCard(
        imageRes = R.drawable.img_balloons,
        titleMr = "फुगे",
        titleEn = "Balloons",
        emoji = "🎈",
        accentColor = BubblePink,
        modifier = Modifier.weight(1f),
        onClick = {
          soundEngine.playBalloonPop()
          soundEngine.playGiggle()
          onAddStar()
          subtitleMessage = "फुगा फुटला! ही-ही-हा-हा! 🎈✨"
          onNavigateCategory(GameCategory.BALLOONS)
        }
      )

      // Flower Hero Card
      PlaygroundHeroCard(
        imageRes = R.drawable.img_flowers,
        titleMr = "फुले",
        titleEn = "Flowers",
        emoji = "🌸",
        accentColor = GrassGreen,
        modifier = Modifier.weight(1f),
        onClick = {
          soundEngine.playFlowerBloom()
          soundEngine.playNote(2)
          onAddStar()
          subtitleMessage = "सुंदर फुले उमलली! 🌸🎶"
          onNavigateCategory(GameCategory.FLOWERS)
        }
      )
    }

    Spacer(modifier = Modifier.height(10.dp))

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      // Birds Hero Card
      PlaygroundHeroCard(
        imageRes = R.drawable.img_birds,
        titleMr = "पक्षी",
        titleEn = "Birds",
        emoji = "🐦",
        accentColor = SkyBlue,
        modifier = Modifier.weight(1f),
        onClick = {
          soundEngine.playBirdChirp()
          soundEngine.playNote(4)
          onAddStar()
          subtitleMessage = "पक्षी गोड गाणे गातोय! चिव चिव! 🐦🎶"
          onNavigateCategory(GameCategory.BIRDS)
        }
      )

      // Toy Cars Hero Card
      PlaygroundHeroCard(
        imageRes = R.drawable.img_toy_cars,
        titleMr = "छोट्या गाड्या",
        titleEn = "Toy Cars",
        emoji = "🚗",
        accentColor = TangerineOrange,
        modifier = Modifier.weight(1f),
        onClick = {
          soundEngine.playCarHorn()
          onAddStar()
          subtitleMessage = "गाडी धावली! पीप पीप! 🚗💨"
          onNavigateCategory(GameCategory.CARS)
        }
      )
    }

    Spacer(modifier = Modifier.height(18.dp))

    // Interactive Toddler Sound Playground Board
    Text(
      text = "🎶 गमतीदार आवाज खेळा (Touch & Play Toys)",
      fontSize = 16.sp,
      fontWeight = FontWeight.ExtraBold,
      color = Color(0xFF37474F),
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, bottom = 6.dp)
    )

    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
      border = BorderStroke(1.5.dp, SkyBlueLight)
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        // Quick interactive toy grid
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceAround
        ) {
          InteractiveToyButton(
            emoji = "🎈",
            title = "फुगा",
            soundDesc = "पॉप!",
            bgColor = BubblePinkLight,
            borderColor = BubblePink,
            onClick = {
              soundEngine.playBalloonPop()
              onAddStar()
              subtitleMessage = "फुगा फुटला! पॉप! 🎈"
            }
          )

          InteractiveToyButton(
            emoji = "😄",
            title = "हसू",
            soundDesc = "खिलखिलाट!",
            bgColor = SunnyYellowSoft,
            borderColor = SunnyYellowDark,
            onClick = {
              soundEngine.playGiggle()
              onAddStar()
              subtitleMessage = "बाळाचे हसू! ही-ही-हा-हा! 😄❤️"
            }
          )

          InteractiveToyButton(
            emoji = "🌸",
            title = "फूल",
            soundDesc = "संगीत सूर",
            bgColor = GrassGreenLight,
            borderColor = GrassGreen,
            onClick = {
              soundEngine.playFlowerBloom()
              onAddStar()
              subtitleMessage = "सुंदर फूल डोलले! 🌸🎵"
            }
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceAround
        ) {
          InteractiveToyButton(
            emoji = "🐦",
            title = "पक्षी",
            soundDesc = "चिव चिव!",
            bgColor = SkyBlueLight,
            borderColor = SkyBlueDark,
            onClick = {
              soundEngine.playBirdChirp()
              onAddStar()
              subtitleMessage = "पक्षी म्हणाला: चिव चिव चिव! 🐦"
            }
          )

          InteractiveToyButton(
            emoji = "🚗",
            title = "कार",
            soundDesc = "पीप पीप!",
            bgColor = OrangeLight,
            borderColor = TangerineOrange,
            onClick = {
              soundEngine.playCarHorn()
              onAddStar()
              subtitleMessage = "गाडीचा हॉर्न: पीप पीप! 🚗💨"
            }
          )

          InteractiveToyButton(
            emoji = "🎵",
            title = "संगीत",
            soundDesc = "छान सूर",
            bgColor = PurpleLight,
            borderColor = LavenderPurple,
            onClick = {
              soundEngine.playHappyMelody()
              onAddStar()
              subtitleMessage = "मस्त संगीत वाजले! 🎶⭐"
            }
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Big Cheerful Jingle Banner
    Button(
      onClick = {
        soundEngine.playHappyMelody()
        soundEngine.playGiggle()
        onAddStar()
        subtitleMessage = "सर्व मित्रांनी मिळून गाणे गायले! 🎉😄"
      },
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 14.dp)
        .height(54.dp),
      shape = RoundedCornerShape(26.dp),
      colors = ButtonDefaults.buttonColors(containerColor = SunnyYellowDark),
      elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "🎉", fontSize = 22.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "छान गाणे व हसू वाजवा! (Fun Melody)",
          fontSize = 15.sp,
          fontWeight = FontWeight.ExtraBold,
          color = Color.White
        )
      }
    }
  }
}

@Composable
fun PlaygroundHeroCard(
  imageRes: Int,
  titleMr: String,
  titleEn: String,
  emoji: String,
  accentColor: Color,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  var isPressed by remember { mutableStateOf(false) }
  val scale by animateFloatAsState(
    targetValue = if (isPressed) 0.95f else 1f,
    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
    label = "heroCardScale"
  )

  Card(
    modifier = modifier
      .scale(scale)
      .clickable {
        isPressed = true
        onClick()
      }
      .testTag("hero_card_$titleEn"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
    border = BorderStroke(2.dp, accentColor.copy(alpha = 0.6f))
  ) {
    LaunchedEffect(isPressed) {
      if (isPressed) {
        delay(150)
        isPressed = false
      }
    }

    Column(
      modifier = Modifier.padding(10.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Illustrated Image
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(1.1f)
          .clip(RoundedCornerShape(14.dp))
          .background(accentColor.copy(alpha = 0.1f))
      ) {
        Image(
          painter = painterResource(id = imageRes),
          contentDescription = titleMr,
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop
        )

        // Emoji badge overlay
        Surface(
          shape = CircleShape,
          color = Color.White.copy(alpha = 0.9f),
          modifier = Modifier
            .padding(6.dp)
            .size(32.dp)
            .align(Alignment.TopEnd),
          shadowElevation = 2.dp
        ) {
          Box(contentAlignment = Alignment.Center) {
            Text(text = emoji, fontSize = 16.sp)
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = titleMr,
        fontSize = 15.sp,
        fontWeight = FontWeight.ExtraBold,
        color = KidOnSurface,
        textAlign = TextAlign.Center
      )

      Text(
        text = titleEn,
        fontSize = 11.sp,
        color = Color(0xFF78909C),
        textAlign = TextAlign.Center
      )
    }
  }
}

@Composable
fun InteractiveToyButton(
  emoji: String,
  title: String,
  soundDesc: String,
  bgColor: Color,
  borderColor: Color,
  onClick: () -> Unit
) {
  var isTapped by remember { mutableStateOf(false) }
  val scale by animateFloatAsState(
    targetValue = if (isTapped) 0.88f else 1f,
    animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy),
    label = "toyScale"
  )

  LaunchedEffect(isTapped) {
    if (isTapped) {
      delay(120)
      isTapped = false
    }
  }

  Surface(
    modifier = Modifier
      .scale(scale)
      .size(width = 95.dp, height = 90.dp)
      .clickable {
        isTapped = true
        onClick()
      },
    shape = RoundedCornerShape(18.dp),
    color = bgColor,
    border = BorderStroke(1.5.dp, borderColor),
    shadowElevation = 2.dp
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(6.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Text(text = emoji, fontSize = 28.sp)
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = title,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = KidOnSurface
      )
      Text(
        text = soundDesc,
        fontSize = 9.sp,
        color = borderColor,
        fontWeight = FontWeight.SemiBold
      )
    }
  }
}
