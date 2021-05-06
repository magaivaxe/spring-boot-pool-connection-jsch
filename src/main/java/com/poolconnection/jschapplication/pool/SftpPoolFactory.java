package com.poolconnection.jschapplication.pool;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.Generated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Generated
@Slf4j
@Getter
@RequiredArgsConstructor
@Component
public class SftpPoolFactory extends BasePooledObjectFactory<ChannelSftp> {

  private final Session session;

  @Override
  public ChannelSftp create() throws Exception {
    try {
      if (!session.isConnected()) {
        session.connect();
      }
      var channel = (ChannelSftp) session.openChannel("sftp");
      channel.connect();
      return channel;
    } catch (JSchException jschException) {
      throw new IOException("Impossible to create connection to server.", jschException);
    }
  }

  @Override
  public PooledObject<ChannelSftp> wrap(ChannelSftp channelSftp) {
    return new DefaultPooledObject<>(channelSftp);
  }

  @Override
  public void destroyObject(PooledObject<ChannelSftp> pooledChannelSftp) {
    var channelSftp = pooledChannelSftp.getObject();
    try {
      channelSftp.disconnect();
    } catch (Exception exception) {
      log.warn(exception.getMessage(), exception);
    }
  }

}