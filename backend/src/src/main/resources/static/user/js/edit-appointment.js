const urlParams = new URLSearchParams(window.location.search);
const id = urlParams.get("id");


async function loadDoctors() {
  const res = await fetch("/api/user/doctors");
  const json = await res.json();
  const select = document.getElementById("doctorSelect");
  json.data.forEach(doc => {
    const opt = document.createElement("option");
    opt.value = doc.doctor_id;
    opt.textContent = `${doc.fullName} (${doc.specialist})`;
    select.appendChild(opt);
  });
}

async function loadAppointment() {
  try {
    const res = await fetch(`/api/user/appointments/${id}`, { credentials: 'include' });
    const json = await res.json();
    const doc = json.data;
    const form = document.forms['editAppointmentForm'];

    form.fullname.value = doc.fullname;
    form.gender.value = doc.gender;
    form.age.value = doc.age;
    form.appointmentDate.value = formatDateForInput(doc.appointmentDate);
    form.phone.value = doc.phone;
    form.diseases.value = doc.diseases;
    form.address.value = doc.address;
    form.doctorId.value = doc.doctorId;
    form.userId.value = doc.userId;
    form.id.value = doc.id;

  } catch (err) {
    document.getElementById("msg").textContent = "Tải thông tin bác sĩ thất bại.";
    console.error(err);
  }
}

document.getElementById("editAppointmentForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const form = e.target;
  const app = Object.fromEntries(new FormData(form).entries());
  app.appointmentDate = formatDateForAPI(app.appointmentDate);

  try {
    const res = await fetch(`/api/user/appointments/${app.id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json"
      },
      credentials: "include",
      body: JSON.stringify(app)
    });

    if (res.ok) {
      alert("Thay đổi lịch hẹn thành công!");
      // window.location.href = "./view-appointment.html";
    } else {
      alert("Thay đổi lịch hẹn thất bại!");
    }
  } catch (err) {
    console.error("Update failed", err);
    document.getElementById("msg").textContent = "Xảy ra lỗi không thể thay đổi lịch!";
  }
});

window.addEventListener('DOMContentLoaded', () => {
  loadAppointment();
  loadDoctors();
});
