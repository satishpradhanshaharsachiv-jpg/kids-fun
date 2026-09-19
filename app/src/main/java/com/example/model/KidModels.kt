package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.R
import com.example.ui.theme.*

enum class GameCategory(
  val id: String,
  val titleMr: String,
  val titleEn: String,
  val iconEmoji: String,
  val badgeColor: Color
) {
  PLAYGROUND("all", "सर्व खेळ", "Playground", "🎡", SunnyYellow),
  BALLOONS("balloons", "फुगे", "Balloons", "🎈", BubblePink),
  FLOWERS("flowers", "फुले", "Flowers", "🌸", GrassGreen),
  BIRDS("birds", "पक्षी", "Birds", "🐦", SkyBlue),
  CARS("cars", "गाड्या", "Toy Cars", "🚗", TangerineOrange)
}

data class BalloonItem(
  val id: Int,
  val nameMr: String,
  val nameEn: String,
  val color: Color,
  val emoji: String,
  val soundNoteIndex: Int
)

data class FlowerItem(
  val id: Int,
  val nameMr: String,
  val nameEn: String,
  val color: Color,
  val emoji: String,
  val musicalNoteName: String,
  val musicalNoteMr: String,
  val noteIndex: Int
)

data class BirdItem(
  val id: Int,
  val nameMr: String,
  val nameEn: String,
  val color: Color,
  val emoji: String,
  val callSoundMr: String,
  val noteIndex: Int
)

data class ToyCarItem(
  val id: Int,
  val nameMr: String,
  val nameEn: String,
  val color: Color,
  val emoji: String,
  val hornTextMr: String,
  val noteIndex: Int
)

object KidGameData {
  val balloons = listOf(
    BalloonItem(1, "लाल फुगा", "Red Balloon", Color(0xFFFF3366), "🎈", 0),
    BalloonItem(2, "पिवळा फुगा", "Yellow Balloon", Color(0xFFFFD600), "🎈", 1),
    BalloonItem(3, "निळा फुगा", "Blue Balloon", Color(0xFF29B6F6), "🎈", 2),
    BalloonItem(4, "हिरवा फुगा", "Green Balloon", Color(0xFF00E676), "🎈", 3),
    BalloonItem(5, "गुलाबी फुगा", "Pink Balloon", Color(0xFFFF4081), "🎈", 4),
    BalloonItem(6, "जांभळा फुगा", "Purple Balloon", Color(0xFFAB47BC), "🎈", 5),
    BalloonItem(7, "केशरी फुगा", "Orange Balloon", Color(0xFFFF9100), "🎈", 6),
    BalloonItem(8, "सोनेरी फुगा", "Golden Balloon", Color(0xFFFFAB00), "🎈", 7)
  )

  val flowers = listOf(
    FlowerItem(1, "गुलाब", "Rose", Color(0xFFE91E63), "🌹", "C (Do)", "सा", 0),
    FlowerItem(2, "सूर्यफूल", "Sunflower", Color(0xFFFFC107), "🌻", "D (Re)", "रे", 1),
    FlowerItem(3, "कमळ", "Lotus", Color(0xFFFF80AB), "🪷", "E (Mi)", "ग", 2),
    FlowerItem(4, "चाफा / मोगरा", "Jasmine", Color(0xFF81C784), "🌼", "F (Fa)", "म", 3),
    FlowerItem(5, "झेंडू", "Marigold", Color(0xFFFF9800), "🏵️", "G (Sol)", "प", 4),
    FlowerItem(6, "ट्यूलिप", "Tulip", Color(0xFFAB47BC), "🌷", "A (La)", "ध", 5),
    FlowerItem(7, "जास्वंद", "Hibiscus", Color(0xFFFF5252), "🌺", "B (Ti)", "नी", 6),
    FlowerItem(8, "चेरी ब्लॉसम", "Blossom", Color(0xFFF48FB1), "🌸", "C' (High Do)", "सां", 7)
  )

  val birds = listOf(
    BirdItem(1, "निळा पक्षी", "Bluebird", Color(0xFF29B6F6), "🐦", "चिव चिव!", 0),
    BirdItem(2, "चिमणी", "Sparrow", Color(0xFF8D6E63), "🐤", "चीं चीं चीं!", 1),
    BirdItem(3, "पोपट", "Parrot", Color(0xFF4CAF50), "🦜", "मीठू मीठू!", 2),
    BirdItem(4, "कबूतर", "Pigeon", Color(0xFF90A4AE), "🕊️", "गुटर गूं!", 3),
    BirdItem(5, "मोर", "Peacock", Color(0xFF009688), "🦚", "म्याँव म्याँव!", 4),
    BirdItem(6, "बदक", "Duckling", Color(0xFFFFD54F), "🦆", "क्वॅक क्वॅक!", 5),
    BirdItem(7, "घुबड", "Little Owl", Color(0xFF795548), "🦉", "हू हू!", 6),
    BirdItem(8, "कोकीळ", "Songbird", Color(0xFF5C6BC0), "🎶", "कुहू कुहू!", 7)
  )

  val toyCars = listOf(
    ToyCarItem(1, "लाल कार", "Red Racecar", Color(0xFFFF1744), "🚗", "पीप पीप!", 0),
    ToyCarItem(2, "पिवळी बस", "School Bus", Color(0xFFFFC107), "🚌", "पॉम्प पॉम्प!", 1),
    ToyCarItem(3, "निळी गाडी", "Blue Cruiser", Color(0xFF1E88E5), "🚙", "टूट टूट!", 2),
    ToyCarItem(4, "अग्निशामक दल", "Fire Truck", Color(0xFFD50000), "🚒", "टाटाँ टाटाँ!", 3),
    ToyCarItem(5, "पोलीस गाडी", "Police Car", Color(0xFF0D47A1), "🚓", "वी-वू वी-वू!", 4),
    ToyCarItem(6, "छोटा ट्रक", "Pickup Truck", Color(0xFF43A047), "🛻", "भ्रूम भ्रूम!", 5),
    ToyCarItem(7, "आईस्क्रीम व्हॅन", "Ice Cream Van", Color(0xFFFF4081), "🚐", "छान ट्यून!", 6),
    ToyCarItem(8, "रिक्षा / ऑटो", "Auto TukTuk", Color(0xFFFF9800), "🛺", "हॉर्न वाजला!", 7)
  )
}
