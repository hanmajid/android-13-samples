package com.hanmajid.android.tiramisu.programmableshaders

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView

@RequiresApi(33)
class PokemonImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatImageView(context, attrs) {
    private val bulbasaurRuntimeShader = RuntimeShader(
        """
        vec4 main(float2 x, vec4 y)
        {
          return vec4(0.47, 0.78, 0.31, 1.0);
        }
    """.trimIndent()
    )
    private val charmanderRuntimeShader = RuntimeShader(
        """
        vec4 main(float2 x, vec4 y)
        {
          return vec4(0.98, 0.52, 0, 1.0);
        }
    """.trimIndent()
    )
    private val squirtleRuntimeShader = RuntimeShader(
        """
        vec4 main(float2 x, vec4 y)
        {
          return vec4(0.67, 0.85, 0.83, 1.0);
        }
    """.trimIndent()
    )

    private val bulbasaurBitmap = BitmapFactory.decodeResource(resources, R.drawable.bulbasaur)
    private val charmanderBitmap = BitmapFactory.decodeResource(resources, R.drawable.charmander)
    private val squirtleBitmap = BitmapFactory.decodeResource(resources, R.drawable.squirtle)

    private val pokemonName: String?
    private val paint = Paint()
    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PokemonImageView,
            0, 0
        ).apply {
            try {
                pokemonName = getString(R.styleable.PokemonImageView_pokemonName)
            } finally {
                recycle()
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val bitmapPokemon: Bitmap?
        val runtimeShaderPokemon: RuntimeShader
        when (pokemonName) {
            "bulbasaur" -> {
                runtimeShaderPokemon = bulbasaurRuntimeShader
                bitmapPokemon = bulbasaurBitmap
            }
            "charmander" -> {
                runtimeShaderPokemon = charmanderRuntimeShader
                bitmapPokemon = charmanderBitmap
            }
            else -> {
                runtimeShaderPokemon = squirtleRuntimeShader
                bitmapPokemon = squirtleBitmap
            }
        }
        // Draws background
        canvas?.drawColor(Color.WHITE)

        // Draws rectangle with the Pokemon's color
        paint.shader = runtimeShaderPokemon
        canvas?.drawRect(
            0.0f,
            0.0f,
            bitmapPokemon.width.toFloat(),
            bitmapPokemon.height.toFloat(),
            paint,
        )

        // Draw the Pokemon's bitmap with PorterDuff.
        paint.xfermode = xfermode
        canvas?.drawBitmap(bitmapPokemon, 0.0f, 0.0f, paint)
    }
}