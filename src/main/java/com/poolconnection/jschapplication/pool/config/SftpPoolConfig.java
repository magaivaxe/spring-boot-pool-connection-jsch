package com.poolconnection.jschapplication.pool.config;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.poolconnection.jschapplication.pool.SftpPoolFactory;
import lombok.Generated;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Generated
@Configuration
@EnableConfigurationProperties
public class SftpPoolConfig {

  @Bean
  public Session sessionJsch(SftpPropertiesConfig sftpPropertiesConfig) throws JSchException, IOException {
    JSch jsch = new JSch();
    jsch.addIdentity("key", sftpPropertiesConfig.getPrivateKey().getBytes(), null, null);
    var session = jsch.getSession(sftpPropertiesConfig.getUserName(),
                                  sftpPropertiesConfig.getHost(),
                                  sftpPropertiesConfig.getPort());
    var jschConfiguration = jschConfiguration();
    session.setConfig(jschConfiguration);
    return session;
  }

  @Bean
  @ConfigurationProperties(prefix = "sftp.pool")
  public GenericObjectPoolConfig<ChannelSftp> genericObjectPoolConfig() {
    var config = new GenericObjectPoolConfig<ChannelSftp>();
    config.setJmxEnabled(false);
    return config;
  }

  @Bean
  public GenericObjectPool<ChannelSftp> genericObjectPool(SftpPoolFactory factory,
                                                              GenericObjectPoolConfig<ChannelSftp> poolConfig) {
    return new GenericObjectPool<>(factory, poolConfig);
  }

  private Properties jschConfiguration() throws IOException {
    var file = ResourceUtils.getFile("classpath:jschConfiguration.properties");
    var config = new Properties();
    try (var reader = new FileReader(file)) {
      config.load(reader);
    }
    return config;
  }

}