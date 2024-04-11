package it.krisopea.springcors.util;

import java.util.concurrent.atomic.AtomicInteger;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class GlobalEmailResources {
  private AtomicInteger emailCounter = new AtomicInteger(0);
  private AtomicInteger registrationEmailCounter = new AtomicInteger(0);
  private AtomicInteger updateEmailCounter = new AtomicInteger(0);
  private AtomicInteger deleteEmailCounter = new AtomicInteger(0);
  private AtomicInteger loginEmailCounter = new AtomicInteger(0);
  private AtomicInteger verificationEmailCounter = new AtomicInteger(0);

  public void incrementEmailCounter() {
    this.emailCounter.incrementAndGet();
  }

  public void incrementRegistrationEmailCounter() {
    this.registrationEmailCounter.incrementAndGet();
  }

  public void incrementUpdateEmailCounter() {
    this.updateEmailCounter.incrementAndGet();
  }

  public void incrementDeleteEmailCounter() {
    this.deleteEmailCounter.incrementAndGet();
  }

  public void incrementLoginEmailCounter() {
    this.loginEmailCounter.incrementAndGet();
  }

  public void incrementVerificationEmailCounter() {
    this.verificationEmailCounter.incrementAndGet();
  }
}
