package com.example.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class GatewayApplicationTests {
	@Test
	void run() {

		removeStars("leet**cod*e");
	}

	char[] ans = new char[10];
    int size = 0;

    public String removeStars(String s) {
        for (int i = 0; i < s.length(); i++) {
            add(s.charAt(i));
        }

		return (new StringBuffer()).append(ans).toString().substring(0, size);
    }

    public void add (char x) {
        if (size == ans.length) {
            grew();
        }

        if ('*' == x) {
            if (size > 0) {
                size --;
            }
        } else {
            ans[size] = x;
            size ++;
        }
    }

    public void grew() {
        char[] newAns = new char[ans.length + ans.length / 2];
        for (int i = 0; i < ans.length; i++) {
            newAns[i] = ans[i];
        }

        ans = newAns;
    }
}
