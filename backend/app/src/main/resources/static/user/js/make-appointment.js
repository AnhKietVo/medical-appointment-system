// Load danh sách bác sĩ vào <select>
async function loadDoctors() {
  try {
    const res = await fetch("/api/user/doctors");
    const json = await res.json();
    const select = document.getElementById("doctorSelect");

    if (!json.data || !Array.isArray(json.data)) {
      return showMessage("Dữ liệu bác sĩ không hợp lệ", false);
    }

    json.data.forEach(doc => {
      const opt = document.createElement("option");
      opt.value = doc.doctor_id;
      opt.textContent = `${doc.fullName} (${doc.specialist})`;
      select.appendChild(opt);
    });
  } catch (error) {
    console.error("Failed to load doctors:", error);
    showMessage("Không thể tải danh sách bác sĩ", false);
  }
}

// Load user hiện tại (và redirect nếu chưa đăng nhập)
async function loadUser() {
  try {
    const res = await fetch("/api/user/current", { credentials: "include" });
    const json = await res.json();
    if (json.data?.user_id) {
      document.getElementById("userId").value = json.data.user_id;
    } else {
      window.location.href = "/user_login.html";
    }
  } catch (error) {
    console.error("Error loading user:", error);
    window.location.href = "/user_login.html";
  }
}

// Submit form tạo lịch hẹn
async function handleAppointmentSubmit(e) {
  e.preventDefault();
  clearMessage();

  const form = e.target;
  const formData = new FormData(form);
  const obj = Object.fromEntries(formData.entries());

  // Gán biến để kiểm tra
  obj.fullname = formatFullName(obj.fullname);
  const fullName = obj.fullname;
  const age = obj.age;
  const phone = obj.phone;
  const appointmentDate = obj.appointmentDate;

  // Kiểm tra dữ liệu đầu vào
  if (!isValidFullName(fullName)) {
    return showMessage("Họ tên không hợp lệ (chỉ chứa chữ cái và khoảng trắng).", false);
  }

  if (!isValidAge(age)) {
    return showMessage("Tuổi phải từ 1 đến 120.", false);
  }

  if (!isValidPhone(phone)) {
    return showMessage("Số điện thoại không hợp lệ (9–11 chữ số).", false);
  }

  if (!isValidFutureDate(appointmentDate)) {
    return showMessage("Ngày hẹn không được nhỏ hơn hôm nay.", false);
  }

  obj.appointmentDate = formatDateForAPI(appointmentDate);

  try {
    const res = await fetch("/api/user/appointment", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
      body: JSON.stringify(obj)
    });

    const data = await res.json();
    const message = data.message || "Không rõ kết quả.";

    if (res.ok && message.includes("thành công")) {
      showMessage(message, true);
      form.reset();
    } else {
      showMessage(message, false);
    }
  } catch (err) {
    console.error("Lỗi submit:", err);
    showMessage("Lỗi kết nối. Vui lòng thử lại.", false);
  }
}

// Khởi tạo khi DOM sẵn sàng
window.addEventListener("DOMContentLoaded", () => {
  loadDoctors();
  loadUser();
  document.getElementById("appointmentForm").addEventListener("submit", handleAppointmentSubmit);
});
