package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameCategory

@Composable
fun CategorySelector(
  selectedCategory: GameCategory,
  onSelectCategory: (GameCategory) -> Unit,
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()

  Row(
    modifier = modifier
      .fillMaxWidth()
      .horizontalScroll(scrollState)
      .padding(horizontal = 12.dp, vertical = 6.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    GameCategory.values().forEach { category ->
      val isSelected = category == selectedCategory

      val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "catScale"
      )

      val bgColor by animateColorAsState(
        targetValue = if (isSelected) category.badgeColor else Color.White,
        label = "catBg"
      )

      val textColor = if (isSelected) Color.White else Color(0xFF37474F)

      Surface(
        modifier = Modifier
          .scale(scale)
          .clickable { onSelectCategory(category) }
          .testTag("tab_${category.id}"),
        shape = RoundedCornerShape(20.dp),
        color = bgColor,
        shadowElevation = if (isSelected) 6.dp else 2.dp,
        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFE0E0E0))
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = category.iconEmoji, fontSize = 20.sp)
          Spacer(modifier = Modifier.width(6.dp))
          Column {
            Text(
              text = category.titleMr,
              fontSize = 13.sp,
              fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
              color = textColor
            )
            Text(
              text = category.titleEn,
              fontSize = 10.sp,
              color = if (isSelected) Color.White.copy(alpha = 0.85f) else Color(0xFF78909C)
            )
          }
        }
      }
    }
  }
}
