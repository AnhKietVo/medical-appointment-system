async function loadDoctors() {
  try {
    const res = await fetch('/api/admin/doctor-list', { credentials: 'include' });
    const json = await res.json();
    const doctors = json.data;

    const tbody = document.querySelector("#doctorTable tbody");
    tbody.innerHTML = '';

    doctors.forEach(doc => {
      const row = document.createElement("tr");
      row.innerHTML = `
        <td>${doc.fullName}</td>
        <td>${doc.dateOfBirth}</td>
        <td>${doc.qualification}</td>
        <td>${doc.specialist}</td>
        <td>${doc.email}</td>
        <td>${doc.phone}</td>
        <td><a href="edit-doctor.html?id=${doc.doctor_id}" class="btn btn-sm btn-primary">Edit</a></td>
        <td><button onclick="deleteDoctor(${doc.doctor_id})" class="btn btn-sm btn-danger">Delete</button></td>
      `;
      tbody.appendChild(row);
    });
  } catch (err) {
    document.getElementById("msg").textContent = "Lỗi tải dữ liệu.";
    console.error(err);
  }
}

async function deleteDoctor(id) {
  if (!confirm("Bạn có chắc chắn muốn xóa bác sĩ này?")) return;

  try {
    const res = await fetch(`/api/admin/delete-doctor/${id}`, {
      method: 'DELETE',
      credentials: 'include'
    });

    if (res.ok) {
      alert("Xóa bác sĩ thành công.");
      loadDoctors(); // reload danh sách
    } else {
      const errorText = await res.text();
      console.error("Delete failed:", errorText);
      alert("Xóa bác sĩ thất bại.");
    }
  } catch (err) {
    alert("Xảy ra lỗi không thể xóa bác sĩ.");
    console.error(err);
  }
}

window.addEventListener('DOMContentLoaded', loadDoctors);
