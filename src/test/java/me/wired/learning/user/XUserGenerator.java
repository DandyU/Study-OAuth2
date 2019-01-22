package me.wired.learning.user;

import java.util.Arrays;
import java.util.HashSet;

public class XUserGenerator {

    public static XUserDto newNormalXUserDto(int i) {
        return XUserDto.builder()
                .variableId("test" + i +  "@gmail.com")
                .name("Steven Allan Spielberg-" + i)
                .password("12345")
                .roles(new HashSet<>(Arrays.asList(XUserRole.ADMIN, XUserRole.USER)))
                .build();
    }

    public static XUserDto newWrongXUserDto1() {
        // Invalid email format
        return XUserDto.builder()
                .variableId("test")
                .name("Steven Allan Spielberg")
                .password("12345") // Invalid password
                .roles(new HashSet<>(Arrays.asList(XUserRole.ADMIN, XUserRole.USER)))
                .build();
    }

    public static XUserDto newWrongXUserDto2() {
        // Invalid password
        return XUserDto.builder()
                .variableId("test@gmail.com")
                .name("Steven Allan Spielberg")
                .password("1234")
                .roles(new HashSet<>(Arrays.asList(XUserRole.ADMIN, XUserRole.USER)))
                .build();
    }
}
