async function loadAppointments() {
    try {
      // Lấy doctor hiện tại từ session (từ /api/doctor/current)
      const currentRes = await fetch("/api/doctor/current", { credentials: 'include' });
      const currentData = await currentRes.json();

      if (!currentData || !currentData.data) {
        document.getElementById("msg").textContent = "Doctor not logged in.";
        return;
      }

      const doctorId = currentData.data.doctor_id;

      // Gọi API lấy danh sách lịch hẹn theo doctorId
      const res = await fetch(`/api/doctor/appointments/${doctorId}`, { credentials: 'include' });
      const result = await res.json();
      const appointments = result.data;

      const tbody = document.getElementById("appointmentTableBody");
      tbody.innerHTML = "";

      appointments.forEach(app => {
        const row = document.createElement("tr");
        row.innerHTML = `
          <td>${app.fullname}</td>
          <td>${app.gender}</td>
          <td>${app.age}</td>
          <td>${app.appointmentDate}</td>
          <td>${app.phone}</td>
          <td>${app.diseases}</td>
          <td>${app.status}</td>
          <td>
            ${app.status === "Đã duyệt"
              ? `<a href="comment.html?id=${app.id}" class="btn btn-success btn-sm">Chuẩn đoán / Toa thuốc</a>`
              : `<a href="#!" class="btn btn-success btn-sm disabled"><i class="fa fa-comment"></i> Chuẩn đoán / Toa thuốc</a>`
            }
          </td>
        `;
        tbody.appendChild(row);
      });

    } catch (err) {
      console.error(err);
      document.getElementById("msg").textContent = "Lỗi tải dữ liệu.";
    }
  }
  window.addEventListener("DOMContentLoaded", loadAppointments);