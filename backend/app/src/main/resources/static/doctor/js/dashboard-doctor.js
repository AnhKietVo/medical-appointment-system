
  async function loadDashboard() {
    try {
      const res = await fetch("/api/doctor/dashboard", {
        credentials: 'include'
      });

      if (res.status === 401) {
        window.location.href = "/doctor_login.html";
        return;
      }

      const data = await res.json();
      document.getElementById("totalAppointments").textContent = data.data.totalAppointments;
    } catch (err) {
      console.error("Lỗi tải dữ liệu", err);
    }
  }

  window.addEventListener("DOMContentLoaded", loadDashboard);

  // Tải thông tin bác sĩ đang login từ session
  async function loadDoctorName() {
    try {
      const res = await fetch("/api/doctor/current", { credentials: "include" });
      if (res.ok) {
        const json = await res.json();
        document.getElementById("doctorName").textContent = json.data.fullName;
      } else {
        window.location.href = "/doctor_login.html";
      }
    } catch (err) {
      console.error("Không thể tải tên bác sĩ:", err);
      window.location.href = "/doctor_login.html";
    }
  }


  window.addEventListener("DOMContentLoaded", loadDoctorName);