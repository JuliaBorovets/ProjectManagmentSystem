package com.training.demo.dto;

public interface Regex {

   String nameRegex = "[А-ЩЮЯҐІЇЄ][а-щьюяґіїє']{3,30}";
   String surnameRegex = "[А-ЩЮЯҐІЇЄ][а-щьюяґіїє']{1,30}([-][А-ЩЮЯҐІЇЄ][а-щьюяґіїє']{1,30})?";
   String loginRegex = "[a-z0-9_-]{3,30}";
   String deadlineRegex = "202[0-9]-[01-12]-[01-31]";
}
