
  async function loadUserId() {
    const res = await fetch('/api/user/current', { credentials: "include" });
    const json = await res.json();
    const data = json.data;
    const form = document.forms['ChangePasswordForm'];
    form.userId.value = data.user_id;
  }
  document.getElementById("ChangePasswordForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const obj = Object.fromEntries(formData.entries());

    console.log(obj);

    const res = await fetch("/api/user/change-password", {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
      body: JSON.stringify(obj)
    });

<!--    const msg = document.getElementById("msg");-->
    if (res.ok) {
    alert("Đổi mật khẩu thành công!");
<!--      msg.textContent = "Đổi mật khẩu thành công!";-->
<!--      msg.classList.remove("text-danger");-->
<!--      msg.classList.add("text-success");-->
    } else {
    alert("Đổi mật khẩu thất bại! Mật khẩu cũ không đúng!");
<!--      msg.textContent = "Đổi mật khẩu thất bại! Mật khẩu cũ không đúng!";-->
<!--      msg.classList.remove("text-success");-->
<!--      msg.classList.add("text-danger");-->
    }
  });
  window.addEventListener("DOMContentLoaded", loadUserId);
