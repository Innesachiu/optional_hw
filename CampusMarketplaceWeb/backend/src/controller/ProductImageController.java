package controller;

import dao.ProductDAO;
import dao.ProductImageDAO;
import dto.ApiResponse;
import exception.AppException;
import exception.NotFoundException;
import util.DBConnection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.UUID;

/**
 * Handles product image upload business logic.
 */
public class ProductImageController {
    private final ProductDAO productDAO = new ProductDAO();
    private final ProductImageDAO imageDAO = new ProductImageDAO();

    private static final int MAX_BYTES = 3 * 1024 * 1024; // 3MB

    public ApiResponse uploadImage(int productId, String fileName, String mimeType, String base64Data) {
        if (productId <= 0) return new ApiResponse(false, "invalid productId");
        if (mimeType == null) return new ApiResponse(false, "invalid mimeType");
        if (base64Data == null || base64Data.trim().isEmpty()) return new ApiResponse(false, "empty base64 data");

        // validate product exists
        if (productDAO.findById(productId) == null) {
            return new ApiResponse(false, "product not found");
        }

        // Validate mime
        String ext;
        switch (mimeType) {
            case "image/jpeg": ext = "jpg"; break;
            case "image/png": ext = "png"; break;
            case "image/webp": ext = "webp"; break;
            default: return new ApiResponse(false, "unsupported mime type");
        }

        // Decode
        byte[] bytes;
        try {
            bytes = Base64.getDecoder().decode(base64Data);
        } catch (IllegalArgumentException e) {
            return new ApiResponse(false, "invalid base64 data");
        }
        if (bytes.length == 0) return new ApiResponse(false, "empty image data");
        if (bytes.length > MAX_BYTES) return new ApiResponse(false, "image size exceeds limit");

        // prepare uploads dir
        File uploads = new File("uploads/products");
        if (!uploads.exists()) {
            uploads.mkdirs();
        }

        String safeName = "product_" + productId + "_" + UUID.randomUUID().toString() + "." + ext;
        File out = new File(uploads, safeName);
        try (FileOutputStream fos = new FileOutputStream(out)) {
            fos.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return new ApiResponse(false, "failed to save file");
        }

        // store relative image url
        String imageUrl = "/api/product-images/" + safeName;

        // enforce single primary image by deleting previous images for product
        imageDAO.deleteByProductId(productId);
        boolean ok = imageDAO.insert(productId, imageUrl);
        if (!ok) {
            return new ApiResponse(false, "failed to save image record");
        }

        ApiResponse resp = new ApiResponse(true, "商品圖片上傳成功。");
        resp.setData("{\"productId\":" + productId + ",\"imageUrl\":\"" + imageUrl + "\"}");
        return resp;
    }
}
