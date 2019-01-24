package me.wired.learning.user;

import java.util.Arrays;
import java.util.HashSet;

public class XUserGenerator {

    public static XUserDto newNormalXUserDto(int i, String variableId, String password) {
        return XUserDto.builder()
                .variableId(variableId)
                .name("Test Name-" + i)
                .password(password)
                .roles(new HashSet<>(Arrays.asList(XUserRole.ADMIN, XUserRole.USER)))
                .build();
    }

    public static XUserDto newWrongXUserDto1() {
        // Invalid email format
        return XUserDto.builder()
                .variableId("test") // Invalid ID
                .name("Steven Allan Spielberg")
                .password("12345")
                .roles(new HashSet<>(Arrays.asList(XUserRole.ADMIN, XUserRole.USER)))
                .build();
    }

    public static XUserDto newWrongXUserDto2() {
        // Invalid password
        return XUserDto.builder()
                .variableId("test@gmail.com")
                .name("Steven Allan Spielberg")
                .password("1") // Invalid password
                .roles(new HashSet<>(Arrays.asList(XUserRole.ADMIN, XUserRole.USER)))
                .build();
    }
}
