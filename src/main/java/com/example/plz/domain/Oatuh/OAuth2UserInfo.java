package com.example.plz.domain.Oatuh;

public interface OAuth2UserInfo {
    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();

    String getNickname();
}