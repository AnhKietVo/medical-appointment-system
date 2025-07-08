async function loadAppointments() {
  try {
    const res = await fetch("/api/user/appointments", { credentials: "include" });
    const json = await res.json();
    const appointments = json.data;

    const tbody = document.querySelector("#appointmentTable tbody");
    tbody.innerHTML = "";

    appointments
      .sort((a, b) => {
        if (a.status === "Chờ duyệt" && b.status !== "Chờ duyệt") return -1;
        if (a.status !== "Chờ duyệt" && b.status === "Chờ duyệt") return 1;
        return 0;
      })
      .forEach(app => {
        const row = document.createElement("tr");

        const actions =
          app.status === "Chờ duyệt"
            ? `
              <td>
                <a href="edit-appointment.html?id=${app.id}" class="btn btn-sm btn-primary">Sửa</a>
              </td>
              <td>
                <button class="btn btn-sm btn-danger" onclick="deleteAppointment(${app.id})">Xóa</button>
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
          <td>
            ${
              app.status === "Đã duyệt"
                ? '<span class="badge bg-warning text-dark">Đã duyệt</span>'
                : app.status === "Chờ duyệt"
                ? '<span class="badge bg-danger">Chờ duyệt</span>'
                : app.status
            }
          </td>
          ${actions}
        `;
        tbody.appendChild(row);
      });

  } catch (err) {
    console.error("Lỗi tải dữ liệu:", err);
    alert("Không thể tải danh sách lịch hẹn.");
  }
}

async function deleteAppointment(id) {
  if (!confirm("Bạn có chắc chắn muốn xóa lịch hẹn này?")) return;

  try {
    const res = await fetch(`/api/user/appointments/${id}`, {
      method: 'DELETE',
      credentials: 'include'
    });

    if (res.ok) {
      alert("Xóa lịch hẹn thành công!");
      loadAppointments();
    } else {
      const errorText = await res.text();
      console.error("Xóa thất bại:", errorText);
      alert("Xóa lịch hẹn thất bại.");
    }
  } catch (err) {
    console.error("Lỗi khi xóa lịch:", err);
    alert("Đã xảy ra lỗi không thể xóa lịch.");
  }
}

window.addEventListener("DOMContentLoaded", loadAppointments);
