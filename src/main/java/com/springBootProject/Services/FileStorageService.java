package com.springBootProject.Services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

	// Service to store file
	public String storeFile(MultipartFile file);

	// Service to load file as a resource
	public Resource loadFileAsResource(String fileName);
	
}
