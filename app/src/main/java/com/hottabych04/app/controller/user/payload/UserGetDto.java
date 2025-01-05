package com.hottabych04.app.controller.user.payload;

import java.util.List;

public record UserGetDto(
   Long id,
   String email,
   List<String> roles
) {
}
