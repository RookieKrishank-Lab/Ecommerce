package com.ecommerce.project.service.impl;

import com.ecommerce.project.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

//    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        //Get file name of current / original file
        String originalFileName = file.getOriginalFilename();

        //Generate a unique file name( while uploading there is a change of name conflict then we will override some image )
        String randomId = UUID.randomUUID().toString();
        //mat.jpg -> 123(randomId) -> 123.jpg
        String fileName = randomId.concat(originalFileName.substring(originalFileName.indexOf(".")));
        String filePath = path + File.separator + fileName;              //new folder file path. Instead of File.separator we can use "/" but then it wont work on some OS
        //Check if path exist or create a new one
        File folder = new File(path);
        if(!folder.exists())
            folder.mkdir();

        //upload to server
        Files.copy(file.getInputStream(), Paths.get(filePath));         //(input file, destination of file)

        //Return file name
        return fileName;
    }
}
