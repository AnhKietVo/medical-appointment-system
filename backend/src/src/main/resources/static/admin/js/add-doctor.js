//  Xử lý sự kiện submit form
async function handleDoctorFormSubmit(e) {
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
    const res = await fetch('/api/admin/add-doctor', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify(obj)
    });

    const result = await res.json();
    const message = result.message;

    if (res.ok && message.includes("thành công")) {
      showMessage(message, true);
      form.reset();
    } else {
      showMessage(message, false);
    }

  } catch (err) {
    console.error("Lỗi kết nối:", err);
    showMessage("Lỗi kết nối. Vui lòng thử lại.", false);
  }
}

//  Gắn sự kiện khi DOM đã load
document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('doctorForm');
  if (form) {
    form.addEventListener('submit', handleDoctorFormSubmit);
  } else {
    console.error("Không tìm thấy form #doctorForm");
  }
});
