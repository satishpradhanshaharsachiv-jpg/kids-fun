package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin
import kotlin.random.Random

/**
 * SoundEffectsEngine generates playful, pleasant, zero-latency musical notes,
 * cartoon laughs, pops, and animal/car sounds using 16-bit PCM AudioTrack.
 */
class SoundEffectsEngine(private val context: Context) {

  private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
  private val sampleRate = 44100

  // Pre-cached audio buffers for instant responsiveness on touch
  private val soundBuffers = mutableMapOf<String, ByteArray>()

  var isSoundEnabled: Boolean = true
  var isHapticEnabled: Boolean = true

  private val vibrator: Vibrator? by lazy {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
      vibratorManager?.defaultVibrator
    } else {
      @Suppress("DEPRECATION")
      context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }
  }

  init {
    // Pre-cache all sounds in background coroutine on startup
    scope.launch {
      // 8 Xylophone / Marimba musical notes (C5 to C6)
      val noteFrequencies = listOf(
        523.25, // C5 (Sa)
        587.33, // D5 (Re)
        659.25, // E5 (Ga)
        698.46, // F5 (Ma)
        783.99, // G5 (Pa)
        880.00, // A5 (Dha)
        987.77, // B5 (Ni)
        1046.50 // C6 (Sa')
      )
      noteFrequencies.forEachIndexed { index, freq ->
        soundBuffers["note_$index"] = generateMusicalTone(freq, 0.35)
      }

      // Fun cartoon & kid sounds
      soundBuffers["giggle"] = generateGiggleSound()
      soundBuffers["pop"] = generateBalloonPopSound()
      soundBuffers["bird"] = generateBirdChirpSound()
      soundBuffers["horn"] = generateCarHornSound()
      soundBuffers["bloom"] = generateSparkleChimeSound()
      soundBuffers["boing"] = generateBoingSound()
      soundBuffers["cheer"] = generateHappyMelody()
    }
  }

  fun playNote(index: Int) {
    if (!isSoundEnabled) return
    triggerHaptic(40)
    scope.launch {
      val key = "note_${index.coerceIn(0, 7)}"
      val pcm = soundBuffers[key] ?: generateMusicalTone(523.25 + (index * 65.0), 0.35)
      playPcmData(pcm)
    }
  }

  fun playGiggle() {
    if (!isSoundEnabled) return
    triggerHaptic(60)
    scope.launch {
      val pcm = soundBuffers["giggle"] ?: generateGiggleSound()
      playPcmData(pcm)
    }
  }

  fun playBalloonPop() {
    if (!isSoundEnabled) return
    triggerHaptic(50)
    scope.launch {
      val pcm = soundBuffers["pop"] ?: generateBalloonPopSound()
      playPcmData(pcm)
    }
  }

  fun playBirdChirp() {
    if (!isSoundEnabled) return
    triggerHaptic(40)
    scope.launch {
      val pcm = soundBuffers["bird"] ?: generateBirdChirpSound()
      playPcmData(pcm)
    }
  }

  fun playCarHorn() {
    if (!isSoundEnabled) return
    triggerHaptic(70)
    scope.launch {
      val pcm = soundBuffers["horn"] ?: generateCarHornSound()
      playPcmData(pcm)
    }
  }

  fun playFlowerBloom() {
    if (!isSoundEnabled) return
    triggerHaptic(45)
    scope.launch {
      val pcm = soundBuffers["bloom"] ?: generateSparkleChimeSound()
      playPcmData(pcm)
    }
  }

  fun playBoing() {
    if (!isSoundEnabled) return
    triggerHaptic(50)
    scope.launch {
      val pcm = soundBuffers["boing"] ?: generateBoingSound()
      playPcmData(pcm)
    }
  }

  fun playHappyMelody() {
    if (!isSoundEnabled) return
    triggerHaptic(80)
    scope.launch {
      val pcm = soundBuffers["cheer"] ?: generateHappyMelody()
      playPcmData(pcm)
    }
  }

  private fun triggerHaptic(durationMs: Long) {
    if (!isHapticEnabled) return
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator?.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
      } else {
        @Suppress("DEPRECATION")
        vibrator?.vibrate(durationMs)
      }
    } catch (_: Exception) {}
  }

  private fun playPcmData(pcm: ByteArray) {
    try {
      val track = AudioTrack.Builder()
        .setAudioAttributes(
          AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        )
        .setAudioFormat(
          AudioFormat.Builder()
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
            .setSampleRate(sampleRate)
            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
            .build()
        )
        .setBufferSizeInBytes(pcm.size)
        .setTransferMode(AudioTrack.MODE_STATIC)
        .build()

      track.write(pcm, 0, pcm.size)
      track.play()
      // Release after playback finishes
      scope.launch {
        kotlinx.coroutines.delay((pcm.size / 2 * 1000L / sampleRate) + 100)
        try {
          track.stop()
          track.release()
        } catch (_: Exception) {}
      }
    } catch (_: Exception) {}
  }

  // --- Audio Synthesis Methods ---

  /**
   * Warm marimba / xylophone tone with fundamental frequency and sweet second harmonic
   */
  private fun generateMusicalTone(frequency: Double, durationSeconds: Double): ByteArray {
    val numSamples = (durationSeconds * sampleRate).toInt()
    val pcm = ByteArray(numSamples * 2)

    for (i in 0 until numSamples) {
      val time = i.toDouble() / sampleRate
      // Envelope: fast 5ms attack, smooth exponential decay
      val attack = (time / 0.006).coerceAtMost(1.0)
      val decay = exp(-time * 7.5)
      val envelope = attack * decay

      // Fundamental + gentle octave harmonic
      val sampleValue = (
        sin(2.0 * PI * frequency * time) * 0.75 +
        sin(2.0 * PI * frequency * 2.0 * time) * 0.22 +
        sin(2.0 * PI * frequency * 3.0 * time) * 0.08
      ) * envelope

      val shortSample = (sampleValue * 28000).toInt().coerceIn(-32767, 32767).toShort()
      pcm[i * 2] = (shortSample.toInt() and 0xFF).toByte()
      pcm[i * 2 + 1] = ((shortSample.toInt() shr 8) and 0xFF).toByte()
    }
    return pcm
  }

  /**
   * Cheerful toddler giggle (हसण्याचा आवाज):
   * Rapid staccato vocal chuckles with bubbly pitch bend "hehehe-hahaha!"
   */
  private fun generateGiggleSound(): ByteArray {
    val durationSeconds = 0.65
    val numSamples = (durationSeconds * sampleRate).toInt()
    val pcm = ByteArray(numSamples * 2)

    val chuckleBursts = 5
    val burstDuration = durationSeconds / chuckleBursts

    for (i in 0 until numSamples) {
      val time = i.toDouble() / sampleRate
      val burstIndex = (time / burstDuration).toInt().coerceIn(0, chuckleBursts - 1)
      val timeInBurst = time - (burstIndex * burstDuration)

      // Envelope per chuckle burst
      val attack = (timeInBurst / 0.015).coerceAtMost(1.0)
      val decay = exp(-timeInBurst * 22.0)
      val burstEnvelope = attack * decay

      // Pitch rises playfully in each chuckle burst (550Hz up to 900Hz)
      val baseFreq = 580.0 + (burstIndex * 50.0)
      val freq = baseFreq + sin(timeInBurst * 35.0) * 80.0

      // Formant synthesis for cute giggle sound
      val wave = (
        sin(2.0 * PI * freq * time) * 0.65 +
        sin(2.0 * PI * freq * 1.5 * time) * 0.25 +
        sin(2.0 * PI * freq * 2.0 * time) * 0.10
      ) * burstEnvelope

      val shortSample = (wave * 26000).toInt().coerceIn(-32767, 32767).toShort()
      pcm[i * 2] = (shortSample.toInt() and 0xFF).toByte()
      pcm[i * 2 + 1] = ((shortSample.toInt() shr 8) and 0xFF).toByte()
    }
    return pcm
  }

  /**
   * Snappy balloon pop sound (फुगा फुटणे)
   */
  private fun generateBalloonPopSound(): ByteArray {
    val durationSeconds = 0.12
    val numSamples = (durationSeconds * sampleRate).toInt()
    val pcm = ByteArray(numSamples * 2)

    for (i in 0 until numSamples) {
      val time = i.toDouble() / sampleRate
      val decay = exp(-time * 42.0)
      // Rapid downward frequency sweep (600Hz down to 80Hz)
      val freq = 600.0 * exp(-time * 30.0) + 80.0
      val sine = sin(2.0 * PI * freq * time)
      // Slight noise component for the pop burst
      val noise = (Random.nextDouble(-1.0, 1.0) * 0.25)

      val wave = (sine * 0.75 + noise) * decay
      val shortSample = (wave * 30000).toInt().coerceIn(-32767, 32767).toShort()
      pcm[i * 2] = (shortSample.toInt() and 0xFF).toByte()
      pcm[i * 2 + 1] = ((shortSample.toInt() shr 8) and 0xFF).toByte()
    }
    return pcm
  }

  /**
   * Sweet bird chirp (चिव चिव)
   */
  private fun generateBirdChirpSound(): ByteArray {
    val durationSeconds = 0.35
    val numSamples = (durationSeconds * sampleRate).toInt()
    val pcm = ByteArray(numSamples * 2)

    for (i in 0 until numSamples) {
      val time = i.toDouble() / sampleRate
      // Two distinct chirps: 0.0 - 0.14s and 0.18 - 0.32s
      val chirpPhase = if (time < 0.15) time else (time - 0.17).coerceAtLeast(0.0)
      val isActive = (time < 0.13) || (time in 0.17..0.31)

      if (isActive) {
        val env = sin((chirpPhase / 0.14) * PI).coerceAtLeast(0.0)
        // Upward and downward melodic glide (2100Hz to 3100Hz)
        val freq = 2100.0 + sin((chirpPhase / 0.14) * PI) * 1100.0
        val wave = sin(2.0 * PI * freq * time) * env
        val shortSample = (wave * 24000).toInt().coerceIn(-32767, 32767).toShort()
        pcm[i * 2] = (shortSample.toInt() and 0xFF).toByte()
        pcm[i * 2 + 1] = ((shortSample.toInt() shr 8) and 0xFF).toByte()
      }
    }
    return pcm
  }

  /**
   * Toy car horn (बीप बीप)
   */
  private fun generateCarHornSound(): ByteArray {
    val durationSeconds = 0.36
    val numSamples = (durationSeconds * sampleRate).toInt()
    val pcm = ByteArray(numSamples * 2)

    for (i in 0 until numSamples) {
      val time = i.toDouble() / sampleRate
      // Double beep: 0.0 to 0.13s and 0.18 to 0.32s
      val isBeep = (time in 0.0..0.13) || (time in 0.18..0.32)
      if (isBeep) {
        val tBeep = if (time < 0.15) time else (time - 0.18)
        val env = (tBeep / 0.008).coerceAtMost(1.0) * exp(-tBeep * 6.0)
        // Friendly cartoon dual tone (440Hz + 554Hz)
        val wave = (
          sin(2.0 * PI * 440.0 * time) * 0.55 +
          sin(2.0 * PI * 554.37 * time) * 0.45
        ) * env

        val shortSample = (wave * 27000).toInt().coerceIn(-32767, 32767).toShort()
        pcm[i * 2] = (shortSample.toInt() and 0xFF).toByte()
        pcm[i * 2 + 1] = ((shortSample.toInt() shr 8) and 0xFF).toByte()
      }
    }
    return pcm
  }

  /**
   * Magical flower sparkle chime (जादुई फुलांचे संगीत)
   */
  private fun generateSparkleChimeSound(): ByteArray {
    val durationSeconds = 0.45
    val numSamples = (durationSeconds * sampleRate).toInt()
    val pcm = ByteArray(numSamples * 2)

    // Ascending arpeggio of 4 notes: C6, E6, G6, C7
    val notes = listOf(1046.5, 1318.5, 1567.98, 2093.0)
    val noteDuration = 0.09

    for (i in 0 until numSamples) {
      val time = i.toDouble() / sampleRate
      var totalWave = 0.0

      notes.forEachIndexed { idx, freq ->
        val noteStart = idx * noteDuration
        if (time >= noteStart) {
          val tNote = time - noteStart
          val env = (tNote / 0.005).coerceAtMost(1.0) * exp(-tNote * 12.0)
          totalWave += (sin(2.0 * PI * freq * time) * 0.4 + sin(2.0 * PI * freq * 2.0 * time) * 0.1) * env
        }
      }

      val shortSample = (totalWave * 26000).toInt().coerceIn(-32767, 32767).toShort()
      pcm[i * 2] = (shortSample.toInt() and 0xFF).toByte()
      pcm[i * 2 + 1] = ((shortSample.toInt() shr 8) and 0xFF).toByte()
    }
    return pcm
  }

  /**
   * Playful bouncy cartoon boing sound
   */
  private fun generateBoingSound(): ByteArray {
    val durationSeconds = 0.28
    val numSamples = (durationSeconds * sampleRate).toInt()
    val pcm = ByteArray(numSamples * 2)

    for (i in 0 until numSamples) {
      val time = i.toDouble() / sampleRate
      val env = (time / 0.01).coerceAtMost(1.0) * exp(-time * 7.0)
      // Upward sweeping pitch with wobble
      val baseFreq = 220.0 + (time / durationSeconds) * 450.0
      val wobble = sin(2.0 * PI * 24.0 * time) * 35.0
      val freq = baseFreq + wobble

      val wave = sin(2.0 * PI * freq * time) * env
      val shortSample = (wave * 28000).toInt().coerceIn(-32767, 32767).toShort()
      pcm[i * 2] = (shortSample.toInt() and 0xFF).toByte()
      pcm[i * 2 + 1] = ((shortSample.toInt() shr 8) and 0xFF).toByte()
    }
    return pcm
  }

  /**
   * Cheerful celebratory jingle (हर्षोल्हासाचे संगीत)
   */
  private fun generateHappyMelody(): ByteArray {
    val durationSeconds = 0.60
    val numSamples = (durationSeconds * sampleRate).toInt()
    val pcm = ByteArray(numSamples * 2)

    // C-E-G-C-E ascending cheerful chord
    val notes = listOf(523.25, 659.25, 783.99, 1046.5, 1318.5)
    val noteStep = 0.10

    for (i in 0 until numSamples) {
      val time = i.toDouble() / sampleRate
      var sample = 0.0

      notes.forEachIndexed { idx, freq ->
        val start = idx * noteStep
        if (time >= start) {
          val t = time - start
          val env = (t / 0.005).coerceAtMost(1.0) * exp(-t * 9.0)
          sample += (sin(2.0 * PI * freq * time) * 0.45 + sin(2.0 * PI * freq * 2.0 * time) * 0.15) * env
        }
      }

      val shortSample = (sample * 25000).toInt().coerceIn(-32767, 32767).toShort()
      pcm[i * 2] = (shortSample.toInt() and 0xFF).toByte()
      pcm[i * 2 + 1] = ((shortSample.toInt() shr 8) and 0xFF).toByte()
    }
    return pcm
  }
}
