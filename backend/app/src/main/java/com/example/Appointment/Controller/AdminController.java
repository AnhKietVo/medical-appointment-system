package com.example.Appointment.Controller;

import com.example.Appointment.DTO.AppointmentDTO;
import com.example.Appointment.DTO.DoctorDTO;
import com.example.Appointment.DTO.UserDTO;
import com.example.Appointment.Response.ResponseHandler;
import com.example.Appointment.Service.AdminService;
import com.example.Appointment.Service.AppointmentService;
import com.example.Appointment.Service.DoctorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    AdminService adminService;
    DoctorService doctorService;
    AppointmentService appointmentService;
    ResponseHandler responseHandler;

    public AdminController(AdminService adminService, DoctorService doctorService, AppointmentService appointmentService, ResponseHandler responseHandler) {
        this.adminService = adminService;
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
        this.responseHandler = responseHandler;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestParam String email,
                                        @RequestParam String password,
                                        HttpSession session) {
        if ("admin@gmail.com".equals(email) && "123456".equals(password)) {
            // Tạo user giả cho session admin
            UserDTO adminUser = new UserDTO();
            adminUser.setEmail(email);
            adminUser.setFullName("Quản trị viên");
            session.setAttribute("adminObj", adminUser);

            return responseHandler.responseBuilder("Đăng nhập thành công!", HttpStatus.OK, adminUser);
        }

        else {
            return responseHandler.responseBuilder("Email hoặc mật khẩu không hợp lệ!", HttpStatus.UNAUTHORIZED, null);
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpSession session) {
        UserDTO adminSession = (UserDTO) session.getAttribute("adminObj");

        if (adminSession != null) {
            session.removeAttribute("adminObj");
            return responseHandler.responseBuilder("Đăng xuất thành công", HttpStatus.OK, null);
        } else {
            return responseHandler.responseBuilder("Không có admin nào đang đăng nhập", HttpStatus.UNAUTHORIZED, null);
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Object> getDashboard(HttpSession session) {
        UserDTO adminSession = (UserDTO) session.getAttribute("adminObj");
        if (adminSession == null) {
            return responseHandler.responseBuilder("Quản trị viên chưa đăng nhập", HttpStatus.UNAUTHORIZED, null);
        }

        Map<String, Integer> total = adminService.getDashboardStats();

        return responseHandler.responseBuilder("Tải dữ liệu thành công", HttpStatus.OK, total);

    }

    @PostMapping("/add-doctor")
    public ResponseEntity<Object> addDoctor(@RequestBody DoctorDTO doctorDTO, HttpSession session) {
        UserDTO adminSession = (UserDTO) session.getAttribute("adminObj");
        if (adminSession == null) {
            return responseHandler.responseBuilder("Quản trị viên chưa đăng nhập", HttpStatus.UNAUTHORIZED, null);
        }
        if (doctorService.existsByEmail(doctorDTO.getEmail())) {
            return responseHandler.responseBuilder("Email đã được sử dụng", HttpStatus.CONFLICT, null);
        }
        doctorService.saveDoctor(doctorDTO);
        return responseHandler.responseBuilder("Thêm bác sĩ mới thành công", HttpStatus.OK, null);
    }

    @GetMapping("/doctor-list")
    public ResponseEntity<Object> getAllDoctors(HttpSession session) {
        UserDTO adminSession = (UserDTO) session.getAttribute("adminObj");
        if (adminSession == null) {
            return responseHandler.responseBuilder("Quản trị viên chưa đăng nhập", HttpStatus.UNAUTHORIZED, null);
        }
        List<DoctorDTO> doctors = doctorService.getAllDoctors();
        if (doctors.isEmpty()) {
            return responseHandler.responseBuilder("Không có bác sĩ nào trong hệ thống", HttpStatus.OK, doctors);
        }
        return responseHandler.responseBuilder("Lấy dữ liệu thành công", HttpStatus.OK, doctors);

    }

    @DeleteMapping("/delete-doctor/{id}")
    public ResponseEntity<Object> deleteDoctor(@PathVariable int id, HttpSession session) {
        UserDTO adminSession = (UserDTO) session.getAttribute("adminObj");
        if (adminSession == null) {
            return responseHandler.responseBuilder("Quản trị viên chưa đăng nhập", HttpStatus.UNAUTHORIZED, null);
        }
        boolean deleted = doctorService.deleteDoctor(id);
        if (deleted) {
            return responseHandler.responseBuilder("Xóa thành công", HttpStatus.OK, null);
        }
        return responseHandler.responseBuilder("Không tìm thấy bác sĩ", HttpStatus.NOT_FOUND, null);
    }

    @GetMapping("/doctor/{id}")
    public ResponseEntity<Object> getDoctorById(@PathVariable int id, HttpSession session) {
        UserDTO adminSession = (UserDTO) session.getAttribute("adminObj");
        if (adminSession == null) {
            return responseHandler.responseBuilder("Quản trị viên chưa đăng nhập", HttpStatus.UNAUTHORIZED, null);
        }
        DoctorDTO doctorDTO = doctorService.getDoctorById(id);
        if (doctorDTO != null){
            return responseHandler.responseBuilder("Lấy dữ liệu thành công", HttpStatus.OK, doctorDTO);
        }
        return  responseHandler.responseBuilder("Không tìm thấy bác sĩ", HttpStatus.NOT_FOUND, null);
    }

    @PutMapping("/update-doctor/{id}")
    public ResponseEntity<Object> updateDoctor(@PathVariable int id, @RequestBody DoctorDTO doctorDTO,HttpSession session) {
        UserDTO adminSession = (UserDTO) session.getAttribute("adminObj");
        if (adminSession == null) {
            return responseHandler.responseBuilder("Quản trị viên chưa đăng nhập", HttpStatus.UNAUTHORIZED, null);
        }
        doctorDTO.setDoctor_id(id); // Gán id từ URL vào DTO
        boolean updated = doctorService.updateDoctor(doctorDTO);
        if (updated) {
            return responseHandler.responseBuilder("Cập nhật thông tin bác sĩ thành công", HttpStatus.OK, doctorDTO);
        }
        return responseHandler.responseBuilder("Cập nhật thông tin bác sĩ thất bại", HttpStatus.NOT_FOUND, null);
    }

    @GetMapping("/appointments")
    public ResponseEntity<Object> getAllAppointments(HttpSession session) {
        UserDTO adminSession = (UserDTO) session.getAttribute("adminObj");
        if (adminSession == null) {
            return responseHandler.responseBuilder("Quản trị viên chưa đăng nhập", HttpStatus.UNAUTHORIZED, null);
        }
        List<AppointmentDTO> list = appointmentService.getAllAppointments();
        return responseHandler.responseBuilder("Lấy dữ liệu thành công", HttpStatus.OK, list);
    }

    @PutMapping("/appointments/accept/{id}")
    public ResponseEntity<Object> acceptAppointment(@PathVariable int id,HttpSession session) {
        UserDTO adminSession = (UserDTO) session.getAttribute("adminObj");
        if (adminSession == null) {
            return responseHandler.responseBuilder("Quản trị viên chưa đăng nhập", HttpStatus.UNAUTHORIZED, null);
        }
        boolean accepted = appointmentService.updateComment(id, "Đã duyệt");
        if (accepted) {
            return responseHandler.responseBuilder("Duyệt thành công", HttpStatus.OK, null);
        }
        return responseHandler.responseBuilder("Duyệt thất bại", HttpStatus.NOT_FOUND, null);
    }

    @DeleteMapping("/appointments/reject/{id}")
    public ResponseEntity<Object> rejectAppointment(@PathVariable int id, HttpSession session) {
        UserDTO adminSession = (UserDTO) session.getAttribute("adminObj");
        if (adminSession == null) {
            return responseHandler.responseBuilder("Quản trị viên chưa đăng nhập", HttpStatus.UNAUTHORIZED, null);
        }
        boolean deleted = appointmentService.deleteAppointment(id);
        if (deleted) {
            return responseHandler.responseBuilder("Từ chối lịch hẹn thành công", HttpStatus.OK, null);
        }
        return responseHandler.responseBuilder("Không tìm thấy lịch hẹn", HttpStatus.NOT_FOUND, null);
    }
}
