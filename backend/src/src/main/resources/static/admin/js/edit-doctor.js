const urlParams = new URLSearchParams(window.location.search);
const id = urlParams.get("id");

async function loadDoctor() {
  try {
    const res = await fetch(`/api/admin/doctor/${id}`, { credentials: 'include' });
    const json = await res.json();
    const doc = json.data;

    const form = document.forms['editDoctorForm'];
    form.fullName.value = doc.fullName;
    form.dateOfBirth.value = formatDateForInput(doc.dateOfBirth);
    form.qualification.value = doc.qualification;
    form.specialist.value = doc.specialist;
    form.email.value = doc.email;
    form.phone.value = doc.phone;
    form.password.value = doc.password;
    form.id.value = doc.doctor_id;
  } catch (err) {
    document.getElementById("msg").textContent = "Lỗi tải dữ liệu.";
    console.error(err);
  }
}

document.getElementById("editDoctorForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const form = e.target;
  const doctorData = Object.fromEntries(new FormData(form).entries());
  doctorData.dateOfBirth = formatDateForAPI(doctorData.dateOfBirth);
  try {
    const res = await fetch(`/api/admin/update-doctor/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json"
      },
      credentials: "include",
      body: JSON.stringify(doctorData)
    });

    const msgEl = document.getElementById("msg");
    if (res.ok) {
      msgEl.textContent = "Cập nhật thông tin bác sĩ thành công!";
    } else {
      msgEl.textContent = "Cập nhật thông tin bác sĩ thất bại!";
    }
  } catch (err) {
    console.error("Update failed", err);
    document.getElementById("msg").textContent = "Xảy ra lỗi không thể cập nhật.";
  }
});

window.addEventListener("DOMContentLoaded", loadDoctor);
