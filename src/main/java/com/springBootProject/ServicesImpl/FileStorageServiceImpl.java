package com.springBootProject.ServicesImpl;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.springBootProject.Exceptions.FileStorageException;
import com.springBootProject.Exceptions.MyFileNotFoundException;
import com.springBootProject.Pojos.FileStorageProperties;
import com.springBootProject.Services.FileStorageService;

@Service
public class FileStorageServiceImpl implements FileStorageService {

	private final Path fileStorageLocation;

	// File storage service constructor
	@Autowired
	public FileStorageServiceImpl(FileStorageProperties fileStorageProperties) {

		// Set the file storage location
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

		// Create directory if doesn't exist
		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new FileStorageException("Could not create the directory where the files will be uploaded.", ex);
		}
	}

	// Service to store file
	@Override
	public String storeFile(MultipartFile file) {

		// Normalize the file name
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try {

			// Check file name for invalid characters
			if (fileName.contains("..")) {
				throw new FileStorageException("The file name contain invalid path symbols -> " + fileName);
			}

			// Copy file to the location (OverWriting Enabled)
			Path targetLocation = this.fileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			// Return file name
			return fileName;

		} catch (Exception ex) {
			throw new FileStorageException("There is a error saving the file.", ex);
		}
	}

	// Service to load file as a resource
	@Override
	public Resource loadFileAsResource(String fileName) {

		try {
			Path sourceLocation = this.fileStorageLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(sourceLocation.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new MyFileNotFoundException("The file doesn't exist.");
			}
		} catch (MalformedURLException ex) {
			throw new MyFileNotFoundException("The file doesn't exist.", ex);
		}
	}

}
