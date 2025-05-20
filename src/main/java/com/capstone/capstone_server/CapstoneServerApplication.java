package com.capstone.capstone_server;

import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CapstoneServerApplication {

  public static void main(String[] args) {
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    System.out.println("서버 시간대 설정 완료: " + TimeZone.getDefault().getID());
    SpringApplication.run(CapstoneServerApplication.class, args);
  }

}
