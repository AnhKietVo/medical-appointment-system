function renderDoctorRow(doc) {
  const row = document.createElement("tr");
  row.innerHTML = `
    <td>${doc.fullName}</td>
    <td>${doc.dateOfBirth}</td>
    <td>${doc.qualification}</td>
    <td>${doc.specialist}</td>
    <td>${doc.email}</td>
    <td>${doc.phone}</td>
    <td><a href="edit-doctor.html?id=${doc.doctor_id}" class="btn btn-sm btn-primary">Sửa</a></td>
    <td><button class="btn btn-sm btn-danger" onclick="handleDeleteDoctor(${doc.doctor_id})">Xóa</button></td>
  `;
  return row;
}

function updateDoctorTable(doctors) {
  const tbody = document.querySelector("#doctorTable tbody");
  tbody.innerHTML = "";
  doctors.forEach(doc => {
    tbody.appendChild(renderDoctorRow(doc));
  });
}


async function fetchDoctorList() {
  const res = await fetch('/api/admin/doctor-list', { credentials: 'include' });
  const json = await res.json();
  return json.data || [];
}

async function deleteDoctorById(id) {
  const res = await fetch(`/api/admin/delete-doctor/${id}`, {
    method: 'DELETE',
    credentials: 'include'
  });
  return { ok: res.ok, text: await res.text() };
}


async function handleDeleteDoctor(id) {
  if (!confirm("Bạn có chắc chắn muốn xóa bác sĩ này?")) return;

  try {
    const { ok, text } = await deleteDoctorById(id);
    if (ok) {
      showMessage("Xóa bác sĩ thành công.", true);
      await loadDoctors();
    } else {
      console.error("Delete failed:", text);
      showMessage("Xóa bác sĩ thất bại.", false);
    }
  } catch (err) {
    console.error("Lỗi khi xóa:", err);
    showMessage("Xảy ra lỗi không thể xóa bác sĩ.", false);
  }
}


async function loadDoctors() {
  try {
    const doctors = await fetchDoctorList();
    updateDoctorTable(doctors);
  } catch (err) {
    console.error("Lỗi tải dữ liệu:", err);
    showMessage("Không thể tải danh sách bác sĩ.", false);
  }
}

window.addEventListener('DOMContentLoaded', loadDoctors);
