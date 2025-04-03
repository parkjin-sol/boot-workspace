package com.kh.start.file.service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
public class FileService {
	private final Path fileLocation;
	
	public FileService() {
		this.fileLocation = Paths.get("uploads").toAbsolutePath().normalize();
	}
	
	public String store(MultipartFile file) {
		
		// 이름 버꾸는 메서드 호출했다 치기~~~
		
		String originalFileName = file.getOriginalFilename();
		
		Path targetLocation = this.fileLocation.resolve(originalFileName);
		
		try {
			Files.copy(file.getInputStream(),
		   targetLocation,
		   StandardCopyOption.REPLACE_EXISTING);	
			
			return "http://localhost/uploads/" + originalFileName;
		} catch(IOException e) {
			throw new RuntimeException("이상요상 파일이예용");
		}
		
	}
	
}