fetch('/component/navbar.html')
  .then(res => res.text())
  .then(data => {
    document.getElementById('navbar').innerHTML = data;

    const navList = document.getElementById('nav-items');
    if (!navList) return;

    fetch('/api/user/current', {
      method: 'GET',
      credentials: 'include',
      headers: { 'Accept': 'application/json' }
    })
      .then(response => {
        if (!response.ok) throw new Error('Not logged in');
        return response.json();
      })
      .then(data => {
        const user = data.data;
        navList.innerHTML = `
          <li class="nav-item"><a class="nav-link" href="/user/view-doctor.html"><i class="fa fa-book fa-1x"></i> DANH SÁCH BÁC SĨ</a></li>
          <li class="nav-item"><a class="nav-link" href="/user/make-appointment.html"><i class="fa fa-book fa-1x"></i> ĐẶT LỊCH</a></li>
          <li class="nav-item"><a class="nav-link" href="/user/view-appointment.html"><i class="fa fa-calendar-check-o"></i> LỊCH SỬ ĐẶT LỊCH</a></li>
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="userMenu"
              data-bs-toggle="dropdown" aria-expanded="false" role="button">
              <i class="fa-solid fa-circle-user"></i> ${user.fullName || user.name || 'User'}
            </a>
            <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userMenu">
              <li><a class="dropdown-item" href="/user/change-password.html">Đổi mật khẩu</a></li>
              <li><a class="dropdown-item" href="#" onclick="logout('/api/user/logout','/index.html')">Đăng xuất</a></li>
            </ul>
          </li>
        `;

        // 👇 Re-activate Bootstrap dropdown manually
        const dropdownTriggerList = document.querySelectorAll('[data-bs-toggle="dropdown"]');
        dropdownTriggerList.forEach(el => new bootstrap.Dropdown(el));
      })
      .catch(error => {
        navList.innerHTML = `
          <li class="nav-item"><a class="nav-link" href="admin_login.html"><i class="fa-solid fa-right-to-bracket"></i> QUẢN TRỊ VIÊN</a></li>
          <li class="nav-item"><a class="nav-link" href="doctor_login.html"><i class="fas fa-sign-in-alt"></i> BÁC SĨ</a></li>
          <li class="nav-item"><a class="nav-link" href="user_login.html"><i class="fas fa-sign-in-alt"></i> NGƯỜI DÙNG</a></li>
        `;
      });
  });
