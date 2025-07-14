const urlParams = new URLSearchParams(window.location.search);
const id = urlParams.get("id");


async function loadAppointment() {
  try {
    const res = await fetch(`/api/doctor/appointment/${id}`, { credentials: "include" });
    const json = await res.json();

    if (!res.ok || !json.data) {
      throw new Error(json.message || "Không tìm thấy dữ liệu");
    }

    const data = json.data;
    const form = document.forms["commentForm"];

    form.id.value = data.id;
    form.fullName.value = data.fullname;
    form.age.value = data.age;
    form.phone.value = data.phone;
    form.diseases.value = data.diseases;

  } catch (err) {
    console.error("Lỗi khi tải thông tin lịch hẹn:", err);
    showMessage("Không thể tải thông tin lịch hẹn!", false);
  }
}

// ✅ Xử lý gửi comment
async function handleCommentSubmit(e) {
  e.preventDefault();
  clearMessage();

  const formData = new FormData(e.target);
  const obj = Object.fromEntries(formData.entries());

  try {
    const res = await fetch("/api/doctor/appointment/comment", {
      method: "PUT",
      headers: {
        "Content-Type": "application/json"
      },
      credentials: "include",
      body: JSON.stringify(obj)
    });

    const data = await res.json();
    const message = data.message;

    if (res.ok) {
      showMessage(message, true);
      e.target.reset(); // nếu muốn xóa form
    } else {
      showMessage(message, false);
    }
  } catch (err) {
    console.error("Lỗi khi gửi chuẩn đoán:", err);
    showMessage("Lỗi kết nối đến máy chủ. Vui lòng thử lại!", false);
  }
}

window.addEventListener("DOMContentLoaded", () => {
  loadAppointment();
  document.getElementById("commentForm").addEventListener("submit", handleCommentSubmit);
});
