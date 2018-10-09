package com.springBootProject.controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.springBootProject.Pojos.UploadFileResponse;
import com.springBootProject.Services.FileStorageService;

@RestController
public class FileController {

	@Autowired
	private FileStorageService fileStorageService;

	// Upload a single file
	@PostMapping("/uploadFile")
	public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {

		// Store file
		String fileName = fileStorageService.storeFile(file);

		// Set the download URI
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
				.path(fileName).toUriString();

		// return the response
		return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
	}

	// Upload multiple files
	@PostMapping("/uploadMultipleFiles")
	public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
		return Arrays.asList(files).stream().map(file -> uploadFile(file)).collect(Collectors.toList());
	}

	// Download requested file
	@GetMapping("/downloadFile/{fileName}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {

		// Get file as resource
		Resource fileResource = fileStorageService.loadFileAsResource(fileName);

		// Get type of file
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(fileResource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			contentType = "application/octet-stream";
		}

		// Send back response
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachement; filename = \"" + fileResource.getFilename() + "\"")
				.body(fileResource);
	}

}
