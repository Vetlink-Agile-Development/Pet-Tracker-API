package com.vetlink.pet.tracker.monitoring.infrastructure.storage;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Service for uploading files to Azure Blob Storage.
 */
@Service
public class AzureStorageService {

    private final BlobServiceClient blobServiceClient;
    private final String containerName;

    public AzureStorageService(
            @Value("${azure.storage.connection-string}") String connectionString,
            @Value("${azure.storage.container-name}") String containerName
    ) {
        this.blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
        this.containerName = containerName;
        // Ensure container exists
        ensureContainerExists();
    }

    private void ensureContainerExists() {
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        if (!containerClient.exists()) {
            containerClient.create();
        }
    }

    /**
     * Uploads a file to Azure Blob Storage and returns the public URL.
     *
     * @param file the file to upload
     * @param folder optional folder/prefix for organizing blobs (e.g., "diseases", "vaccinations")
     * @return the public URL of the uploaded blob
     * @throws IOException if an error occurs during upload
     */
    public String uploadFile(MultipartFile file, String folder) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String blobName = (folder != null && !folder.isEmpty() ? folder + "/" : "") 
                + UUID.randomUUID() + "_" + System.currentTimeMillis() + extension;

        // Get blob client
        BlobClient blobClient = blobServiceClient
                .getBlobContainerClient(containerName)
                .getBlobClient(blobName);

        // Set content type
        BlobHttpHeaders headers = new BlobHttpHeaders()
                .setContentType(file.getContentType());

        // Upload file
        try (InputStream inputStream = file.getInputStream()) {
            blobClient.upload(inputStream, file.getSize(), true);
            blobClient.setHttpHeaders(headers);
        }

        // Return the public URL
        return blobClient.getBlobUrl();
    }

    /**
     * Deletes a blob from Azure Storage given its URL.
     *
     * @param blobUrl the full URL of the blob to delete
     */
    public void deleteFile(String blobUrl) {
        if (blobUrl == null || blobUrl.isEmpty()) {
            return;
        }

        try {
            // Extract blob name from URL
            // URL format: https://<account>.blob.core.windows.net/<container>/<blobName>
            String[] parts = blobUrl.split("/" + containerName + "/");
            if (parts.length < 2) {
                return;
            }
            String blobName = parts[1];

            BlobClient blobClient = blobServiceClient
                    .getBlobContainerClient(containerName)
                    .getBlobClient(blobName);

            if (blobClient.exists()) {
                blobClient.delete();
            }
        } catch (Exception e) {
            // Log error but don't throw exception
            System.err.println("Error deleting blob: " + e.getMessage());
        }
    }
}
