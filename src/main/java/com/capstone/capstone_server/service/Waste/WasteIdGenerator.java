package com.capstone.capstone_server.service.Waste;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class WasteIdGenerator implements IdentifierGenerator {
  private static final AtomicInteger counter = new AtomicInteger(0);
  private static String lastGeneratedDate = new SimpleDateFormat("yyyyMMdd").format(new Date());

  public Serializable generate(SharedSessionContractImplementor session, Object obj) {
    String nowDate = new SimpleDateFormat("yyyyMMdd").format(new Date());

    if(!nowDate.equals(lastGeneratedDate)) {
      counter.set(0);
      lastGeneratedDate = nowDate;
    }
    int number = counter.incrementAndGet();
    String formattedNumber = String.format("%03d", number);
    return nowDate + formattedNumber;
  }
}
