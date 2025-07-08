
async function loadDoctors() {
  try {
    const res = await fetch("/api/user/doctors");
    const json = await res.json();
    const select = document.getElementById("doctorSelect");

    if (!json.data || !Array.isArray(json.data)) {
      console.error("Invalid doctor data", json);
      return;
    }

    json.data.forEach(doc => {
      const opt = document.createElement("option");
      opt.value = doc.doctor_id;
      opt.textContent = `${doc.fullName} (${doc.specialist})`;
      select.appendChild(opt);
    });
  } catch (error) {
    console.error("Failed to load doctors:", error);
  }
}

async function loadUser() {
  try {
    const res = await fetch("/api/user/current", { credentials: "include" });
    const json = await res.json();
    if (json.data && json.data.user_id) {
      document.getElementById("userId").value = json.data.user_id;
    } else {
      window.location.href = "/user_login.html";
    }
  } catch (error) {
    console.error("Error loading user:", error);
    window.location.href = "/user_login.html";
  }
}

document.getElementById("appointmentForm").addEventListener("submit", async e => {
  e.preventDefault();
  const formData = new FormData(e.target);
  const obj = Object.fromEntries(formData.entries());
  obj.appointmentDate = formatDateForAPI(obj.appointmentDate);

  try {
    const res = await fetch("/api/user/appointment", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
      body: JSON.stringify(obj)
    });

    const resBody = await res.json();

    if (res.ok) {
      alert("Đặt lịch thành công! Vui lòng chờ được xét duyệt.");
      e.target.reset();
    } else {
      alert(resBody.message || "Đặt lịch thất bại!");
      console.error("Error details:", resBody);
    }
  } catch (error) {
    console.error("Network error:", error);
    alert("Xảy ra lỗi không thể đặt lịch!");
  }
});

window.addEventListener("DOMContentLoaded", () => {
  loadDoctors();
  loadUser();
});
