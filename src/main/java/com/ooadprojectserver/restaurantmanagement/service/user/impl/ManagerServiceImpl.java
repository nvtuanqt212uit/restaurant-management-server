package com.ooadprojectserver.restaurantmanagement.service.user.impl;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.UserResponse;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.RoleConstant;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.Status;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.User;
import com.ooadprojectserver.restaurantmanagement.repository.user.ManagerRepository;
import com.ooadprojectserver.restaurantmanagement.service.authentication.JwtService;
import com.ooadprojectserver.restaurantmanagement.service.user.ManagerService;
import com.ooadprojectserver.restaurantmanagement.service.user.factory.ManagerFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {
    private final ManagerFactory managerFactory;
    private final ManagerRepository managerRepository;
    private final JwtService jwtService;

    // UserService implementation Start
    @Override
    public void saveUser(UserRegisterRequest userRegisterRequest) {
        managerRepository.save(managerFactory.create(userRegisterRequest));
    }

    @Override
    public void updateUserById(User user, UserRegisterRequest userRegisterRequest) {
        managerRepository.save(managerFactory.update(user, userRegisterRequest));
    }

    @Override
    public UserResponse getProfile(HttpServletRequest request) {
        String username = jwtService.getUsernameFromHeader(request);
        User manager = managerRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(APIStatus.USER_NOT_FOUND)
        );
        return covertUserToUserResponse(manager);
    }
    // UserService implementation End

    private UserResponse covertUserToUserResponse(User user) {
        String sRole = switch (user.getRole()) {
            case 1 -> RoleConstant.ROLE.OWNER.name().toLowerCase();
            case 2 -> RoleConstant.ROLE.MANAGER.name().toLowerCase();
            case 3 -> RoleConstant.ROLE.STAFF.name().toLowerCase();
            default -> throw new IllegalStateException("Unexpected value: " + user.getRole());
        };
        Integer status = switch (user.getStatus()) {
            case 0 -> Status.ACTIVE.ordinal();
            case 1 -> Status.INACTIVE.ordinal();
            default -> throw new IllegalStateException("Unexpected value: " + user.getStatus());
        };
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .dateOfBirth(user.getDateOfBirth())
                .address(user.getAddress())
                .status(status)
                .role(sRole)
                .build();
    }
}
