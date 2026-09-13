package com.app_rickmorty.ui.screens



import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app_rickmorty.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun WelcomeScreen(
    onIniciar: () -> Unit,
    onConfiguracion: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SpaceBackgroundBrush)
    ) {
        // Fondo de "constelaciones" (líneas + puntos aleatorios)
        StarFieldBackground(modifier = Modifier.fillMaxSize())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Badge superior "MULTIVERSE ACCESS ENABLED"
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(PanelDark.copy(alpha = 0.6f))
                    .border(BorderStroke(1.dp, CyanAccent.copy(alpha = 0.6f)), RoundedCornerShape(50))
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "MULTIVERSE ACCESS ENABLED",
                    color = CyanAccent,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Portal / vórtice central
            PortalVortex(size = 220.dp)

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "BIENVENIDO A",
                color = CyanAccent,
                fontSize = 13.sp,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "RICK & MORTY",
                color = NeonGreen,
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botón principal INICIAR
            Button(
                onClick = onIniciar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonGreen,
                    contentColor = Color.Black
                )
            ) {
                Text(text = "INICIAR", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón secundario Configuración
            OutlinedButton(
                onClick = onConfiguracion,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                border = BorderStroke(1.dp, BorderSubtle),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary)
            ) {
                Icon(Icons.Filled.Settings, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Configuración", fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/** Anillo cian con espiral verde tipo "vórtice" (placeholder vectorial del portal). */
@Composable
private fun PortalVortex(size: androidx.compose.ui.unit.Dp) {
    Box(
        modifier = Modifier
            .size(size)
            .background(PortalGlowBrush, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .size(size * 0.82f)
                .clip(CircleShape)
                .background(Color.Black)
                .border(BorderStroke(2.dp, CyanAccent), CircleShape)
        ) {
            val strokeCount = 40
            for (i in 0 until strokeCount) {
                val angle = (i * (360f / strokeCount))
                rotate(degrees = angle, pivot = center) {
                    val len = size.toPx() * (0.15f + Random.nextFloat() * 0.25f)
                    drawLine(
                        color = NeonGreen.copy(alpha = 0.5f + Random.nextFloat() * 0.5f),
                        start = center,
                        end = Offset(center.x + len, center.y),
                        strokeWidth = 2f
                    )
                }
            }
            // núcleo blanco brillante
            drawCircle(color = Color.White, radius = size.toPx() * 0.09f, center = center)
        }
    }
}

/** Fondo simple de estrellas + líneas de constelación. */
@Composable
private fun StarFieldBackground(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val random = Random(42)
        val points = List(28) {
            Offset(random.nextFloat() * size.width, random.nextFloat() * size.height)
        }
        // líneas tenues conectando algunos puntos
        for (i in points.indices) {
            val j = (i + 1) % points.size
            drawLine(
                color = Color.White.copy(alpha = 0.05f),
                start = points[i],
                end = points[j],
                strokeWidth = 1f
            )
        }
        // puntos (estrellas)
        points.forEach { p ->
            drawCircle(color = Color.White.copy(alpha = 0.5f), radius = 1.5f, center = p)
        }
    }
}