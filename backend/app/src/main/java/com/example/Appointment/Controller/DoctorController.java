package com.example.Appointment.Controller;

import com.example.Appointment.DTO.AppointmentDTO;
import com.example.Appointment.DTO.CommentDTO;
import com.example.Appointment.DTO.DoctorDTO;
import com.example.Appointment.DTO.UserDTO;
import com.example.Appointment.Response.ResponseHandler;
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
@RequestMapping("/api/doctor")
public class DoctorController {
    DoctorService doctorService;
    AppointmentService appointmentService;
    ResponseHandler responseHandler;

    public DoctorController(DoctorService doctorService, AppointmentService appointmentService, ResponseHandler responseHandler) {
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
        this.responseHandler = responseHandler;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody DoctorDTO doctorDTO, HttpSession session) {
        DoctorDTO doctor = doctorService.login(doctorDTO.getEmail(), doctorDTO.getPassword());

        if (doctor != null) {
            session.setAttribute("doctorObj", doctor);
            return responseHandler.responseBuilder("Đăng nhập thành công!", HttpStatus.OK, null);
        } else {
            return responseHandler.responseBuilder("Email hoặc mật khẩu không hợp lệ", HttpStatus.NOT_FOUND, null);
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Object> getDoctorDashboard(HttpSession session) {
        DoctorDTO sessionDoctor = (DoctorDTO) session.getAttribute("doctorObj");
        if (sessionDoctor == null) {
            return responseHandler.responseBuilder("Bác sĩ chưa đăng nhập", HttpStatus.UNAUTHORIZED, null);
        }
        System.out.println("Doctor ID from session: " + sessionDoctor.getDoctor_id());
        int totalAppointments = appointmentService.countAppointmentsByDoctorIdAndStatus(sessionDoctor.getDoctor_id(), "Đã duyệt");

        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalAppointments", totalAppointments);

        return responseHandler.responseBuilder("Tải dữ liệu thành công", HttpStatus.OK, stats);
    }

    @GetMapping("/current")
    public ResponseEntity<Object> getCurrentUser(HttpSession session) {
        DoctorDTO sessionDoctor = (DoctorDTO) session.getAttribute("doctorObj");

        if (sessionDoctor != null) {
            return responseHandler.responseBuilder("Bác sĩ đã trong phiên đăng nhập", HttpStatus.OK, sessionDoctor);
        } else {
            return responseHandler.responseBuilder("Bác sĩ chưa đăng nhập", HttpStatus.UNAUTHORIZED, null);
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpSession session) {
        DoctorDTO sessionDoctor = (DoctorDTO) session.getAttribute("doctorObj");

        if (sessionDoctor != null) {
            session.removeAttribute("doctorObj");
            return responseHandler.responseBuilder("Đăng xuất thành công", HttpStatus.OK, null);
        } else {
            return responseHandler.responseBuilder("Không có bác sĩ nào đang đăng nhập", HttpStatus.UNAUTHORIZED, null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getDoctorById(@PathVariable int id, HttpSession session) {
        DoctorDTO sessionDoctor = (DoctorDTO) session.getAttribute("doctorObj");
        if (sessionDoctor == null) {
            return responseHandler.responseBuilder("Bác sĩ chưa đăng nhập", HttpStatus.UNAUTHORIZED, null);
        }

        if (sessionDoctor.getDoctor_id() != id) {
            return responseHandler.responseBuilder("Không có quyền truy cập thông tin người khác", HttpStatus.FORBIDDEN, null);
        }
        DoctorDTO doctorDTO = doctorService.getDoctorById(id);
        if (doctorDTO != null){
            return responseHandler.responseBuilder("Lấy dữ liệu thành công", HttpStatus.OK, doctorDTO);
        }
        return  responseHandler.responseBuilder("Không tìm thấy bác sĩ", HttpStatus.NOT_FOUND, null);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateDoctor(@PathVariable int id, @RequestBody DoctorDTO doctorDTO, HttpSession session) {
        DoctorDTO sessionDoctor = (DoctorDTO) session.getAttribute("doctorObj");
        if (sessionDoctor == null) {
            return responseHandler.responseBuilder("Bác sĩ chưa đăng nhập", HttpStatus.UNAUTHORIZED, null);
        }

        doctorDTO.setDoctor_id(id); // Gán id từ URL vào DTO
        boolean updated = doctorService.updateDoctor(doctorDTO);
        if (updated) {
            return responseHandler.responseBuilder("Cập nhật thông tin thành công", HttpStatus.OK, doctorDTO);
        }
        return responseHandler.responseBuilder("Cập nhật thông tin thất bại", HttpStatus.NOT_FOUND, null);
    }

    @GetMapping("/appointments/{doctorId}")
    public ResponseEntity<Object> showAllAppointmentsByDoctor(@PathVariable int doctorId, HttpSession session) {
        DoctorDTO sessionDoctor = (DoctorDTO) session.getAttribute("doctorObj");
        if (sessionDoctor == null) {
            return responseHandler.responseBuilder("Bác sĩ chưa đăng nhập", HttpStatus.UNAUTHORIZED, null);
        }
        List<AppointmentDTO> list = appointmentService.getAllAppointmentsByDoctorId(doctorId);
        return responseHandler.responseBuilder("Lấy dữ liệu thành công", HttpStatus.OK, list);
    }

    @PutMapping("/appointment/comment")
    public ResponseEntity<Object> updateAppointmentComment(@RequestBody CommentDTO commentDTO, HttpSession session) {
        DoctorDTO sessionDoctor = (DoctorDTO) session.getAttribute("doctorObj");
        if (sessionDoctor == null) {
            return responseHandler.responseBuilder("Bác sĩ chưa đăng nhập", HttpStatus.UNAUTHORIZED, null);
        }
        boolean updated = appointmentService.updateComment(commentDTO.getId(), commentDTO.getComment());
        if (updated) {
            return responseHandler.responseBuilder("Thêm chuẩn đoán thành công", HttpStatus.OK, commentDTO);
        }
        return responseHandler.responseBuilder("Thêm chuẩn đoán thất bại", HttpStatus.NOT_FOUND, null);
    }

    @GetMapping("/appointment/{id}")
    public ResponseEntity<Object> getAppointmentById(@PathVariable int id, HttpSession session) {
        DoctorDTO sessionDoctor = (DoctorDTO) session.getAttribute("doctorObj");
        if (sessionDoctor == null) {
            return responseHandler.responseBuilder("Bác sĩ chưa đăng nhập", HttpStatus.UNAUTHORIZED, null);
        }
        AppointmentDTO appointmentDTO = appointmentService.getAppointmentById(id);
        if (appointmentDTO != null){
            return responseHandler.responseBuilder("Lấy dữ liệu thành công", HttpStatus.OK, appointmentDTO);
        }
        return  responseHandler.responseBuilder("Không tìm thấy lịch hẹn", HttpStatus.NOT_FOUND, null);
    }

}
