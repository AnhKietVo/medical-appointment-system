
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
// Kiểm tra họ tên hợp lệ (chỉ chứa chữ cái và khoảng trắng)
function isValidFullName(name) {
  return /^[A-Za-zÀ-ỹ\s]+$/.test(name.trim());
}

// Kiểm tra tuổi hợp lệ: 1–120
function isValidAge(age) {
  const num = parseInt(age);
  return !isNaN(num) && num > 0 && num <= 120;
}

// Kiểm tra số điện thoại hợp lệ: 9–11 chữ số
function isValidPhone(phone) {
  return /^[0-9]{9,11}$/.test(phone.trim());
}
// Kiểm tra email
function isValidEmail(email) {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}
// Kiểm tra ngày hẹn không nhỏ hơn hôm nay
function isValidFutureDate(dateStr) {
  const inputDate = new Date(dateStr);
  const today = new Date();
  today.setHours(0, 0, 0, 0);
  return inputDate >= today;
}
function formatFullName(name) {
  return name
    .trim()                          // Xoá khoảng trắng đầu/cuối
    .toLowerCase()                   // Chuyển hết thành thường
    .replace(/\s+/g, ' ')            // Nén khoảng trắng giữa các từ
    .split(' ')                      // Tách từng từ
    .map(word => word.charAt(0).toUpperCase() + word.slice(1)) // Viết hoa chữ đầu
    .join(' ');                      // Ghép lại thành chuỗi
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
      console.error("Lỗi đăng nhập:", err);
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
//  Hiển thị thông báo
function showMessage(message, isSuccess = true) {
  const msgEl = document.getElementById('message');
  if (!msgEl) return;
  msgEl.textContent = message;
  msgEl.className = `text-center fs-5 ${isSuccess ? "text-success" : "text-danger"}`;
  msgEl.style.display = 'block';
}

//  Xóa thông báo
function clearMessage() {
  const msgEl = document.getElementById('message');
  if (msgEl) {
    msgEl.textContent = "";
    msgEl.style.display = "none";
  }
}
