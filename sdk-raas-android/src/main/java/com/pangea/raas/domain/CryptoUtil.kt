package com.pangea.raas.domain

import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.*
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException


internal object CryptoUtil {
    private const val CRYPTO_METHOD = "RSA"
    private const val CRYPTO_BITS = 2048

    @Throws(
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    fun generateKeyPair(): Map<String, String> {
        val kpg = KeyPairGenerator.getInstance(CRYPTO_METHOD)
        kpg.initialize(CRYPTO_BITS)
        val kp = kpg.genKeyPair()
        val publicKey = kp.public
        val privateKey = kp.private
        val map: MutableMap<String, String> = HashMap<String, String>()
        map["privateKey"] = Base64.encodeToString(privateKey.encoded, Base64.DEFAULT)
        map["publicKey"] = Base64.encodeToString(publicKey.encoded, Base64.DEFAULT)
        return map
    }

    @Throws(
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class,
        InvalidKeySpecException::class
    )
    fun encrypt(pubk: String, plain: String): String {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, stringToPublicKey(clearPem(pubk)))
        val encryptedBytes = cipher.doFinal(plain.toByteArray(StandardCharsets.UTF_8))
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    //add private key conditions in order to work on both scenarios
    fun clearPem(publicKey: String): String {
        return publicKey.replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "").replace(Regex("\\s+"), "")
            .replace(Regex("[ \\t\\n]+"), "")

    }

    @Throws(
        NoSuchPaddingException::class,
        NoSuchAlgorithmException::class,
        BadPaddingException::class,
        IllegalBlockSizeException::class,
        InvalidKeySpecException::class,
        InvalidKeyException::class
    )
    fun decrypt(privk: String, result: String): String {
        //val cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding")
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING")
        cipher.init(Cipher.DECRYPT_MODE, stringToPrivateKey(privk))
        val decryptedBytes = cipher.doFinal(Base64.decode(result, Base64.DEFAULT))
        return String(decryptedBytes)
    }

    @Throws(InvalidKeySpecException::class, NoSuchAlgorithmException::class)
    private fun stringToPublicKey(publicKeyString: String): PublicKey {
        val keyBytes = Base64.decode(publicKeyString, Base64.DEFAULT)
        val spec = X509EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance(CRYPTO_METHOD)
        return keyFactory.generatePublic(spec)
    }

    @Throws(InvalidKeySpecException::class, NoSuchAlgorithmException::class)
    private fun stringToPrivateKey(privateKeyString: String): PrivateKey {
        val pkcs8EncodedBytes = Base64.decode(privateKeyString, Base64.DEFAULT)
        val keySpec = PKCS8EncodedKeySpec(pkcs8EncodedBytes)
        val kf = KeyFactory.getInstance(CRYPTO_METHOD)
        return kf.generatePrivate(keySpec)
    }
}