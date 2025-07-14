//  Xóa bảng và render lại từng dòng
function renderAppointmentRow(app) {
  const row = document.createElement("tr");

  const actions = app.status === "Chờ duyệt"
    ? `
      <td>
        <button class="btn btn-sm btn-primary" onclick="acceptAppointment(${app.id})">Duyệt</button>
      </td>
      <td>
        <button class="btn btn-sm btn-danger" onclick="rejectAppointment(${app.id})">Từ chối</button>
      </td>
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
    <td>${app.address}</td>
    <td>${app.status}</td>
    ${actions}
  `;

  return row;
}

//  Load danh sách lịch hẹn
async function loadAppointments() {
  try {
    const res = await fetch("/api/admin/appointments", { credentials: "include" });
    const json = await res.json();
    const appointments = json.data || [];

    const tbody = document.querySelector("#appointmentTable tbody");
    tbody.innerHTML = "";

    appointments
      .sort((a, b) => {
        if (a.status === "Chờ duyệt" && b.status !== "Chờ duyệt") return -1;
        if (a.status !== "Chờ duyệt" && b.status === "Chờ duyệt") return 1;
        return 0;
      })
      .forEach(app => {
        const row = renderAppointmentRow(app);
        tbody.appendChild(row);
      });
  } catch (err) {
    console.error("Lỗi tải dữ liệu:", err);
    showMessage("Không thể tải danh sách lịch hẹn.", false);
  }
}

//  Duyệt lịch hẹn
async function acceptAppointment(id) {
  if (!confirm("Bạn có chắc chắn muốn duyệt lịch hẹn này?")) return;

  try {
    const res = await fetch(`/api/admin/appointments/accept/${id}`, {
      method: "PUT",
      credentials: "include"
    });
    const data = await res.json();
    const message = data.message || "Duyệt lịch hẹn thành công.";

    if (res.ok) {
      showMessage(message, true);
      loadAppointments();
    } else {
      showMessage(message, false);
    }
  } catch (err) {
    console.error("Accept error:", err);
    showMessage("Lỗi kết nối khi duyệt lịch hẹn.", false);
  }
}

//  Từ chối lịch hẹn
async function rejectAppointment(id) {
  if (!confirm("Bạn có chắc chắn muốn từ chối lịch hẹn này?")) return;

  try {
    const res = await fetch(`/api/admin/appointments/reject/${id}`, {
      method: "DELETE",
      credentials: "include"
    });

    if (res.ok) {
      showMessage("Từ chối thành công.", true);
      loadAppointments();
    } else {
      const errorText = await res.text();
      console.error("Delete failed:", errorText);
      showMessage("Từ chối thất bại.", false);
    }
  } catch (err) {
    console.error("Lỗi reject:", err);
    showMessage("Lỗi kết nối khi từ chối lịch hẹn.", false);
  }
}

// ✅ Gắn sự kiện khi DOM load xong
window.addEventListener("DOMContentLoaded", loadAppointments);
