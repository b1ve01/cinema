package com.example.demo.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class User {

  private Long userId;
  private String userName;
  private String userPhone;
  private String userPassword;
  private String userEmail;
  private String userUrl;
  private String userProfile;

}