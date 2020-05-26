package com.training.demo.dto;

public interface Regex {

    String nameRegex = "[А-ЩЮЯҐІЇЄA-Z][а-щьюяґіїє'a-z]{3,30}";
    String surnameRegex = "[А-ЩЮЯҐІЇЄA-Z][а-щьюяґіїє'a-z]{1,30}([-][А-ЩЮЯҐІЇЄA-Z][а-щьюяґіїє'a-z]{1,30})?";
    String loginRegex = "[a-z0-9_-]{3,30}";
}
