const urlParams = new URLSearchParams(window.location.search);
const id = urlParams.get("id");

//  Tải danh sách bác sĩ
async function loadDoctors() {
  try {
    const res = await fetch("/api/user/doctors");
    const json = await res.json();
    const select = document.getElementById("doctorSelect");

    if (json.data && Array.isArray(json.data)) {
      json.data.forEach(doc => {
        const opt = document.createElement("option");
        opt.value = doc.doctor_id;
        opt.textContent = `${doc.fullName} (${doc.specialist})`;
        select.appendChild(opt);
      });
    } else {
      showMessage("Không có dữ liệu bác sĩ", false);
    }
  } catch (err) {
    console.error("Lỗi loadDoctors:", err);
    showMessage("Không thể tải danh sách bác sĩ", false);
  }
}

//  Tải thông tin lịch hẹn
async function loadAppointment() {
  try {
    const res = await fetch(`/api/user/appointments/${id}`, {
      credentials: 'include'
    });
    const json = await res.json();
    const doc = json.data;
    const form = document.forms['editAppointmentForm'];

    if (doc && form) {
      form.fullname.value = doc.fullname;
      form.gender.value = doc.gender;
      form.age.value = doc.age;
      form.appointmentDate.value = formatDateForInput(doc.appointmentDate);
      form.phone.value = doc.phone;
      form.diseases.value = doc.diseases;
      form.address.value = doc.address;
      form.doctorId.value = doc.doctorId;
      form.userId.value = doc.userId;
      form.id.value = doc.id;
    } else {
      showMessage("Không tìm thấy dữ liệu lịch hẹn", false);
    }
  } catch (err) {
    console.error("Lỗi loadAppointment:", err);
    showMessage("Tải thông tin lịch hẹn thất bại", false);
  }
}

//  Submit form cập nhật lịch hẹn
async function handleUpdateAppointment(e) {
  e.preventDefault();
  clearMessage();

  const form = e.target;
  const obj = Object.fromEntries(new FormData(form).entries());

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
    const res = await fetch(`/api/user/appointments/${obj.id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json"
      },
      credentials: "include",
      body: JSON.stringify(obj)
    });

    const data = await res.json();
    const message = data.message || "Không rõ kết quả.";

    if (res.ok && message.includes("thành công")) {
      showMessage(message, true);
      // form.reset(); // Gỡ comment nếu muốn reset form sau khi sửa xong
    } else {
      showMessage(message, false);
    }
  } catch (err) {
    console.error("Lỗi submit update:", err);
    showMessage("Lỗi kết nối. Vui lòng thử lại.", false);
  }
}

//  Khởi tạo khi trang tải xong
window.addEventListener("DOMContentLoaded", () => {
  loadDoctors();
  loadAppointment();
  document.getElementById("editAppointmentForm").addEventListener("submit", handleUpdateAppointment);
});
