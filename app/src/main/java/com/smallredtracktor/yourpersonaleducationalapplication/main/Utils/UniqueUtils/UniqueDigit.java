package com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.UniqueUtils;

import java.util.UUID;

public class UniqueDigit {

        public static String getUnique()
        {
            String u = UUID.randomUUID().toString();
            u = u.replace('-', '0');
            return u;
        }
    }

