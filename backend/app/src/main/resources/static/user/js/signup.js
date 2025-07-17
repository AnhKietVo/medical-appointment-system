//  Xử lý submit form đăng ký
async function onRegisterSubmit(e) {
  e.preventDefault();
  clearMessage();

  let fullName = formatFullName(e.target.fullName.value);
  const email = e.target.email.value.trim();
  const password = e.target.password.value;

  if (!fullName || !email || !password) {
    showMessage("Vui lòng nhập đầy đủ thông tin", false);
    return;
  }

  try {
    const res = await fetch('/api/user/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ fullName, email, password })
    });

    const data = await res.json();
    const message = data.message || "Không rõ kết quả.";

    if (res.ok && message.includes("thành công")) {
      showMessage(message, true);
      e.target.reset();
    } else {
      showMessage(message, false);
    }
  } catch (err) {
    console.error("Lỗi đăng ký:", err);
    showMessage("Lỗi kết nối. Vui lòng thử lại.", false);
  }
}

//  Khởi tạo
document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('RegisterForm');
  if (form) {
    form.addEventListener('submit', onRegisterSubmit);
  } else {
    console.error("Không tìm thấy form #RegisterForm");
  }
});
