package com.poolconnection.jschapplication.pool;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Generated
@RequiredArgsConstructor
@Component
public class SftpPoolClient {

  private final SftpPool sftpPool;

  public void uploadFile(String directory, String fileName, InputStream content) throws IOException {
    var channelSftp = sftpPool.borrowChannelSftp();
    try {
      channelSftp.cd(directory);
      channelSftp.put(content, fileName);
    } catch (SftpException sftpException) {
      throw new IOException("Impossible to upload file.", sftpException);
    } finally {
      sftpPool.returnChannelSftp(channelSftp);
    }
  }

  public byte[] downloadFile(String directory, String name) throws IOException {
    var channelSftp = sftpPool.borrowChannelSftp();
    try {
      channelSftp.cd(directory);
      InputStream content = channelSftp.get(name);
      return StreamUtils.copyToByteArray(content);
    } catch (SftpException exception) {
      throw new IOException("Impossible to download file.", exception);
    } finally {
      sftpPool.returnChannelSftp(channelSftp);
    }
  }

  public void deleteFile(String directory, String fileName) throws IOException {
    var channelSftp = sftpPool.borrowChannelSftp();
    try {
      channelSftp.cd(directory);
      channelSftp.rm(fileName);
    } catch (SftpException exception) {
      throw new IOException("Impossible to delete file.", exception);
    } finally {
      sftpPool.returnChannelSftp(channelSftp);
    }
  }

  //TODO: test this method
  private void accessElseMakeDirectory(ChannelSftp channelSftp, String directory) throws IOException {
    var folders = directory.split("/");
    try {
      channelSftp.cd("/");
      for (String folder : folders) {
        if (folder.trim().length() > 0) {
          try {
            channelSftp.cd(folder);
          } catch (Exception e) {
            channelSftp.mkdir(folder);
            channelSftp.cd(folder);
          }
        }
      }
    } catch (SftpException exception) {
      throw  new IOException("Impossible to access root directory.", exception);
    }
  }

}