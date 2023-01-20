package com.crypto;

public enum Environment {
    SANDBOX,
    PRODUCTION
    ;

   public String value(){
       return this.name().toLowerCase();
   }

   public String withResourceName(String resourceName) {
       return this.value().concat("-").concat(resourceName);
    }
}
