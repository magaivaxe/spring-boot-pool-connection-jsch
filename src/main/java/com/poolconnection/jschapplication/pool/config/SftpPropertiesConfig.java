package com.poolconnection.jschapplication.pool.config;

import lombok.Generated;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Generated
@Getter
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "sftp")
public class SftpPropertiesConfig {

  private String userName;
  private String host;
  private int port;
  private String privateKey;
  private String directory;

  @Bean("directory")
  public String getDirectory() {
    return directory;
  }

}