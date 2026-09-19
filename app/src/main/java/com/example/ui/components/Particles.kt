package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

data class Particle(
  val x: Float,
  val y: Float,
  val vx: Float,
  val vy: Float,
  val color: Color,
  val size: Float,
  val shape: Int // 0: circle, 1: star, 2: square
)

@Composable
fun ParticleBurstEffect(
  trigger: Int,
  originX: Float,
  originY: Float,
  baseColor: Color,
  modifier: Modifier = Modifier
) {
  if (trigger <= 0) return

  val progress = remember(trigger) { Animatable(0f) }
  val particles = remember(trigger) {
    val list = mutableListOf<Particle>()
    val colors = listOf(
      baseColor,
      Color(0xFFFFD54F),
      Color(0xFFFF4081),
      Color(0xFF29B6F6),
      Color(0xFF66BB6A),
      Color(0xFFFF9100)
    )
    for (i in 0..24) {
      val angle = Random.nextFloat() * 2f * Math.PI.toFloat()
      val speed = Random.nextFloat() * 380f + 120f
      list.add(
        Particle(
          x = originX,
          y = originY,
          vx = cos(angle) * speed,
          vy = sin(angle) * speed,
          color = colors[Random.nextInt(colors.size)],
          size = Random.nextFloat() * 14f + 8f,
          shape = Random.nextInt(3)
        )
      )
    }
    list
  }

  LaunchedEffect(trigger) {
    progress.snapTo(0f)
    progress.animateTo(
      targetValue = 1f,
      animationSpec = tween(durationMillis = 650, easing = LinearEasing)
    )
  }

  if (progress.value < 1f) {
    Canvas(modifier = modifier.fillMaxSize()) {
      val t = progress.value
      val alpha = (1f - t).coerceIn(0f, 1f)

      particles.forEach { p ->
        // Add slight gravity pull to vy
        val px = p.x + p.vx * t
        val py = p.y + p.vy * t + (250f * t * t)

        when (p.shape) {
          0 -> {
            drawCircle(
              color = p.color.copy(alpha = alpha),
              radius = p.size * (1f - t * 0.4f),
              center = Offset(px, py)
            )
          }
          1 -> {
            // Star or diamond
            drawCircle(
              color = p.color.copy(alpha = alpha),
              radius = (p.size * 0.8f) * (1f - t * 0.3f),
              center = Offset(px, py)
            )
          }
          else -> {
            // Little confetti square
            drawRect(
              color = p.color.copy(alpha = alpha),
              topLeft = Offset(px - p.size / 2, py - p.size / 2),
              size = androidx.compose.ui.geometry.Size(p.size * (1f - t * 0.3f), p.size * (1f - t * 0.3f))
            )
          }
        }
      }
    }
  }
}
