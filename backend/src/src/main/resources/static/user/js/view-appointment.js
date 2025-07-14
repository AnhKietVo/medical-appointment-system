//  Sắp xếp lịch hẹn theo trạng thái ưu tiên "Chờ duyệt"
function sortAppointments(list) {
  return list.sort((a, b) => {
    if (a.status === "Chờ duyệt" && b.status !== "Chờ duyệt") return -1;
    if (a.status !== "Chờ duyệt" && b.status === "Chờ duyệt") return 1;
    return 0;
  });
}

//  Tạo phần tử <tr> cho từng lịch hẹn
function renderAppointmentRow(app) {
  const row = document.createElement("tr");

  const statusBadge = app.status === "Đã duyệt"
    ? '<span class="badge bg-warning text-dark">Đã duyệt</span>'
    : app.status === "Chờ duyệt"
    ? '<span class="badge bg-danger">Chờ duyệt</span>'
    : app.status;

  const actions = app.status === "Chờ duyệt"
    ? `
      <td><a href="edit-appointment.html?id=${app.id}" class="btn btn-sm btn-primary">Sửa</a></td>
      <td><button class="btn btn-sm btn-danger" onclick="deleteAppointment(${app.id})">Xóa</button></td>
    `
    : `<td></td><td></td>`;

  row.innerHTML = `
    <td>${app.fullname}</td>
    <td>${app.gender}</td>
    <td>${app.age}</td>
    <td>${app.appointmentDate}</td>
    <td>${app.phone}</td>
    <td>${app.diseases}</td>
    <td>${app.doctorName}</td>
    <td>${statusBadge}</td>
    ${actions}
  `;
  return row;
}

//  Tải danh sách lịch hẹn và render vào bảng
async function loadAppointments() {
  try {
    const res = await fetch("/api/user/appointments", { credentials: "include" });
    const json = await res.json();

    const tbody = document.querySelector("#appointmentTable tbody");
    tbody.innerHTML = "";

    const sorted = sortAppointments(json.data);
    sorted.forEach(app => tbody.appendChild(renderAppointmentRow(app)));

  } catch (err) {
    console.error("Lỗi tải dữ liệu:", err);
    showMessage("Không thể tải danh sách lịch hẹn.", false);
  }
}

//  Xoá lịch hẹn theo ID
async function deleteAppointment(id) {
  if (!confirm("Bạn có chắc chắn muốn xóa lịch hẹn này?")) return;
  clearMessage();

  try {
    const res = await fetch(`/api/user/appointments/${id}`, {
      method: "DELETE",
      credentials: "include"
    });

    const data = await res.json();
    const message = data.message || "Không rõ kết quả.";

    if (res.ok && message.includes("thành công")) {
      showMessage(message, true);
      loadAppointments();
    } else {
      showMessage(message, false);
    }
  } catch (err) {
    console.error("Lỗi xoá:", err);
    showMessage("Lỗi kết nối. Vui lòng thử lại.", false);
  }
}

//  Khởi động khi DOM sẵn sàng
window.addEventListener("DOMContentLoaded", loadAppointments);
