let doctorId = null;
async function getCurrentDoctorId() {
  try {
    const res = await fetch("/api/doctor/current", { credentials: 'include' });
    const json = await res.json();
    if (json.data && json.data.doctor_id) {
      doctorId = json.data.doctor_id;
      loadDoctor(doctorId);
    } else {
      showMessage("Không tìm thấy thông tin bác sĩ", false);
    }
  } catch (err) {
    console.error("Error fetching current doctor:", err);
    showMessage("Lỗi tải thông tin người dùng", false);
  }
}

//  Tải thông tin bác sĩ từ API
async function loadDoctor(id) {
  try {
    const res = await fetch(`/api/doctor/${id}`, { credentials: 'include' });
    const json = await res.json();
    const doc = json.data;

    const form = document.forms['editDoctorForm'];
    form.fullName.value = doc.fullName;
    form.dateOfBirth.value = formatDateForInput(doc.dateOfBirth);
    form.qualification.value = doc.qualification;
    form.specialist.value = doc.specialist;
    form.email.value = doc.email;
    form.phone.value = doc.phone;
    form.password.value = doc.password;
  } catch (err) {
    console.error("Error loading doctor:", err);
    showMessage("Lỗi lấy dữ liệu từ hệ thống", false);
  }
}

//  Gửi form cập nhật bác sĩ
document.getElementById("editDoctorForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  clearMessage();

  const form = e.target;
  const obj = Object.fromEntries(new FormData(form).entries());
  obj.fullName = formatFullName(obj.fullName);
  obj.dateOfBirth = formatDateForAPI(obj.dateOfBirth);

  const fullName = obj.fullName;
  const email = obj.email;
  const phone = obj.phone;
 if (!isValidFullName(fullName)) {
    return showMessage("Họ tên không hợp lệ (chỉ chứa chữ và khoảng trắng).", false);
  }

  if (!isValidEmail(email)) {
    return showMessage("Email không hợp lệ.", false);
  }

  if (!isValidPhone(phone)) {
    return showMessage("Số điện thoại không hợp lệ (9–11 chữ số).", false);
  }
  try {
    const res = await fetch(`/api/doctor/update/${doctorId}`, {
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
      // form.reset(); // Uncomment nếu muốn xóa form
    } else {
      showMessage(message, false);
    }
  } catch (err) {
    console.error("Lỗi:", err);
    showMessage("Lỗi kết nối. Vui lòng thử lại.", false);
  }
});

window.addEventListener('DOMContentLoaded', getCurrentDoctorId);