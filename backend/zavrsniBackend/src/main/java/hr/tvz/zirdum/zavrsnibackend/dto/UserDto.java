package hr.tvz.zirdum.zavrsnibackend.dto;

import hr.tvz.zirdum.zavrsnibackend.model.User;

public record UserDto(
        Long id,
        String userName,
        String email,
        String firstName,
        String lastName
) {
    public static UserDto from(User user) {
        return new UserDto(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName()
        );
    }
}