
// Load navbar vào elementId từ filePath
function loadNavbar(elementId, filePath) {
  fetch(filePath)
    .then(res => res.text())
    .then(data => {
      document.getElementById(elementId).innerHTML = data;
    });
}

// Load footer vào elementId từ filePath
function loadFooter(elementId, filePath) {
  fetch(filePath)
    .then(res => res.text())
    .then(data => {
      document.getElementById(elementId).innerHTML = data;
    });
}

// Format dd/MM/yyyy -> yyyy-MM-dd cho input type="date"
function formatDateForInput(dateStr) {
  const [day, month, year] = dateStr.split('/');
  const paddedDay = day.padStart(2, '0');
  const paddedMonth = month.padStart(2, '0');
  return `${year}-${paddedMonth}-${paddedDay}`;
}

// Format yyyy-MM-dd -> dd/MM/yyyy khi gửi về API
function formatDateForAPI(dateStr) {
  const [year, month, day] = dateStr.split("-");
  return `${day}/${month}/${year}`;
}
async function handleLogin(formId, apiUrl, redirectUrl, isFormEncoded = false) {
  const form = document.getElementById(formId);
  if (!form) {
    console.error(`Form ID ${formId} not found`);
    return;
  }

  form.addEventListener('submit', async function (e) {
    e.preventDefault();
    const email = form.email.value;
    const password = form.password.value;

    let headers = {};
    let body;

    if (isFormEncoded) {
      headers['Content-Type'] = 'application/x-www-form-urlencoded';
      body = new URLSearchParams({ email, password });
    } else {
      headers['Content-Type'] = 'application/json';
      body = JSON.stringify({ email, password });
    }

    try {
      const res = await fetch(apiUrl, {
        method: 'POST',
        headers,
        credentials: 'include',
        body
      });

      const data = await res.json();
      const message = data.message || 'Đăng nhập thất bại.';

      const msgEl = document.getElementById('message') || document.getElementById('errorMsg');
      if (!msgEl) {
        alert(message);
        return;
      }

      if (res.ok && message.includes("thành công")) {
        msgEl.textContent = message;
        msgEl.className = "text-success text-center fs-5";
        msgEl.style.display = 'block';
        setTimeout(() => {
          window.location.href = redirectUrl;
        }, 1000);
      } else {
        msgEl.textContent = message;
        msgEl.className = "text-danger text-center fs-5";
        msgEl.style.display = 'block';
      }
    } catch (err) {
      console.error("Login error:", err);
      const msgEl = document.getElementById('message') || document.getElementById('errorMsg');
      if (msgEl) {
        msgEl.textContent = "Lỗi kết nối. Vui lòng thử lại.";
        msgEl.className = "text-danger text-center fs-5";
        msgEl.style.display = 'block';
      }
    }
  });
}

// Logout dùng chung
function logout(apiPath = '/api/logout', redirectTo = '/index.html') {
  fetch(apiPath, {
    method: 'POST',
    credentials: 'include'
  })
    .then(res => {
      if (res.ok) {
        window.location.href = redirectTo;
      } else {
        alert("Đăng xuất thất bại");
      }
    })
    .catch(err => {
      console.error('Logout failed', err);
    });
}

