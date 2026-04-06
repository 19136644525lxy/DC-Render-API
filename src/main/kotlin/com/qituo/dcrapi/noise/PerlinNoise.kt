package com.qituo.dcrapi.noise

import java.util.Random

class PerlinNoise(seed: Long = System.currentTimeMillis()) : Noise {
    private val permutation = IntArray(512)
    
    init {
        val rand = Random(seed)
        for (i in 0 until 256) {
            permutation[i] = rand.nextInt(256)
        }
        for (i in 256 until 512) {
            permutation[i] = permutation[i - 256]
        }
    }
    
    override fun noise(x: Double, y: Double, z: Double): Double {
        val X = (x.toInt() and 255)
        val Y = (y.toInt() and 255)
        val Z = (z.toInt() and 255)
        
        val u = fade(x - x.toInt())
        val v = fade(y - y.toInt())
        val w = fade(z - z.toInt())
        
        val A = permutation[X] + Y
        val AA = permutation[A] + Z
        val AB = permutation[A + 1] + Z
        val B = permutation[X + 1] + Y
        val BA = permutation[B] + Z
        val BB = permutation[B + 1] + Z
        
        return lerp(w, lerp(v, lerp(u, grad(permutation[AA], x - x.toInt(), y - y.toInt(), z - z.toInt()),
            grad(permutation[BA], x - x.toInt() - 1, y - y.toInt(), z - z.toInt())),
            lerp(u, grad(permutation[AB], x - x.toInt(), y - y.toInt() - 1, z - z.toInt()),
                grad(permutation[BB], x - x.toInt() - 1, y - y.toInt() - 1, z - z.toInt()))),
            lerp(v, lerp(u, grad(permutation[AA + 1], x - x.toInt(), y - y.toInt(), z - z.toInt() - 1),
                grad(permutation[BA + 1], x - x.toInt() - 1, y - y.toInt(), z - z.toInt() - 1)),
                lerp(u, grad(permutation[AB + 1], x - x.toInt(), y - y.toInt() - 1, z - z.toInt() - 1),
                    grad(permutation[BB + 1], x - x.toInt() - 1, y - y.toInt() - 1, z - z.toInt() - 1))))
    }
    
    private fun fade(t: Double): Double {
        return t * t * t * (t * (t * 6 - 15) + 10)
    }
    
    private fun lerp(t: Double, a: Double, b: Double): Double {
        return a + t * (b - a)
    }
    
    private fun grad(hash: Int, x: Double, y: Double, z: Double): Double {
        val h = hash and 15
        val u = if (h < 8) x else y
        val v = if (h < 4) y else if (h == 12 || h == 14) x else z
        return if ((h and 1) == 0 && (h and 2) == 0) (u + v)
        else if ((h and 1) == 0) (u - v)
        else if ((h and 2) == 0) (-u + v)
        else (-u - v)
    }
}
