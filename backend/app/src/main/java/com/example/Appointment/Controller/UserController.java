package com.example.Appointment.Controller;

import com.example.Appointment.DTO.AppointmentDTO;
import com.example.Appointment.DTO.ChangePasswordDTO;
import com.example.Appointment.DTO.DoctorDTO;
import com.example.Appointment.DTO.UserDTO;
import com.example.Appointment.Response.ResponseHandler;
import com.example.Appointment.Service.AppointmentService;
import com.example.Appointment.Service.DoctorService;
import com.example.Appointment.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    UserService userService;
    DoctorService doctorService;
    AppointmentService appointmentService;
    ResponseHandler responseHandler;

    public UserController(UserService userService, DoctorService doctorService, AppointmentService appointmentService, ResponseHandler responseHandler) {
        this.userService = userService;
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
        this.responseHandler = responseHandler;
    }

    @GetMapping("/")
    public RedirectView redirectToIndex() {
        return new RedirectView("/user_login.html");
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserDTO userDTO, HttpSession session) {
        UserDTO user = userService.login(userDTO.getEmail(), userDTO.getPassword());

        if (user != null) {
            session.setAttribute("userObj", user);
            return responseHandler.responseBuilder("Đăng nhập thành công!", HttpStatus.OK, user);
        } else {
            return responseHandler.responseBuilder("Email hoặc mật khẩu không đúng!", HttpStatus.UNAUTHORIZED, null);
        }
    }
    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody UserDTO userDto) {
        boolean success = userService.registerUser(userDto);
        if (success) {
            return responseHandler.responseBuilder("Đăng kí tài khoản thành công", HttpStatus.CREATED, null);
        } else {
            return responseHandler.responseBuilder("Email đã được sử dụng", HttpStatus.CONFLICT, null);
        }
    }
    @GetMapping("/current")
    public ResponseEntity<Object> getCurrentUser(HttpSession session) {
        Object sessionUser = session.getAttribute("userObj");

        if (sessionUser != null) {
            return responseHandler.responseBuilder("Người dùng đã trong phiên đăng nhập", HttpStatus.OK, sessionUser);
        } else {
            return responseHandler.responseBuilder("Người dùng chưa đăng nhập", HttpStatus.UNAUTHORIZED, null);
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpSession session) {
        UserDTO sessionUser = (UserDTO) session.getAttribute("userObj");

        if (sessionUser != null) {
            session.removeAttribute("userObj");
            return responseHandler.responseBuilder("Đăng xuất thành công", HttpStatus.OK, null);
        } else {
            return responseHandler.responseBuilder("Không có người dùng nào đang đăng nhập", HttpStatus.UNAUTHORIZED, null);
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable("id") int id, HttpSession session) {
        UserDTO sessionUser = (UserDTO) session.getAttribute("userObj");
        if (sessionUser == null) {
            return responseHandler.responseBuilder("Người dùng chưa đăng nhập", HttpStatus.UNAUTHORIZED, null);
        }

        if (sessionUser.getUser_id() != id) {
            return responseHandler.responseBuilder("Không có quyền truy cập thông tin người khác", HttpStatus.FORBIDDEN, null);
        }

        UserDTO user = userService.getUserById(id);
        if (user != null) {
            return responseHandler.responseBuilder("Lấy dữ liệu thành công", HttpStatus.OK, user);
        } else {
            return responseHandler.responseBuilder("Không tìm thấy người dùng", HttpStatus.NOT_FOUND, null);
        }
    }
    @PutMapping("/change-password")
    public ResponseEntity<Object> changePassword(@RequestBody ChangePasswordDTO dto, HttpSession session) {
        UserDTO sessionUser = (UserDTO) session.getAttribute("userObj");
        if (sessionUser == null) {
            return responseHandler.responseBuilder("Người dùng chưa đăng nhập", HttpStatus.UNAUTHORIZED, null);
        }

        // Chỉ cho đổi mật khẩu nếu user trong session trùng với userId gửi lên
        if (dto.getUserId() != sessionUser.getUser_id()) {
            return responseHandler.responseBuilder("Không có quyền thay đổi mật khẩu của người khác", HttpStatus.FORBIDDEN, null);
        }

        boolean success = userService.changeUserPassword(dto.getUserId(), dto.getOldPassword(), dto.getNewPassword());

        if (success) {
            return responseHandler.responseBuilder("Mật khẩu đã được thay đổi thành công", HttpStatus.OK, null);
        } else {
            return responseHandler.responseBuilder("Mật khẩu cũ không đúng", HttpStatus.UNAUTHORIZED, null);
        }
    }
    @GetMapping("/doctors")
    public ResponseEntity<Object> getAllDoctors() {
        List<DoctorDTO> doctors = doctorService.getAllDoctors();
        if (doctors.isEmpty()) {
            return responseHandler.responseBuilder("Không có bác sĩ nào trong hệ thống", HttpStatus.OK, doctors);
        }
        return responseHandler.responseBuilder("Lấy dữ liệu thành công", HttpStatus.OK, doctors);

    }

    @PostMapping("/appointment")
    public ResponseEntity<Object> createAppointment(@RequestBody AppointmentDTO appointmentDTO, HttpSession session) {
        UserDTO sessionUser = (UserDTO) session.getAttribute("userObj");
        if (sessionUser == null) {
            return responseHandler.responseBuilder("Người dùng chưa đăng nhập", HttpStatus.UNAUTHORIZED, null);
        }

        appointmentDTO.setUserId(sessionUser.getUser_id()); // Gán ID người dùng đang đăng nhập
        appointmentDTO.setStatus("Chờ duyệt");       // Trạng thái mặc định

        appointmentService.saveAppointment(appointmentDTO);
        return responseHandler.responseBuilder("Lịch hẹn đã được tạo thành công. Vui lòng chờ duyệt", HttpStatus.CREATED, null);
    }

    @GetMapping("/appointments")
    public ResponseEntity<Object> getAppointmentsByUser(HttpSession session) {
        UserDTO sessionUser = (UserDTO) session.getAttribute("userObj");
        if (sessionUser == null) {
            return responseHandler.responseBuilder("Người dùng chưa đăng nhập", HttpStatus.UNAUTHORIZED, null);
        }

        List<AppointmentDTO> list = appointmentService.getAllAppointmentsByUserId(sessionUser.getUser_id());
        if (list.isEmpty()) {
            return responseHandler.responseBuilder("Không có lịch hẹn nào", HttpStatus.OK, list);
        }
        return responseHandler.responseBuilder("Lấy dữ liệu thành công", HttpStatus.OK, list);
    }

    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<Object> deleteAppointment(@PathVariable int id, HttpSession session) {
        UserDTO sessionUser = (UserDTO) session.getAttribute("userObj");
        if (sessionUser == null) {
            return responseHandler.responseBuilder("Người dùng chưa đăng nhập", HttpStatus.UNAUTHORIZED, null);
        }

        boolean deleted = appointmentService.deleteAppointment(id);
        if (deleted) {
            return responseHandler.responseBuilder("Xóa lịch hẹn thành công", HttpStatus.OK, null);
        }
        return responseHandler.responseBuilder("Không tìm thấy lịch hẹn hoặc không có quyền", HttpStatus.NOT_FOUND, null);
    }
    @GetMapping("/appointments/{id}")
    public ResponseEntity<Object> getAppointmentById(@PathVariable int id, HttpSession session) {
        AppointmentDTO appointmentDTO = appointmentService.getAppointmentById(id);
        if (appointmentDTO == null) {
            return responseHandler.responseBuilder("Không tìm thấy lịch hẹn", HttpStatus.NOT_FOUND, null);
        }

        // Kiểm tra quyền truy cập nếu cần
        UserDTO sessionUser = (UserDTO) session.getAttribute("userObj");
        if (sessionUser == null || appointmentDTO.getUserId() != sessionUser.getUser_id()) {
            return responseHandler.responseBuilder("Không có quyền truy cập lịch hẹn này", HttpStatus.FORBIDDEN, null);
        }

        return responseHandler.responseBuilder("Lấy dữ liệu thành công", HttpStatus.OK, appointmentDTO);
    }


    @PutMapping("/appointments/{id}")
    public ResponseEntity<Object> updateAppointment(@PathVariable int id,
                                                    @RequestBody AppointmentDTO appointmentDTO,
                                                    HttpSession session) {
        UserDTO sessionUser = (UserDTO) session.getAttribute("userObj");
        if (sessionUser == null) {
            return responseHandler.responseBuilder("Người dùng chưa đăng nhập", HttpStatus.UNAUTHORIZED, null);
        }

        appointmentDTO.setId(id);
        appointmentDTO.setStatus("Chờ duyệt"); // Reset trạng thái nếu cần duyệt lại

        // Kiểm tra quyền nếu cần
        AppointmentDTO existing = appointmentService.getAppointmentById(id);
        if (existing == null) {
            return responseHandler.responseBuilder("Không tìm thấy lịch hẹn", HttpStatus.NOT_FOUND, null);
        }
        if (existing.getUserId() != sessionUser.getUser_id()) {
            return responseHandler.responseBuilder("Không có quyền cập nhật lịch hẹn này", HttpStatus.FORBIDDEN, null);
        }

        boolean updated = appointmentService.updateAppointment(appointmentDTO);
        if (updated) {
            return responseHandler.responseBuilder("Cập nhật lịch hẹn thành công", HttpStatus.OK, appointmentDTO);
        }

        return responseHandler.responseBuilder("Cập nhật lịch hẹn thất bại", HttpStatus.BAD_REQUEST, null);
    }

}
