package com.nickom.reporting.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import java.io.IOException;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties (
    prefix = "gcp.account"
)
@Configuration
public class GcpCloudStorageConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(GcpCloudStorageConfig.class);

  private String credential;

  public String getCredential() {
    return credential;
  }

  public void setCredential(String credential) {
    this.credential = credential;
  }

  public GoogleCredentials createCredential() throws IOException {
    LOGGER.info("initialize gcp nlp credential from: {}", credential);
    return GoogleCredentials.fromStream(this.getClass().getResourceAsStream(credential))
        .createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform"));
  }
}
