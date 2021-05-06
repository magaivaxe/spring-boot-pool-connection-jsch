package com.poolconnection.jschapplication.example;

import com.poolconnection.jschapplication.pool.SftpPoolClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class ExampleController {

  @Autowired
  SftpPoolClient sftpPoolClient;
  @Autowired
  @Qualifier("directory")
  String directory;

  @PostMapping("/postFile")
  public ResponseEntity<HttpStatus> postFile(MultipartFile file) throws IOException {
    sftpPoolClient.uploadFile(directory, file.getName(), file.getInputStream());
    return new ResponseEntity<>(HttpStatus.OK);
  }
}