const urlParams = new URLSearchParams(window.location.search);
const id = urlParams.get("id");

//  Load thông tin bác sĩ từ API
async function loadDoctor() {
  try {
    const res = await fetch(`/api/admin/doctor/${id}`, { credentials: "include" });
    const json = await res.json();
    const doc = json.data;

    if (!doc) {
      showMessage("Không tìm thấy bác sĩ.", false);
      return;
    }

    const form = document.forms["editDoctorForm"];
    form.fullName.value = doc.fullName;
    form.dateOfBirth.value = formatDateForInput(doc.dateOfBirth);
    form.qualification.value = doc.qualification;
    form.specialist.value = doc.specialist;
    form.email.value = doc.email;
    form.phone.value = doc.phone;
    form.password.value = doc.password;
    form.id.value = doc.doctor_id;

  } catch (err) {
    console.error("Lỗi tải bác sĩ:", err);
    showMessage("Lỗi tải dữ liệu từ hệ thống.", false);
  }
}

//  Gửi form cập nhật thông tin bác sĩ
async function handleEditDoctorSubmit(e) {
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
    const res = await fetch(`/api/admin/update-doctor/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json"
      },
      credentials: "include",
      body: JSON.stringify(obj)
    });

    const data = await res.json();
    const message = data.message;

    if (res.ok && message.includes("thành công")) {
      showMessage(message, true);
    } else {
      showMessage(message, false);
    }

  } catch (err) {
    console.error("Lỗi cập nhật:", err);
    showMessage("Xảy ra lỗi. Vui lòng thử lại.", false);
  }
}

//  Khi DOM load xong
window.addEventListener("DOMContentLoaded", () => {
  loadDoctor();
  const form = document.getElementById("editDoctorForm");
  if (form) {
    form.addEventListener("submit", handleEditDoctorSubmit);
  } else {
    console.error("Không tìm thấy form editDoctorForm");
  }
});
