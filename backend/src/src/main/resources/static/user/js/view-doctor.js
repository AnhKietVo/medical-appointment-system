async function loadDoctors() {
  try {
    const res = await fetch('/api/user/doctors', { credentials: 'include' });
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
      `;
      tbody.appendChild(row);
    });

  } catch (err) {
    document.getElementById("msg").textContent = "Lỗi tải dữ liệu.";
    console.error("Lỗi khi tải danh sách bác sĩ:", err);
  }
}

window.addEventListener('DOMContentLoaded', loadDoctors);
