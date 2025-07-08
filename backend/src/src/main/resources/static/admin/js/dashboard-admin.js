async function loadDashboard() {
  try {
    const res = await fetch('/api/admin/dashboard', { credentials: 'include' });
    if (res.status === 401) {
      window.location.href = '/admin_login.html';
      return;
    }

    const data = await res.json();

    document.getElementById('doctorCount').textContent = data.data.doctors;
    document.getElementById('userCount').textContent = data.data.users;
    document.getElementById('appointmentCount').textContent = data.data.appointments;

    document.getElementById('dashboard-stats').style.display = 'flex';
  } catch (err) {
    document.getElementById('msg').textContent = 'Lỗi tải dữ liệu.';
    console.error(err);
  }
}

window.addEventListener('DOMContentLoaded', loadDashboard);
