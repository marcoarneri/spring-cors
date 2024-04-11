package it.krisopea.springcors.util;

import java.util.UUID;

public class UuidUtil {
  private UuidUtil() {}

  public static UUID formatToken(String tokenString) {
    String formattedToken =
        String.format(
            "%s-%s-%s-%s-%s",
            tokenString.substring(0, 8),
            tokenString.substring(8, 12),
            tokenString.substring(12, 16),
            tokenString.substring(16, 20),
            tokenString.substring(20));
    return UUID.fromString(formattedToken);
  }

  public static String removeDashesFromUuidString(String uuidString) {
    return uuidString.replace("-", "");
  }
}
