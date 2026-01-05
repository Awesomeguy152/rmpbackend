package com.example.services

import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import java.io.InputStream

/**
 * Сервис для загрузки изображений в Cloudinary
 */
class CloudinaryService {
    
    private val cloudinary: Cloudinary by lazy {
        val cloudName = System.getenv("CLOUDINARY_CLOUD_NAME") 
            ?: throw IllegalStateException("CLOUDINARY_CLOUD_NAME not set")
        val apiKey = System.getenv("CLOUDINARY_API_KEY") 
            ?: throw IllegalStateException("CLOUDINARY_API_KEY not set")
        val apiSecret = System.getenv("CLOUDINARY_API_SECRET") 
            ?: throw IllegalStateException("CLOUDINARY_API_SECRET not set")
        
        Cloudinary(ObjectUtils.asMap(
            "cloud_name", cloudName,
            "api_key", apiKey,
            "api_secret", apiSecret,
            "secure", true
        ))
    }
    
    /**
     * Загрузить аватар пользователя
     * @param inputStream поток байтов изображения
     * @param userId ID пользователя для именования файла
     * @return URL загруженного изображения
     */
    fun uploadAvatar(inputStream: InputStream, userId: String): String {
        val bytes = inputStream.readBytes()
        
        val result = cloudinary.uploader().upload(bytes, ObjectUtils.asMap(
            "folder", "avatars",
            "public_id", "avatar_$userId",
            "overwrite", true,
            "resource_type", "image",
            "transformation", ObjectUtils.asMap(
                "width", 256,
                "height", 256,
                "crop", "fill",
                "gravity", "face"
            )
        ))
        
        return result["secure_url"] as String
    }
    
    /**
     * Загрузить изображение для сообщения
     * @param inputStream поток байтов изображения
     * @param fileName оригинальное имя файла
     * @return URL загруженного изображения
     */
    fun uploadImage(inputStream: InputStream, fileName: String): String {
        val bytes = inputStream.readBytes()
        val publicId = "img_${System.currentTimeMillis()}"
        
        val result = cloudinary.uploader().upload(bytes, ObjectUtils.asMap(
            "folder", "messages",
            "public_id", publicId,
            "resource_type", "image"
        ))
        
        return result["secure_url"] as String
    }
    
    /**
     * Удалить изображение
     * @param publicId ID изображения в Cloudinary
     */
    fun deleteImage(publicId: String) {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap())
    }
}
