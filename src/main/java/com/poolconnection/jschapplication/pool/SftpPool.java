package com.poolconnection.jschapplication.pool;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import lombok.Generated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;

@Generated
@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class SftpPool {

  private final GenericObjectPool<ChannelSftp> poolObject;
  private final Session session;

  public ChannelSftp borrowChannelSftp() throws IOException {
    try {
      return poolObject.borrowObject();
    } catch (Exception exception) {
      throw new IOException("Impossible to borrow ChannelSftp", exception);
    }
  }

  public void returnChannelSftp(ChannelSftp channelSftp) {
    if (channelSftp != null) {
      poolObject.returnObject(channelSftp);
    }
  }

  @PreDestroy
  void cleanSilently() {
    try {
      poolObject.evict();
    } catch (Exception exception) {
      log.warn(exception.getMessage(), exception);
    } finally {
      disconnectSession();
    }

  }

  private void disconnectSession() {
    try {
      session.disconnect();
    } catch (Exception exception) {
      log.warn(exception.getMessage(), exception);
    }
  }
}