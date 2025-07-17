//  Lấy user hiện tại và gán vào form
async function loadCurrentUserIdToForm() {
  try {
    const res = await fetch('/api/user/current', { credentials: "include" });
    const json = await res.json();
    const data = json.data;

    const form = document.forms['ChangePasswordForm'];
    if (data && form && form.userId) {
      form.userId.value = data.user_id;
    } else {
      showMessage("Không thể lấy thông tin người dùng.", false);
    }
  } catch (err) {
    console.error("Lỗi khi tải user hiện tại:", err);
    showMessage("Lỗi kết nối đến máy chủ.", false);
  }
}

//  Xử lý submit form đổi mật khẩu
async function handleChangePasswordSubmit(e) {
  e.preventDefault();
  clearMessage();

  const formData = new FormData(e.target);
  const obj = Object.fromEntries(formData.entries());

  try {
    const res = await fetch("/api/user/change-password", {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
      body: JSON.stringify(obj)
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
    console.error("Lỗi:", err);
    showMessage("Lỗi kết nối. Vui lòng thử lại.", false);
  }
}

//  Gán sự kiện khi trang tải xong
document.addEventListener("DOMContentLoaded", () => {
  loadCurrentUserIdToForm();

  const form = document.getElementById("ChangePasswordForm");
  if (form) {
    form.addEventListener("submit", handleChangePasswordSubmit);
  }
});
